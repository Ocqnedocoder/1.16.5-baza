package ru.levelup.client.api.module.setting.impl;

import ru.levelup.client.api.module.setting.Setting;

public class AimCenterSetting extends Setting{

    public float current1, minimum1, maximum1, increment1;
    public float current2, minimum2, maximum2, increment2;
    public float current3, minimum3, maximum3, increment3;
    public float current4, minimum4, maximum4, increment4;
    public boolean custom;
    public float sliderWidth1;
    public boolean sliding1;
    public float sliderHeight2;
    public boolean sliding2;
    public boolean dragging;

    public AimCenterSetting(String name, boolean custom,
                            float current1, float minimum1, float maximum1,
                                float increment1, float current2, float minimum2,
                                    float maximum2, float increment2, float current3,
                                        float minimum3, float maximum3, float increment3,
                                            float current4, float minimum4, float maximum4, float increment4) {
        this.name = name;
        this.custom = custom;
        this.minimum1 = minimum1;
        this.current1 = current1;
        this.maximum1 = maximum1;
        this.increment1 = increment1;

        this.minimum2 = minimum2;
        this.current2 = current2;
        this.maximum2 = maximum2;
        this.increment2 = increment2;

        this.current3 = current3;
        this.minimum3 = minimum3;
        this.maximum3 = maximum3;
        this.increment3 = increment3;

        this.current4 = current4;
        this.minimum4 = minimum4;
        this.maximum4 = maximum4;
        this.increment4 = increment4;

    }

    public float get1() {
        return current1;
    }
    public float get2() {
        return current2;
    }
    public float get3() {
        return current3;
    }
    public float get4() {
        return current4;
    }
}
