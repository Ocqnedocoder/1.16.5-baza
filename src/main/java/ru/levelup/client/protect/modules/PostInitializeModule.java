package ru.levelup.client.protect.modules;

import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.protect.ProtectModule;
import ru.levelup.client.protect.events.ClientInitializeEvent;

public class PostInitializeModule extends ProtectModule {

    @EventHendler
    public void onClientInitialize(ClientInitializeEvent event) {

    }

}
