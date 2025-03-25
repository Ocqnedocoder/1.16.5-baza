package ru.levelup.client.api.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil {
    public static Minecraft mc = Minecraft.getInstance();
    public static MainWindow mw = mc.getMainWindow();
    public static void start() {
        GlStateManager.clearCurrentColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture();
        GlStateManager.disableCull();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableDepthTest();
    }

    public static void stop() {
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableCull();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        GlStateManager.clearCurrentColor();
    }
    public static Color setAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }



}
