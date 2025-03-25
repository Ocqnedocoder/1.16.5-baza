package ru.levelup.client.api.utils.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ColorUtil {
    public static int swapAlpha(final int n, final float n2) {
        return ColorUtil.toRGBA(n >> 16 & 0xFF, n >> 8 & 0xFF, n & 0xFF, (int)n2);
    }

    public static Color swapAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static float[] getColorComps(Color color) {
        return new float[]{color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f};
    }

    public static int getColor(int color, int alpha) {
        return new Color(color, color, color, alpha).getRGB();
    }

    public static int gradient(int speed, int index, int... colors) {
        int angle = (int) ((System.currentTimeMillis() / (long) speed + index) % 360);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int) (angle / 360f * colors.length);
        if (colorIndex == colors.length) {
            colorIndex--;
        }
        int color1 = colors[colorIndex];
        int color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        float amount = (angle / 360f * colors.length - colorIndex);
        amount = Math.min(1, Math.max(0, amount));
        int red1 = color1 >> 16 & 255;
        int green1 = color1 >> 8 & 255;
        int blue1 = color1 & 255;
        int alpha1 = color1 >> 24 & 255;
        int red2 = color2 >> 16 & 255;
        int green2 = color2 >> 8 & 255;
        int blue2 = color2 & 255;
        int alpha2 = color2 >> 24 & 255;
        int interpolatedRed = (int) (red1 + (red2 - red1) * amount);
        int interpolatedGreen = (int) (green1 + (green2 - green1) * amount);
        int interpolatedBlue = (int) (blue1 + (blue2 - blue1) * amount);
        int interpolatedAlpha = (int) (alpha1 + (alpha2 - alpha1) * amount);
        return (interpolatedAlpha << 24) | (interpolatedRed << 16) | (interpolatedGreen << 8) | interpolatedBlue;
    }


    public static float r(int color){
        return (float)(color >> 16 & 255) / 255.0F;
    }
    public static float g(int color){
        return (float)(color >> 8 & 255) / 255.0F;
    }
    public static float b(int color){
        return (float)(color & 255) / 255.0F;
    }
    public static float a(int color){
        return (float)(color >> 24 & 255) / 255.0F;
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static void setColor(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        setColor(r, g, b, alpha);
    }
    public static void setColor(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b << 0) + (a << 24);
    }

}