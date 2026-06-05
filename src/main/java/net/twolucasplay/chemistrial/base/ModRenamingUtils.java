package net.twolucasplay.chemistrial.base;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import net.createmod.catnip.lang.LangBuilder;

public class ModRenamingUtils extends LangBuilder {
    public ModRenamingUtils(String namespace) {
        super(namespace);
    }

    public static LangBuilder resistance(double value) {
        return TFMGLang.text("   R = " +  TFMGTexts.resistance(value)).color(0xc98969);
    }
}
