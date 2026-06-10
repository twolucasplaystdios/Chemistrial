package net.twolucasplay.chemistrial;

import com.cosmads.chemica.registry.ChemicaFluids;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.twolucasplay.chemistrial.base.ModRegistrate;
import net.twolucasplay.chemistrial.blocks.ModBlocks;
import net.twolucasplay.chemistrial.tabs.ModCreativeModeTabs;
import net.twolucasplay.chemistrial.item.ModItems;

import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Chemistrial.MODID)
public class Chemistrial {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "chemistrial";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ModRegistrate REGISTRATE = ModRegistrate.create(MODID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Chemistrial(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register();
        REGISTRATE.registerEventListeners(modEventBus);

        ModBlocks.register();
        ModItems.register();

//        modEventBus.addListener(EventPriority.HIGHEST, ModDatagen::gatherDataHighPriority);
//        modEventBus.addListener(EventPriority.LOWEST, ModDatagen::gatherData);
    //        modEventBus.addListener(ModCreativeModeTabs::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        AtomicReference<Float> DURABILITY_SCALE = new AtomicReference<>(6.75f);
        event.enqueueWork(() -> {
            for (Block b : BuiltInRegistries.BLOCK){
                float originalResistance = b.getExplosionResistance();
                if (originalResistance >= 1000.0f || originalResistance <= 0.0f) continue;

                if (b == Blocks.BEDROCK) continue;

                String blockId = BuiltInRegistries.BLOCK.getKey(b).getPath();
                float finalScale = DURABILITY_SCALE.get();

                if (blockId.contains("iron")) {
                    finalScale *= 8.0f;
                } else if (blockId.contains("brass")) {
                    finalScale *= 4.5f;
                } else if (blockId.contains("netherite")) {
                    finalScale *= 1.5f;
                } else if (blockId.contains("gold")) {
                    finalScale *= 0.8f;
                } else if (blockId.contains("zinc")) {
                    finalScale *= 0.6f;
                } else if (blockId.contains("copper")) {
                    finalScale *= 1.2f;
                } else if (blockId.contains("diamond")) {
                    finalScale *= 0.2f;
                } else if (blockId.contains("aluminum") || blockId.contains("bauxite")) {
                    finalScale = 1.8f;
                } else if (blockId.contains("andesite_alloy")) {
                    finalScale *= 0.5f;
                } else {
                    finalScale *= 1.0f;
                }

                // 計算新抗性並直接覆蓋
                float newResistance = originalResistance * finalScale;
                overrideBlastResistance(b, newResistance);

            }

        });
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO");
    }
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // 核心：將原生的鐵錠 (Items.IRON_INGOT) 強行關聯至 Create 的說明網格
                // 這會直接激活 Create 內部的語言鍵搜尋機制！
                TooltipModifier.REGISTRY.register(Items.IRON_INGOT,
                        new ItemDescription.Modifier(Items.IRON_INGOT, FontHelper.Palette.STANDARD_CREATE)
                );

            });
        }
    }

    private void overrideBlastResistance(net.minecraft.world.level.block.Block block, float newResistance) {
        try {
            // 透過反射獲取方塊的 properties 欄位並修改 explosionResistance
            // 注意：在不同版本的對映表（Mappings）中，欄位名稱可能叫 "explosionResistance" 或 "f_60441_"
            Field propertiesField = BlockBehaviour.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            BlockBehaviour.Properties properties = (BlockBehaviour.Properties) propertiesField.get(block);

            // 重新設定該方塊的爆炸抗性
            properties.explosionResistance(newResistance);

            // 同時需要把方塊本身的 explosionResistance 欄位也更新（部分版本直接讀取此欄位）
            Field resistanceField = BlockBehaviour.class.getDeclaredField("explosionResistance");
            resistanceField.setAccessible(true);
            resistanceField.setFloat(block, newResistance);

        } catch (Exception e) {
            LOGGER.warn("Blocks Resistance couldn't change!");
        }
    }

}
