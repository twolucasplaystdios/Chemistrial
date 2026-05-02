package net.twolucasplay.chemistrial.datagen;

import com.drmangotea.tfmg.base.TFMGRegistrateTags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.twolucasplay.chemistrial.Chemistrial;

import static com.drmangotea.tfmg.registry.TFMGItems.REGISTRATE;

public class ModDatagen {

    public static void gatherDataHighPriority(GatherDataEvent event) {
        if (event.getMods().contains(Chemistrial.MODID))
            addExtraRegistrateData();
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ModGeneratedEntriesProvider generatedEntriesProvider = new ModGeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);




    }

    private static void addExtraRegistrateData() {
        TFMGRegistrateTags.addGenerators();

            REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("tooltips", langConsumer);

        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/chemistrial/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

}
