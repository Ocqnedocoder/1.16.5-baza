package ru.levelup.client.protect;

import ru.levelup.client.protect.modules.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProtectRegister {

    private List<ProtectModule> protectList;
    public boolean screenCheck;

    public ProtectRegister() {
        //protectList = Arrays.asList(new OpenScreenBlocker().enable(), new StartRegisterModule().enable(),
        //        new PostInitializeModule().enable(), new ServerConnectionBlocker().enable());
        //new FirstScreenCheck().enable();
    }

    public ProtectModule get(Class<?> clas) {
        Iterator<ProtectModule> iteratorMod = protectList.iterator();
        ProtectModule module;
        do {
            if (!iteratorMod.hasNext()) {
                return null;
            }
            module = (ProtectModule) iteratorMod.next();
        } while (module.getClass() != clas);
        return module;
    }

    public List<ProtectModule> getProtectList() {
        return protectList;
    }

    public void disableList() {
        getProtectList().forEach(protectModule -> protectModule.disable());
    }

    public void enableList() {
        getProtectList().forEach(protectModule -> protectModule.enable());
    }

}
