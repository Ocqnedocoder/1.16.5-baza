package ru.levelup.client.api.module;

import net.minecraft.client.Minecraft;
import ru.levelup.client.api.event.EventManager;
import ru.levelup.client.api.module.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {

    public static Minecraft mc = Minecraft.getInstance();
    private String name, description;
    private boolean toggled;
    private Category category;
    private int key;
    private List<Setting> settings = new ArrayList<>();

    public Module() {
        var anotations = getClass().getAnnotation(ModuleHendler.class);
        this.name = anotations.name();
        this.description = anotations.description();
        this.category = anotations.c();
    }

    public boolean addSetting(Setting... setting) {
        return settings.addAll(Arrays.asList(setting));
    }

    public List<Setting> getSettings(){
        return settings;
    }

    public boolean getToggled(){
        return toggled;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void renderShader(){

    }

    public void toggled(){
        toggled = !toggled;
        if (toggled){
            System.out.println("enable");
            onEnable();
        }else{
            onDisable();
        }
    }

    public void onEnable(){
        EventManager.register(this);
    }
    public void onDisable(){EventManager.unregister(this);}

    public enum Category{
        COMBAT,
        MOVEMENT,
        VISUALS,
        PLAYER,
        MISC
    }

}
