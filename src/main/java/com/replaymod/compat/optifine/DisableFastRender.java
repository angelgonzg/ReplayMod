package com.replaymod.compat.optifine;

import com.replaymod.render.events.ReplayRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisableFastRender {

    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean wasFastRender = false;

    @SubscribeEvent
    public void onRenderBegin(ReplayRenderEvent.Pre event) {
        if (!FMLClientHandler.instance().hasOptifine()) return;

        try {
            wasFastRender = (boolean) OptifineReflection.gameSettings_ofFastRender.get(mc.gameSettings);
            OptifineReflection.gameSettings_ofFastRender.set(mc.gameSettings, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderEnd(ReplayRenderEvent.Post event){
        if (!FMLClientHandler.instance().hasOptifine()) return;

        try {
            OptifineReflection.gameSettings_ofFastRender.set(mc.gameSettings, wasFastRender);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
