package net.twolucasplay.chemistrial.tabs;

import com.cosmads.chemica.registry.ChemicaItems;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.twolucasplay.chemistrial.item.ModItems;
import org.slf4j.Logger;



import static net.twolucasplay.chemistrial.Chemistrial.MODID;
import static net.twolucasplay.chemistrial.Chemistrial.REGISTRATE;

public class ModCreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static final Logger LOGGER = LogUtils.getLogger();


    public static void register() {
        REGISTRATE.defaultCreativeTab("chemicals", builder -> builder
                .title(Component.translatable("creativetab.chemistrialmod.chemicals"))
                .icon(() -> new ItemStack(ModItems.MERCURY_II_SULFIDE.get()))
                .displayItems((params, output) -> {

                })
        ).register();

        LOGGER.info("Registered creative tab for {}", MODID);
    }
}
