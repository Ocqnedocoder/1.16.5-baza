package ru.levelup.client.api.utils.font;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import ru.levelup.client.api.utils.render.ColorUtil;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.*;


/**
 *
 * @author superblaubeere27
 * @ported sprayD, Subi (fork + port to 1.16)
 *
 */
public class CCFontRenderer {

    private float posX;
    private float posY;
    private int[] colorCode = new int[32];
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;

    private CCFont regularGlyphPage, boldGlyphPage, italicGlyphPage, boldItalicGlyphPage;


    public CCFontRenderer(CCFont regularGlyphPage, CCFont boldGlyphPage, CCFont italicGlyphPage,
                          CCFont boldItalicGlyphPage) {
        this.regularGlyphPage = regularGlyphPage;
        this.boldGlyphPage = boldGlyphPage;
        this.italicGlyphPage = italicGlyphPage;
        this.boldItalicGlyphPage = boldItalicGlyphPage;

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }
    public static CCFontRenderer create(String name, int size, boolean bold, boolean italic, boolean boldItalic) {
        try(InputStream audioSrc = Minecraft.class.getResourceAsStream("/assets/minecraft/font/" + name);
        ) {
            char[] chars = new char[4096]; //256

            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) i;
            }

            CCFont regularPage = null;

            regularPage = new CCFont(Font.createFont(Font.TRUETYPE_FONT, audioSrc).deriveFont(Font.PLAIN, size), true, true);


            regularPage.generateGlyphPage(chars);
            regularPage.setupTexture();

            CCFont boldPage = regularPage;
            CCFont italicPage = regularPage;
            CCFont boldItalicPage = regularPage;

            if (bold) {
                boldPage = new CCFont(Font.createFont(Font.TRUETYPE_FONT, audioSrc).deriveFont(Font.BOLD, size), true, true);

                boldPage.generateGlyphPage(chars);
                boldPage.setupTexture();
            }

            if (italic) {
                italicPage = new CCFont(Font.createFont(Font.TRUETYPE_FONT, audioSrc).deriveFont(Font.ITALIC, size), true, true);;

                italicPage.generateGlyphPage(chars);
                italicPage.setupTexture();
            }

            if (boldItalic) {
                boldItalicPage = new CCFont(Font.createFont(Font.TRUETYPE_FONT, audioSrc).deriveFont(Font.BOLD | Font.ITALIC, size), true, true);

                boldItalicPage.generateGlyphPage(chars);
                boldItalicPage.setupTexture();
            }

            return new CCFontRenderer(regularPage, boldPage, italicPage, boldItalicPage);
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public int drawStringWithShadow(MatrixStack matrices, String text, float x, float y, int color) {
        return drawString(matrices, text, x, y, color, true);
    }

    public int drawStringWithShadow(MatrixStack matrices, String text, double x, double y, int color) {
        return drawString(matrices, text, (float) x, (float) y, color, true);
    }

