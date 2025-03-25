package ru.levelup.client.api.module.setting.impl;

import net.minecraft.util.math.MathHelper;
import ru.levelup.client.api.module.setting.Setting;

public class FloatSetting extends Setting {

    public float current, minimum, maximum, increment;
    public float sliderWidth;
    public boolean sliding;

    public FloatSetting(String name, float current, float minimum, float maximum, float increment) {
        this.name = name;
        this.minimum = minimum;
        this.current = current;
        this.maximum = maximum;
        this.increment = increment;
    }

    public float get() {
        return current;
    }
}
