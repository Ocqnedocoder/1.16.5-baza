package ru.levelup.client.api.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import ru.levelup.client.api.utils.misc.StencilUtil;
import ru.levelup.client.api.utils.Utils;
import ru.levelup.client.api.utils.render.shaders.ShaderUtil;

import java.awt.*;
import java.nio.FloatBuffer;

import static com.mojang.blaze3d.platform.GlStateManager.blendFunc;
import static com.mojang.blaze3d.systems.RenderSystem.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class DrawHelper extends Utils {

    public static ShaderUtil blurShader = new ShaderUtil("Gausin");
    public static ShaderUtil Glass = new ShaderUtil("glass");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);
    public static ShaderUtil roundedShader = new ShaderUtil("roundRectTexture");
    public static ShaderUtil roundedOutlineShader = new ShaderUtil("roundRectOutline");
    private static ShaderUtil roundedGradientShader = new ShaderUtil("roundedRectGradient");
    private static ShaderUtil roundedCustom = new ShaderUtil("CustomRounded");

    public static void roundedRectangle(final double x, final double y, final double width, final double height, final double ltRadius, final double lbRadius, final double rtRadius, final double rbRadius, final int color) {
        drawRoundedRectCustom((float) x, (float) y, (float) width, (float) height, (float) ltRadius, (float) lbRadius, (float) rtRadius, (float) rbRadius, color);
    }

    public static void drawRoundedRectCustom(final float x, final float y, final float width, final float height, final float ltRadius, final float lbRadius, final float rtRadius, final float rbRadius, final int color) {
        float min = Math.min(width, height);
        float[] c = ColorUtil.getColorComps(new Color(color, true));
        RenderSystem.disableTexture();
        enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedCustom.init();
        roundedCustom.setUniformf("size", (float)width, (float)height);
        roundedCustom.setUniformf("cornerRadius", lbRadius, ltRadius, rbRadius, rtRadius);
        roundedCustom.setUniformf("color", c[0], c[1], c[2], c[3]);
        GlStateManager.enableBlend();
        blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        ShaderUtil.drawQuads(x, y, width, height);
        GlStateManager.disableBlend();
        roundedCustom.unload();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        GlStateManager.color4f(1, 1, 1, 1);
    }

    public static void drawRound( float x, float y, float width, float height, float radius, int color) {
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        blendFunc(770, 771);
        roundedShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", 0);
        roundedShader.setUniformf("color", ColorUtil.r(color), ColorUtil.g(color), ColorUtil.b(color), ColorUtil.a(color));
        ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        RSColor(-1);
    }

    public static void renderGlass() {
        GlStateManager.enableBlend();
        GlStateManager.color4f(1, 1, 1, 1);
        framebuffer = ShaderUtil.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        Glass.init();
        setupUniforms();
        GlStateManager.bindTexture(Minecraft.getInstance().getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        Glass.unload();
        Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
        Glass.init();
        setupUniforms();
        GlStateManager.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        Glass.unload();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.bindTexture(0);
    }

    public static void setupUniforms() {
        Glass.setUniformi("textureIn", 0);
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        blendFunc(770, 771);
        roundedGradientShader.load();
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        roundedGradientShader.setUniformf("color1", ColorUtil.r(bottomLeft), ColorUtil.g(bottomLeft) , ColorUtil.b(bottomLeft) , ColorUtil.a(bottomLeft) );
        roundedGradientShader.setUniformf("color2", ColorUtil.r(topLeft), ColorUtil.g(topLeft), ColorUtil.b(topLeft), ColorUtil.a(topLeft));
        roundedGradientShader.setUniformf("color3", ColorUtil.r(bottomRight), ColorUtil.g(bottomRight), ColorUtil.b(bottomRight) , ColorUtil.a(bottomRight) );
        roundedGradientShader.setUniformf("color4", ColorUtil.r(topRight), ColorUtil.g(topRight), ColorUtil.b(topRight), ColorUtil.a(topRight));
        ShaderUtil.drawQuads(x, y, width, height);
        roundedGradientShader.unload();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
        RSColor(-1);
    }

    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float outlineThickness, Color color, int outlineColor) {
        GlStateManager.enableBlend();
        blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedOutlineShader.load();
        MainWindow sr = Minecraft.getInstance().getMainWindow();
        Color c = new Color(outlineColor);
        ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, roundedOutlineShader);
        roundedOutlineShader.setUniformf("outlineThickness", (float) (outlineThickness * sr.getGuiScaleFactor()));
        roundedOutlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        roundedOutlineShader.setUniformf("outlineColor", c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        ShaderUtil.drawQuads(x - (2 + outlineThickness), y - (2 + outlineThickness), width + (4 + outlineThickness * 2), height + (4 + outlineThickness * 2));
        roundedOutlineShader.unload();
        GlStateManager.disableBlend();
    }

    public static void enableBloom() {
        enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void disableBloom() {
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void drawBlur(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void drawBlur3D(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void drawGlass(Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        renderGlass();
        StencilUtil.uninitStencilBuffer();
    }

    public static void renderBlur(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color4f(1, 1, 1, 255);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        framebuffer = ShaderUtil.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);
        ShaderUtil.bindTexture(Minecraft.getInstance().getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();
        Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);
        ShaderUtil.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        blurShader.unload();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.bindTexture(0);
    }

    public static void renderBlur3D(float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color4f(1, 1, 1, 1);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        framebuffer = ShaderUtil.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);
        ShaderUtil.draw3DQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();
        Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);
        ShaderUtil.draw3DQuads();
        blurShader.unload();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.bindTexture(0);
    }

    public static void RSColor(final int n) {
        RenderSystem.color4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }

    public static void StartScissor( float x,float y, float width, float height) {
        Minecraft mc = Minecraft.getInstance();
        final double scale = mc.getMainWindow().getGuiScaleFactor();
        double finalHeight = height * scale;
        double finalY = (mc.getMainWindow().getScaledHeight() - y) * scale;
        double finalX = x * scale;
        double finalWidth = width * scale;
        RenderSystem.enableScissor((int) finalX, (int) (finalY - finalHeight), (int) finalWidth, (int) finalHeight);
    }

    public static void stopScissor(){
        RenderSystem.disableScissor();
    }

    public static void scale(float f, float f2, double d, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef(f, f2, 0.0f);
        GL11.glScaled(d, d, 1.0);
        GL11.glTranslatef(-f, -f2, 0.0f);
        runnable.run();
        GL11.glPopMatrix();
    }

    public static void scale(float f, float f2, float f3, float f4, float f5, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef(f + f3 / 2.0f, f2 + f4 / 2.0f, 0.0f);
        GL11.glScalef(f5, f5, 1.0f);
        GL11.glTranslatef(-(f + f3 / 2.0f), -(f2 + f4 / 2.0f), 0.0f);
        runnable.run();
        GL11.glPopMatrix();
    }

    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0F / (float) Minecraft.getInstance().getMainWindow().getScaledWidth(), 1.0F / (float) Minecraft.getInstance().getMainWindow().getScaledHeight());
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(ShaderUtil.calculateGaussianValue(i, radius / 2));
        }
        weightBuffer.rewind();
        RenderSystem.glUniform1(blurShader.getUniform("weights"), weightBuffer);
    }

    public static void drawCircle(float x, float y, float radius) {
        int segments = 100;
        double angleIncrement = (2 * Math.PI) / segments;

        for (int i = 0; i < segments; i++) {
            double angle = i * angleIncrement;
            double newX = x + radius * Math.cos(angle);
            double newY = y + radius * Math.sin(angle);

            DrawHelper.drawRound((float) (newX - 1), (float) (newY - 1), 2, 2, 1, new Color(255, 0, 0, 255).getRGB());
        }
    }
    public static void drawCircle(float x, float y, float start, float end, float radius, float width, boolean filled, int color) {

        float i;
        float endOffset;
        if (start > end) {
            endOffset = end;
            end = start;
            start = endOffset;
        }

        GlStateManager.enableBlend();
        GL11.glDisable(GL_TEXTURE_2D);
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (i = end; i >= start; i--) {
            int Color = color;
            float cos = (float) (MathHelper.cos((float) (i * Math.PI / 180)) * radius);
            float sin = (float) (MathHelper.sin((float) (i * Math.PI / 180)) * radius);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        if (filled) {
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            for (i = end; i >= start; i--) {
                int Color = color;
                float cos = (float) MathHelper.cos((float) (i * Math.PI / 180)) * radius;
                float sin = (float) MathHelper.sin((float) (i * Math.PI / 180)) * radius;
                GL11.glVertex2f(x + cos, y + sin);
            }
            GL11.glEnd();
        }

        GL11.glEnable(GL_TEXTURE_2D);
        GlStateManager.disableBlend();
    }
}
