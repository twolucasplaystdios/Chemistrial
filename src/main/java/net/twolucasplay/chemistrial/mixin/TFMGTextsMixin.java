package net.twolucasplay.chemistrial.mixin;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import net.createmod.catnip.lang.LangBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "com.drmangotea.tfmg.base.lang.TFMGTexts$Multimeter", remap = false) // 直接指定目標類別
public class TFMGTextsMixin {
    /**
     * @author twolucasplay
     * @reason 修改電阻顯示格式與顏色
     */
    @Overwrite

    public static LangBuilder resistance(double value) {
        // 在這裡寫入你想修改的邏輯
        return TFMGLang.text("   R = " + value + " Ω").color(0xc98969);
    }
}
