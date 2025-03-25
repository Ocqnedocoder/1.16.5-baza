package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.levelup.client.api.module.setting.Setting;

public class Component {

    private float x;
    private float y;
    private float width;
    protected float height;
    private Setting setting;

    public Component(Setting setting1) {
        this.setting = setting1;
    }

    public void renderSetting(MatrixStack matrix, float x1, float y1, float mouseX, float mouseY) {
        this.x = x1;
        this.y = y1;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {

    }


}
