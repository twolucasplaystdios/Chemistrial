package net.twolucasplay.chemistrial.event;


import com.cosmads.chemica.registry.ChemicaFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.twolucasplay.chemistrial.Chemistrial;

@EventBusSubscriber(modid = Chemistrial.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModPotassiumExplosionInteractionHandler {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            Level level = itemEntity.level();
            if (level.isClientSide()) return;

            ItemStack stack = itemEntity.getItem();
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (itemId.getNamespace().equals("chemistrial") && itemId.getPath().equals("potassium_ingot")) {
                BlockPos pos = itemEntity.blockPosition();

                boolean isInWater = level.getFluidState(pos).is(FluidTags.WATER) ||
                        level.getFluidState(pos.above()).is(FluidTags.WATER);
                boolean isInDistilledWater = level.getFluidState(pos).getType().isSame(ChemicaFluids.DISTILLED_WATER.get()) ||
                        level.getFluidState(pos.above()).getType().isSame(ChemicaFluids.DISTILLED_WATER.get());

                boolean causesExplosion = isInDistilledWater || isInWater;
                if (causesExplosion) {

                    double x = itemEntity.getX();
                    double y = itemEntity.getY();
                    double z = itemEntity.getZ();
                    int count = stack.getCount();

                    itemEntity.discard();

                    BlockPos.betweenClosedStream(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))
                            .forEach(clearPos -> {
                                var fluidState = level.getFluidState(clearPos);
                                if (fluidState.is(FluidTags.WATER)) {
                                    level.setBlock(clearPos, Blocks.AIR.defaultBlockState(), 2);
                                }

                                // 2. 清除蒸餾水（isSame 會同時匹配該流體的水源與流動狀態）
                                if (fluidState.getType().isSame(ChemicaFluids.DISTILLED_WATER.get())) {
                                    level.setBlock(clearPos, Blocks.AIR.defaultBlockState(), 2);
                                }
                            });

                    // 根據鈉錠的數量決定爆炸威力（基礎 3.0f，每多一個增加 0.1f，上限 7.0f）
                    float explosionPower = 4.0f + (count * 0.1f);
                    explosionPower = Math.min(explosionPower, 10.0f);

                    // 4. 觸發真實的化學爆炸
                    level.explode(
                            null,                          // 造成爆炸的實體（null 代表環境/化學爆炸）
                            null,
                            null,
                            x, y, z,
                            explosionPower,                // 爆炸半徑
                            true,
                            Level.ExplosionInteraction.TNT // 破壞方塊模式 (BLOCK/TNT/NONE)
                    );

                    if (level instanceof ServerLevel serverLevel) {

                        serverLevel.sendParticles(ParticleTypes.LAVA, x, y + 0.5, z, 30, 0.5, 0.5, 0.5, 0.2);
                        serverLevel.sendParticles(ParticleTypes.EXPLOSION, x, y + 0.5, z, 5, 1.0, 1.0, 1.0, 0.0);

                        // 巡檢爆炸中心周圍，將水面上的空氣方塊強行換成火方塊
                        BlockPos.betweenClosedStream(pos.offset(-2, -1, -2), pos.offset(2, 2, 2))
                                .forEach(aroundPos -> {
                                    // 如果該位置是空氣，且下方是固體方塊或水面，就點火
                                    if (level.getBlockState(aroundPos).isAir() &&
                                            (!level.getFluidState(aroundPos.below()).isEmpty() || level.getBlockState(aroundPos.below()).isSolid())) {
                                        level.setBlockAndUpdate(aroundPos, BaseFireBlock.getState(level, aroundPos));
                                    }
                                });
                    }
                }
            }

        }
    }
}
