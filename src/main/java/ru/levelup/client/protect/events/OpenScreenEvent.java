package ru.levelup.client.protect.events;

import net.minecraft.client.gui.screen.Screen;
import ru.levelup.client.api.event.Event;

public class OpenScreenEvent extends Event {

    private Screen screen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

}
