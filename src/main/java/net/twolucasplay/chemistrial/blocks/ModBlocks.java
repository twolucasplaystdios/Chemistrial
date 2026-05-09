package net.twolucasplay.chemistrial.blocks;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.twolucasplay.chemistrial.Chemistrial;

import static net.twolucasplay.chemistrial.Chemistrial.REGISTRATE;

public class ModBlocks {


    private static TagKey<Block> createCommonBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> createCommonItemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static void register() {
        Chemistrial.LOGGER.info("Registering blocks for {}", "Chemistrial");
    }

    private static final ResourceKey<CreativeModeTab> CHEMICALS_ITEMS_TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Chemistrial.asResource("chemicals"));

    public static final BlockEntry<Block> CINNABAR = REGISTRATE
            .block("cinnabar", Block::new).properties((p) -> p.strength(2.0f))
            .simpleItem().register();

}
