package ru.levelup.client.api.clickgui.clientgui.theme;

import ru.levelup.client.api.utils.render.ColorUtil;

public enum Theme {

    //LIGHT_THEME(Type.CLIENT, ColorUtil.getColor(255, 255, 255), ColorUtil.getColor(221, 221, 221), ColorUtil.getColor(170, 170, 170), ColorUtil.getColor(68, 68, 68)),
    //DARK_THEME(Type.CLIENT, ColorUtil.getColor(51, 51, 51), ColorUtil.getColor(85, 85, 85), ColorUtil.getColor(119, 119, 119), ColorUtil.getColor(153, 153, 153)),
    RED_THEME(ColorUtil.getColor(255, 85, 85), ColorUtil.getColor(119, 0, 0)),
    PURPLE_THEME(ColorUtil.getColor(153, 51, 255), ColorUtil.getColor(119, 0, 170)),
    GOLD_THEME(ColorUtil.getColor(255, 204, 102), ColorUtil.getColor(153, 115, 0)),
    LAPIS_THEME(ColorUtil.getColor(0, 102, 204), ColorUtil.getColor(102, 204, 255)),
    GREEN_THEME(ColorUtil.getColor(51, 204, 51), ColorUtil.getColor(0, 153, 0));

    private int[] colors;

    Theme(int... colors){
        this.colors = colors;
    }

    public int getColor(int index) {
        return ColorUtil.gradient(5, index, colors);
    }

    public int[] getColors() {
        return colors;
    }
}
