package ru.levelup.client.api.event.impl;

import ru.levelup.client.api.event.Event;

public class MouseInputEvent extends Event {

    private int button;

    public MouseInputEvent(int button1) {
        this.button = button1;
    }

    public int getButton() {
        return button;
    }

}
