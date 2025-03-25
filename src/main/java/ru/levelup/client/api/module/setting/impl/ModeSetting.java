package ru.levelup.client.api.module.setting.impl;

import ru.levelup.client.api.module.setting.Setting;

public class ModeSetting<T extends Enum<T>> extends Setting {

    public enum MODESS {
        CYCLE,
        CUSTOM
    }

    private final T[] modes;
    private T currentMode;
    private int index;
    private final MODESS modeType;

    public ModeSetting(String name, T initialMode, Class<T> enumClass, MODESS modeType) {
        this.name = name;
        this.modes = enumClass.getEnumConstants();
        if (!containsMode(initialMode)) {
            throw new IllegalArgumentException("Initial mode is not in the list of options");
        }
        this.currentMode = initialMode;
        this.modeType = modeType;
    }

    public T get() {
        return currentMode;
    }

    public boolean is(T mode) {
        return currentMode == mode;
    }

    public void cycle() {
        if (modeType == MODESS.CYCLE) {
            int nextIndex = (indexOfMode(currentMode) + 1) % modes.length;
            currentMode = modes[nextIndex];
        }
    }

    public void setMode(T newMode) {
        if (!containsMode(newMode)) {
            throw new IllegalArgumentException("Mode is not in the list of options");
        }
        this.currentMode = newMode;
    }

    private boolean containsMode(T mode) {
        for (T availableMode : modes) {
            if (availableMode == mode) {
                return true;
            }
        }
        return false;
    }

    private int indexOfMode(T mode) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i] == mode) {
                return i;
            }
        }
        return -1;
    }

    public MODESS getModeType() {
        return modeType;
    }
}
