package ru.levelup.client.api.event.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.levelup.client.api.event.Event;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class EventMotion extends Event {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
}
