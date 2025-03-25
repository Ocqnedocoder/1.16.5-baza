package ru.levelup.client.api.event.impl;

import net.minecraft.network.IPacket;
import ru.levelup.client.api.event.Event;

public class EventPacket extends Event {

    private IPacket packet;

    public EventPacket(IPacket packet) {
        this.packet = packet;
    }

    public IPacket getPacket() {
        return packet;
    }

    public void setPacket(IPacket packet) {
        this.packet = packet;
    }
}
