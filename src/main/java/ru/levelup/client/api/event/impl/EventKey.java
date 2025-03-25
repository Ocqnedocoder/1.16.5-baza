package ru.levelup.client.api.event.impl;

import ru.levelup.client.api.event.Event;

public class EventKey extends Event {

    int key;

    public EventKey(int key1){
        this.key = key1;
    }

    public int getKey() {
        return key;
    }
}
