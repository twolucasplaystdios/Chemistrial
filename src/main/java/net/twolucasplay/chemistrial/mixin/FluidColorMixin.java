package net.twolucasplay.chemistrial.mixin;

import com.cosmads.chemica.Chemica;
import com.cosmads.chemica.registry.ChemicaBlocks;
import com.cosmads.chemica.registry.ChemicaFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.cosmads.chemica.common.TransparentTintedFluidType")
public class FluidColorMixin {

    @Inject(method = "getTintColor(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I",
            at = @At("RETURN"), // 在原本計算完生態系顏色後攔截
            cancellable = true,
            remap = false)
    private void adjustFluidTransparency(FluidState state, BlockAndTintGetter getter, BlockPos pos, CallbackInfoReturnable<Integer> cir) {

        var b = state.getFluidType();

        if (b == ChemicaFluids.DISTILLED_WATER.getType()){
            int rgb = 0xFFFFFF;

            // 【核心設定】：0x1A 代表極低的透明度（約 10% 的顏色），可以看穿水底。
            // 如果想要再透明一點點，可以改成 0x0D（約 5%）；想明顯一點改成 0x26（約 15%）。
            int alpha = 0x30000000;

            // 重新組合顏色並傳回
            cir.setReturnValue(alpha | rgb);
            return;
        } else {
            int originalColor = cir.getReturnValue();

            // 【核心設定】：0x1A 代表極低的透明度（約 10% 的顏色），可以看穿水底。
            // 如果想要再透明一點點，可以改成 0x0D（約 5%）；想明顯一點改成 0x26（約 15%）。
            int alpha = 0x30000000;

            // 重新組合顏色並傳回
            cir.setReturnValue(alpha | originalColor);
        }

    }


}
