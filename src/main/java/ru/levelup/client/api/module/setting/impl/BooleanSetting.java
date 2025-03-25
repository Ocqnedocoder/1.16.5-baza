package ru.levelup.client.api.module.setting.impl;

import ru.levelup.client.api.module.setting.Setting;

public class BooleanSetting extends Setting {

    public enum ToggleState {
        ON,
        OFF
    }

    private ToggleState toggleState;

    public BooleanSetting(String name, ToggleState initialState) {
        this.name = name;
        this.toggleState = initialState;
    }

    public ToggleState get() {
        return toggleState;
    }

    public void toggle() {
        toggleState = (toggleState == ToggleState.ON) ? ToggleState.OFF : ToggleState.ON;
    }

}
