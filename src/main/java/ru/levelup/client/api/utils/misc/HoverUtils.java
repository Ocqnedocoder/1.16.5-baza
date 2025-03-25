package ru.levelup.client.api.utils.misc;

public class HoverUtils {

    public static boolean isHover(float xx, float yy, float width, float height, double mouseX, double mouseY) {
        if (mouseX > xx && mouseX < width + xx && mouseY > yy && mouseY < yy + height) {
            return true;
        }
        return false;
    }

}
