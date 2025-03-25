package ru.levelup.client.api.module.setting;

import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.setting.impl.AimCenterSetting;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.module.setting.impl.ModeSetting;

public class SettingManager {

    public FloatSetting getFloatSetting(Module module, String name){
        for (Setting setting : module.getSettings()){
            if (setting instanceof FloatSetting floatSetting){
                if (floatSetting.getName().equals(name)){
                    return floatSetting;
                }
            }
        }
        return null;
    }

    public BooleanSetting getBooleanSetting(Module module, String name){
        for (Setting setting : module.getSettings()){
            if (setting instanceof BooleanSetting booleanSetting){
                if (booleanSetting.getName().equals(name)){
                    return booleanSetting;
                }
            }
        }
        return null;
    }

    public ModeSetting getModeSettings(Module module, String name){
        for (Setting setting : module.getSettings()){
            if (setting instanceof ModeSetting modeSetting){
                if (modeSetting.getName().equals(name)){
                    return modeSetting;
                }
            }
        }
        return null;
    }

    public AimCenterSetting getAimSetting(Module module, String name){
        for (Setting setting : module.getSettings()){
            if (setting instanceof AimCenterSetting aimCenterSetting){
                if (aimCenterSetting.getName().equals(name)){
                    return aimCenterSetting;
                }
            }
        }
        return null;
    }

}
