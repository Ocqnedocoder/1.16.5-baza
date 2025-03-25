package ru.levelup.client.protect.modules;

import ru.levelup.client.api.clickgui.gui.GuiScreen;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.menu.MenuScreen;
import ru.levelup.client.api.utils.misc.WebUtils;
import ru.levelup.client.protect.Crasher;
import ru.levelup.client.protect.ProtectModule;
import ru.levelup.client.protect.UserData;
import ru.levelup.client.protect.events.OpenScreenEvent;

public class FirstScreenCheck extends ProtectModule {

    @EventHendler
    public void onOpenScreen(OpenScreenEvent event) {
        if (event.getScreen() instanceof GuiScreen && event.getScreen() instanceof MenuScreen){
            return;
        }
        UserData userData = new UserData();
        if (WebUtils.getFormattedHWID().equals(userData.hwid) && userData.name != null
                && userData.hwid != null && !userData.role.equals("User")) {
            disable();
        } else {
            Crasher.crash();
        }
    }

}