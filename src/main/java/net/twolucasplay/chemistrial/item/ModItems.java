package net.twolucasplay.chemistrial.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.twolucasplay.chemistrial.Chemistrial;

import static net.twolucasplay.chemistrial.Chemistrial.REGISTRATE;

public class ModItems {
    private static final ResourceKey<CreativeModeTab> CHEMICALS_ITEMS_TAB =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Chemistrial.asResource("chemicals"));

    private static TagKey<Item> createCommonItemTag(String path) {
        return TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("t", path));
    }


    public static final ItemEntry<Item> URANIUM_INGOT = REGISTRATE.item("uranium_ingot", Item::new)
            .tag(Tags.Items.INGOTS)
            .tag(createCommonItemTag("ingots/uranium"))
            .tab(CHEMICALS_ITEMS_TAB)
            .properties(p -> p.stacksTo(64))
            .register(); // Fix texture

    public static final ItemEntry<Item> POTASSIUM_INGOT = REGISTRATE.item("potassium_ingot", Item::new)
            .tag(Tags.Items.INGOTS)
            .tag(createCommonItemTag("ingots/potassium"))
            .tab(CHEMICALS_ITEMS_TAB)
            .properties(p -> p.stacksTo(64))
            .register();




    public static final ItemEntry<Item> MERCURY_II_SULFIDE = REGISTRATE
            .item("mercury_ii_sulfide", Item::new)
            .tab(CHEMICALS_ITEMS_TAB)
            .properties(p -> p.stacksTo(64))
            .register();

    public static final ItemEntry<Item> GLUCOSE = REGISTRATE
            .item("glucose", Item::new)
            .tab(CHEMICALS_ITEMS_TAB)
            .properties(p -> p.stacksTo(64)
                    .food(new FoodProperties.Builder()
                            .nutrition(6)
                            .saturationModifier(0.6f)
                            .fast()
                            .build()
            ))
            .register();

    public static void register() {
        Chemistrial.LOGGER.info("Registering items for {}", "Chemistrial");
    }
}