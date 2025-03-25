package ru.levelup.client.api.event.impl;

import ru.levelup.client.api.event.Event;

public class EventRotation extends Event {

    private float yaw, pitch;
    private double x,y,z;

    public EventRotation(float yaw1, float pitch1, double x1, double y1, double z1){
        this.yaw = yaw1;
        this.x = x1;
        this.y = y1;
        this.z = z1;
        this.pitch = pitch1;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
