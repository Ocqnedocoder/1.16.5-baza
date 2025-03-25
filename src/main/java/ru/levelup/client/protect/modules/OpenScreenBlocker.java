package ru.levelup.client.protect.modules;

import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.protect.ProtectModule;
import ru.levelup.client.protect.events.OpenScreenEvent;

public class OpenScreenBlocker extends ProtectModule {

    @EventHendler
    public void onOpenScreen(OpenScreenEvent event) {

    }

}