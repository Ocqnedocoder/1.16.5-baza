package ru.levelup.client.protect.modules;

import ru.levelup.client.Client;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.utils.misc.WebUtils;
import ru.levelup.client.protect.AntiAgent;
import ru.levelup.client.protect.Crasher;
import ru.levelup.client.protect.ProtectModule;
import ru.levelup.client.protect.UserData;
import ru.levelup.client.protect.events.StartRegisterEvent;

public class StartRegisterModule extends ProtectModule {

    @EventHendler
    public void onInit(StartRegisterEvent event) {
        UserData userData = new UserData();
        if ((WebUtils.getFormattedHWID().equals(userData.hwid) && userData.name != null
                && userData.hwid != null && !userData.role.equals("User"))) {
            disable();
        } else {
            Crasher.crash();
        }
        AntiAgent.antiArgs();
        Client client = new Client();
    }

}
