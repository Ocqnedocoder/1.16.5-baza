package ru.levelup.client.api.event.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import ru.levelup.client.api.event.Event;

public class EventWorldRenderer extends Event {

    public MatrixStack ms;
    public float pt;

    private ActiveRenderInfo activeRenderInfo;

    public EventWorldRenderer(MatrixStack ms1, float pt1, ActiveRenderInfo activeRenderInfo){
        this.ms = ms1;
        this.pt = pt1;
        this.activeRenderInfo = activeRenderInfo;
    }

    public ActiveRenderInfo getActiveRenderInfo() {
        return activeRenderInfo;
    }

    public MatrixStack getMs() {
        return ms;
    }

    public float getPt() {
        return pt;
    }
}
