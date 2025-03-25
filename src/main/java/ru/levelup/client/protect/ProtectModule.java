package ru.levelup.client.protect;

import ru.levelup.client.api.event.EventManager;

public class ProtectModule {

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public ProtectModule enable() {
        EventManager.register(this);
        enabled = true;
        return this;
    }

    public ProtectModule disable() {
        EventManager.unregister(this);
        enabled = false;
        return this;
    }
}
