package com.replaymod.core.handler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Moves certain buttons on the main menu upwards so we can inject our own.
 */
public class MainMenuHandler {
    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiMainMenu) {
            for (GuiButton button : event.getButtonList()) {
                // Buttons that aren't in a rectangle directly above our space don't need moving
                if (button.xPosition + button.width < event.getGui().width / 2 - 100
                        || button.xPosition > event.getGui().width / 2 + 100
                        || button.yPosition > event.getGui().height / 4 + 10 + 4 * 24) continue;
                // Move button up to make space for two rows of buttons
                // and then move back down by 10 to compensate for the space to the exit button that was already there
                button.yPosition -= 2 * 24 - 10;
            }
        }
    }
}
