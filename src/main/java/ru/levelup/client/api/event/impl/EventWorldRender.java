package ru.levelup.client.api.event.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.levelup.client.api.event.Event;

public class EventWorldRender extends Event {
    private float tickDelta;
    public MatrixStack matrixStack = new MatrixStack();

    public EventWorldRender(float delta) {
        this.tickDelta = delta;

    }

    public void setTickDelta(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public Type type;
    public boolean isRender3D() {
        return this.type == Type.RENDER3D;
    }

    public boolean isRender2D() {
        return this.type == Type.RENDER2D;
    }
    public enum Type {
        RENDER3D, RENDER2D
    }
}
