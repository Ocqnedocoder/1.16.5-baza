package ru.levelup.client.api.module.impl.movement;

import org.lwjgl.glfw.GLFW;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;

@ModuleHendler(name = "Sprint", description = "Auto sprinting", c = Module.Category.MOVEMENT)
public class Sprint extends Module {

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setSprinting(false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.player.setSprinting(true);
    }
}
