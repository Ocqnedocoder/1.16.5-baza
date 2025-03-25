package ru.levelup.client.api.event.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.levelup.client.api.event.Event;

public class EventScreen extends Event {

    public MatrixStack ms;

    public EventScreen(MatrixStack matrixStack){
        this.ms = matrixStack;
    }

    public MatrixStack getMs() {
        return ms;
    }
}
