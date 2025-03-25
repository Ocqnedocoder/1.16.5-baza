package ru.levelup.client.api.module;

import ru.levelup.client.api.module.impl.combat.*;
import ru.levelup.client.api.module.impl.misc.*;
import ru.levelup.client.api.module.impl.player.*;
import ru.levelup.client.api.module.impl.movement.*;
import ru.levelup.client.api.module.impl.visuals.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = Arrays.asList(new Sprint(),  new Intarface(),new SwingAnimations(), new KillAura(), new HitColor(),
            new AutoPotion(), new AutoTotem(), new Velocity(), new ScreenWalk(), new NoDelay(), new ClickPearl(), new Aura(),new Circles(), new Particles(),
            new Cross());
    public List<Module> getModules() {
        return modules;
    }

    public Module getModule(final String s){
        for (final Module module : this.modules){
            if (module.getName().equalsIgnoreCase(s)){
                return module;
            }
        }
        return null;
    }

    public List getModules(final Module.Category category){
        final List<Module> list = new ArrayList<>();
        for (final Module module : this.modules){
            if (module.getCategory() == category){
                list.add(module);
            }
        }
        return list;
    }

}
