package ru.levelup.client.protect.modules;

import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.utils.misc.WebUtils;
import ru.levelup.client.protect.Crasher;
import ru.levelup.client.protect.ProtectModule;
import ru.levelup.client.protect.UserData;
import ru.levelup.client.protect.events.ServerConnectionEvent;

public class ServerConnectionBlocker extends ProtectModule {

    @EventHendler
    public void onConnection(ServerConnectionEvent event) {
        UserData userData = new UserData();
        if (WebUtils.getFormattedHWID().equals(userData.hwid) && userData.name != null
                && userData.hwid != null && !userData.role.equals("User")) {
            disable();
        } else {
            Crasher.crash();
        }
    }

}