    public int drawString(MatrixStack matrices, String text, float x, float y, int color) {
        return drawString(matrices, text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color) {
        return drawString(new MatrixStack(), text, x, y, color, false);
    }

    public int drawString(MatrixStack matrices, String text, double x, double y, int color) {
        return drawString(matrices, text, (float) x, (float) y, color, false);
    }

    public int drawCenteredString(MatrixStack matrices, String text, double x, double y, int color) {
        return drawString(matrices, text, (float) x - getWidth(text) / 2f, (float)y, color, false);
    }

    public int drawCenteredStringWithShadow(MatrixStack matrices, String text, double x, double y, int color) {
        return drawString(matrices, text, (float) x - getWidth(text) / 2f, (float) y - getHeight() / 2f, color, true);
    }

    public int drawString(MatrixStack matrices, String text, float x, float y, int color, boolean dropShadow) {
        if (ColorUtil.a(color)< (float) 10 /255)return 1;
        this.resetStyles();
        int i;

        if (dropShadow) {
            i = this.renderString(matrices, text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, this.renderString(matrices, text, x, y, color, false));
        } else {
            i = this.renderString(matrices, text, x, y, color, false);
        }

        return i;
    }

    private int renderString(MatrixStack matrices, String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }
            this.posX = x * 2.0f;
            this.posY = y * 2.0f;
            this.renderStringAtPos(matrices, text, dropShadow, color);
            return (int) (this.posX / 4.0f);
        }
    }

    private void renderStringAtPos(MatrixStack matrices, String text, boolean shadow, int color) {
        CCFont glyphPage = getCurrentGlyphPage();
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;

        matrices.push();

        matrices.scale(0.5f, 0.5F, 0.5F);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.enableTexture();

        glyphPage.bindTexture();

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i1 < 0) {
                        i1 = 15;
                    }
                    if (shadow) {
                        i1 += 16;
                    }
                    int j1 = this.colorCode[i1];
                    g = (float) (j1 >> 16 & 255) / 255.0F;
                    h = (float) (j1 >> 8 & 255) / 255.0F;
                    k = (float) (j1 & 255) / 255.0F;
                } else if (i1 == 16) {
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else {
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                }

                ++i;
            } else {
                glyphPage = getCurrentGlyphPage();

                glyphPage.bindTexture();
                float f = glyphPage.drawChar(matrices, c0, posX, posY, g, k, h, alpha);
                RenderSystem.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                doDraw(matrices, f, glyphPage);
            }

        }
        glyphPage.unbindTexture();
        matrices.pop();
    }

    private void doDraw(MatrixStack matrices, float f, CCFont glyphPage) {
        if (this.strikethroughStyle) {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            GlStateManager.disableTexture();
            bufferBuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
            bufferBuilder
                    .pos( this.posX,  (this.posY + (float) (glyphPage.getMaxFontHeight() / 2)), 0.0D)
                    .endVertex();
            bufferBuilder.pos(matrices.getLast().getMatrix(),  (this.posX + f),
                    (this.posY + (float) (glyphPage.getMaxFontHeight() / 2)), 0.0F).endVertex();
            bufferBuilder.pos(matrices.getLast().getMatrix(),  (this.posX + f),
                    (this.posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F), 0.0F).endVertex();
            bufferBuilder.pos(matrices.getLast().getMatrix(),  this.posX,
                    (this.posY + (float) (glyphPage.getMaxFontHeight() / 2) - 1.0F), 0.0F).endVertex();
            bufferBuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferBuilder);
            GlStateManager.enableTexture();
        }

        if (this.underlineStyle) {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            GlStateManager.disableTexture();
            bufferBuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION);
            int l = this.underlineStyle ? -1 : 0;
            bufferBuilder.pos(matrices.getLast().getMatrix(), (float) (this.posX + (float) l),
                    (this.posY + (float) glyphPage.getMaxFontHeight()), 0.0F).endVertex();
            bufferBuilder
                    .pos(matrices.getLast().getMatrix(),  (this.posX + f), (this.posY + (float) glyphPage.getMaxFontHeight()), 0.0F)
                    .endVertex();
            bufferBuilder.pos(matrices.getLast().getMatrix(), (this.posX + f),
                    (this.posY + (float) glyphPage.getMaxFontHeight() - 1.0F), 0.0F).endVertex();
            bufferBuilder.pos(matrices.getLast().getMatrix(),  (this.posX + (float) l),
                    (this.posY + (float) glyphPage.getMaxFontHeight() - 1.0F), 0.0F).endVertex();;
            bufferBuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferBuilder);
            GlStateManager.enableTexture();
        }

        this.posX += f;
    }

    private CCFont getCurrentGlyphPage() {
        if (boldStyle && italicStyle)
            return boldItalicGlyphPage;
        else if (boldStyle)
            return boldGlyphPage;
        else if (italicStyle)
            return italicGlyphPage;
        else
            return regularGlyphPage;
    }

    private void resetStyles() {
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    public float getHeight() {
        return regularGlyphPage.getMaxFontHeight() / 2;
    }

    public int getWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;

        CCFont currentPage;

        int size = text.length();

        boolean on = false;

        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                i++;
                on = false;
            } else {
                if (on)
                    i--;

                character = text.charAt(i);

                currentPage = getCurrentGlyphPage();

                width += currentPage.getWidth(character) - 8;
            }
        }

        return width / 2;
    }

    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();

        boolean on = false;

        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        int width = 0;

        CCFont currentPage;

        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);
            if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                i++;
                on = false;
            } else {
                if (on)
                    i--;
                character = text.charAt(i);
                currentPage = getCurrentGlyphPage();
                width += (currentPage.getWidth(character) - 8) / 2;
            }
            if (i > width) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, character);
            } else {
                stringbuilder.append(character);
            }
        }
        return stringbuilder.toString();
    }
}
