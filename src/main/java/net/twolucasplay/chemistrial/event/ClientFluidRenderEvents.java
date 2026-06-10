package net.twolucasplay.chemistrial.event;

import com.cosmads.chemica.registry.ChemicaFluids;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.twolucasplay.chemistrial.Chemistrial;

import java.util.Collection;

@EventBusSubscriber(modid = Chemistrial.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientFluidRenderEvents {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                // 1. 透過反射獲取 ChemicaFluids 類別中的 REGISTRATE 欄位
                java.lang.reflect.Field registrateField = ChemicaFluids.class.getDeclaredField("REGISTRATE");

                // 2. 破解 private 權限限制
                registrateField.setAccessible(true);

                // 3. 取得該欄位在運行時的實例（因為是 static 欄位，傳入 null 即可）
                AbstractRegistrate<?> registrate = (AbstractRegistrate<?>) registrateField.get(null);

                // 4. 將左側接收端改為未知泛型的 Collection<?>，成功對接並包容 2 個泛型參數的 RegistryEntry
                java.util.Collection<?> entries = registrate.getAll(net.minecraft.core.registries.Registries.FLUID);

// 5. 迴圈內直接使用最上層的 Object 接收，徹底避開 RegistryEntry 的型態與參數數量限制
                for (Object element : entries) {
                    if (element instanceof com.tterrag.registrate.util.entry.RegistryEntry<?, ?> fluidHolder) {

                        // 6. 安全取得內容物，並確認是否為 Fluid 實例
                        Object obj = fluidHolder.get();
                        if (obj instanceof net.minecraft.world.level.material.Fluid fluid) {

                            // 7. 將流體（包含源頭和流動狀態）設為半透明 (Translucent)
                            net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(fluid, net.minecraft.client.renderer.RenderType.translucent());
                        }
                    }
                }


            } catch (Exception e) {
                // 萬一 Registrate 內部結構改變導致反射失敗，拋出 log 以便偵錯，且不會導致遊戲直接崩潰
                System.err.println("[Chemistrial] 無法透過反射讀取 REGISTRATE 流體項目: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
