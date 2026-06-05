package net.twolucasplay.chemistrial.event;

import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.twolucasplay.chemistrial.Chemistrial;

@EventBusSubscriber(modid = Chemistrial.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClearWaterFogEvents {
    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (event.getCamera().getFluidInCamera() == FogType.WATER) {

            event.setNearPlaneDistance(5000.0F);
            event.setFarPlaneDistance(10000.0F);

            // 必須呼叫 cancel 才會強制覆蓋 vanilla 預設的水下霧氣距離
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        if (event.getCamera().getFluidInCamera() == FogType.WATER) {
            // 這裡可以選擇將霧氣顏色跟隨天空或直接歸零
            // 配合上面推遠的距離，直接設為 0 可以確保水底背景最乾淨、最通透
            event.setRed(0.0F);
            event.setGreen(0.0F);
            event.setBlue(0.0F);
        }
    }
}
