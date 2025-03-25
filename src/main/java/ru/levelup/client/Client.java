package ru.levelup.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import ru.levelup.client.api.clickgui.clientgui.ClientGui;
import ru.levelup.client.api.clickgui.clientgui.theme.Theme;
import ru.levelup.client.api.clickgui.gui.GuiScreen;
import ru.levelup.client.api.module.ModuleManager;
import ru.levelup.client.api.module.setting.SettingManager;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.render.GifManager;

public class Client {

    private static Client instance = new Client();
    private ModuleManager manager;
    private SettingManager settingManager;
    private Fonts font;
    private Theme theme;
    public GifManager gifManager;

    public Client(){
        instance = this;
        font = new Fonts();
        manager = new ModuleManager();
        theme = Theme.PURPLE_THEME;
        settingManager = new SettingManager();
        gifManager = new GifManager();
        gifManager.init();
    }

    public static Client getInstance() {
        return instance;
    }

    public ModuleManager getManager() {
        return manager;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }
    public GifManager getGifManager() {
        return gifManager;
    }
    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public void keyboard(int action, int key) {
        getManager().getModules().forEach((module) -> {
            if (action == 1 && key == module.getKey() && !(Minecraft.getInstance().currentScreen instanceof ChatScreen)) {
                module.toggled();
            }

            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT){
                Minecraft.getInstance().displayGuiScreen(new GuiScreen());
            }

            if (key == GLFW.GLFW_KEY_INSERT){
                Minecraft.getInstance().displayGuiScreen(new ClientGui());
            }
        });
    }
}
