package net.twolucasplay.chemistrial.base;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModRegistrate extends CreateRegistrate {
    protected ModRegistrate(String modid) {
        super(modid);
    }

    public ModRegistrate setTooltipModifierFactory(@Nullable Function<Item, TooltipModifier> factory) {
        currentTooltipModifierFactory = factory;
        return this;
    }
    public static ModRegistrate create(String id) {
        return new ModRegistrate(id);
    }
}
