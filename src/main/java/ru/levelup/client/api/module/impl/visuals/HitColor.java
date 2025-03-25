package ru.levelup.client.api.module.impl.visuals;

import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.FloatSetting;

@ModuleHendler(name = "HitColor", description = "Custom color hit enity", c = Module.Category.VISUALS)
public class HitColor extends Module{

    private FloatSetting red = new FloatSetting("Red", 255, 1, 255, 1);
    private FloatSetting green = new FloatSetting("Green", 255, 1, 255, 1);
    private FloatSetting blue = new FloatSetting("Blue", 255, 1, 255, 1);
    private FloatSetting alpha = new FloatSetting("Alpha", 255, 1, 255, 1);

    public HitColor(){
        addSetting(red, green, blue, alpha);
    }

}
