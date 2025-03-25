package ru.levelup.client.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.net.URL;
import java.util.HashMap;

public class ImageUtils {

    public static int anInt = 0;
    static HashMap<String, ResourceLocation> map = new HashMap<>();

    public static ResourceLocation getResourceDinamic(String url) {
        if (map.containsKey(url)) {
            return map.get(url);
        } else {
            try {
                ResourceLocation dynamicTextureLocation = Minecraft.getInstance().getTextureManager().getDynamicTextureLocation("dinamic_texture" + anInt, new DynamicTexture(NativeImage.read(new URL(url).openStream())));
                if (dynamicTextureLocation != null && dynamicTextureLocation.getNamespace() != null) {
                    map.put(url, dynamicTextureLocation);
                    anInt++;
                    return dynamicTextureLocation;
                }
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
            return null;
        }
    }

    public static void drawImage(ResourceLocation image, double x, double y, double width, double height, int color) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        RenderSystem.enableDepthTest();

        if (image != null) {
            minecraft.getTextureManager().bindTexture(image);
        }

        int alpha = color >>> 24;
        int finalColor = (255 << 16) | (255 << 8) | 255 | (alpha << 24);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        bufferBuilder.pos(x, y + height, 0).color(finalColor >> 16 & 0xFF, finalColor >> 8 & 0xFF, finalColor & 0xFF, finalColor >>> 24).tex(0, 1 - 0.01f).lightmap(0, 240).endVertex();
        bufferBuilder.pos(x + width, y + height, 0).color(finalColor >> 16 & 0xFF, finalColor >> 8 & 0xFF, finalColor & 0xFF, finalColor >>> 24).tex(1, 1 - 0.01f).lightmap(0, 240).endVertex();
        bufferBuilder.pos(x + width, y, 0).color(finalColor >> 16 & 0xFF, finalColor >> 8 & 0xFF, finalColor & 0xFF, finalColor >>> 24).tex(1, 0).lightmap(0, 240).endVertex();
        bufferBuilder.pos(x, y, 0).color(finalColor >> 16 & 0xFF, finalColor >> 8 & 0xFF, finalColor & 0xFF, finalColor >>> 24).tex(0, 0).lightmap(0, 240).endVertex();
        tessellator.draw();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

}
