package ru.levelup.client.api.module.impl.visuals;

import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.AimCenterSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.module.setting.impl.ModeSetting;

@ModuleHendler(name = "SwingAnimation", description = "Animation your hands", c = Module.Category.VISUALS)
public class SwingAnimations extends Module {

    private AimCenterSetting hands = new AimCenterSetting("Left Hand & Right Hand", false, 1, -3, 3, 0.1f,
            1, -3, 3, 0.1f,
                1, -3, 3, 0.1f,
                    1, -3, 3, 0.1f);
    private FloatSetting size = new FloatSetting("Size Hand", 0.6f, 0.1f, 1.5f, 0.1f);
    private ModeSetting animations = new ModeSetting("Animation", Swings.DEFAULT, Swings.class, ModeSetting.MODESS.CYCLE);

    public SwingAnimations(){
        addSetting(hands, size, animations);
    }

    public enum Swings{
        DEFAULT,
        BLOCK,
        SPIN
    }
}
