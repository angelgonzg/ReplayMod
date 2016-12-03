package com.replaymod.recording;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.utils.Restrictions;
import com.replaymod.recording.handler.ConnectionEventHandler;
import com.replaymod.recording.packet.PacketListener;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = ReplayModRecording.MOD_ID, useMetadata = true)
public class ReplayModRecording {
    public static final String MOD_ID = "replaymod-recording";

    @Mod.Instance(MOD_ID)
    public static ReplayModRecording instance;

    @Mod.Instance(ReplayMod.MOD_ID)
    private static ReplayMod core;

    private Logger logger;

    private ConnectionEventHandler connectionEventHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        core.getSettingsRegistry().register(Setting.class);

        core.getKeyBindingRegistry().registerKeyBinding("replaymod.input.marker", Keyboard.KEY_M, new Runnable() {
            @Override
            public void run() {
                PacketListener packetListener = connectionEventHandler.getPacketListener();
                if (packetListener != null) {
                    packetListener.addMarker();
                    core.printInfoToChat("replaymod.chat.addedmarker");
                }
            }
        });
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EventBus bus = MinecraftForge.EVENT_BUS;
        bus.register(connectionEventHandler = new ConnectionEventHandler(logger, core));

        NetworkRegistry.INSTANCE.newSimpleChannel(Restrictions.PLUGIN_CHANNEL);
    }

    public void initiateRecording(NetworkManager networkManager) {
        connectionEventHandler.onConnectedToServerEvent(networkManager);
    }
}
