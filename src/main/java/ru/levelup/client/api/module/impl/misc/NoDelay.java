package ru.levelup.client.api.module.impl.misc;

import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;

@ModuleHendler(name = "NoDelay", description = "", c = Module.Category.MISC)
public class NoDelay extends Module {

    @EventHendler
    public void pasd(EventUpdate evt) {
        mc.player.jumpTicks = 0;
        mc.rightClickDelayTimer = 0;
    }

}
