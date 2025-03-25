package ru.levelup.client.api.module.impl.player;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;

@ModuleHendler(name = "ScreenWalk", description = "", c = Module.Category.PLAYER)
public class ScreenWalk extends Module{

    @EventHendler
    public void onUpda(EventUpdate evt) {
        KeyBinding[] moveKeys = {mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
                mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump,
                mc.gameSettings.keyBindSprint};
        if ((mc.currentScreen != null)
                && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof EditSignScreen)) {
            KeyBinding[] array;
            int length = (array = moveKeys).length;
            for (int i = 0; i < length; i++) {
                KeyBinding key = array[i];
                key.setPressed(InputMappings.isKeyDown(mc.getMainWindow().getHandle(), key.keyCode.getKeyCode()));
            }
        }
    }

}
