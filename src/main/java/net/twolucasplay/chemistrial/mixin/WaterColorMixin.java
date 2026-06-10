package net.twolucasplay.chemistrial.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.neoforged.neoforge.client.ClientNeoForgeMod$1") // 精準鎖定 NeoForge 註冊的原版水 Extension 匿名內部類別
public class WaterColorMixin {

    @Inject(method = "getTintColor(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I",
            at = @At("RETURN"), // 在原本計算完生態系顏色後攔截
            cancellable = true,
            remap = false)
    private void adjustWaterTransparency(FluidState state, BlockAndTintGetter getter, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        int originalColor = cir.getReturnValue();

        // 提取原先的 RGB，忽略原版的高透明度 (Alpha)
        int rgb = 0xFFFFFF;

        // 【核心設定】：0x1A 代表極低的透明度（約 10% 的顏色），可以看穿水底。
        // 如果想要再透明一點點，可以改成 0x0D（約 5%）；想明顯一點改成 0x26（約 15%）。
        int alpha = 0x30000000;

        // 重新組合顏色並傳回
        cir.setReturnValue(alpha | rgb);
    }


}
