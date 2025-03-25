package ru.levelup.client.api.clickgui.clientgui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import ru.levelup.client.Client;
import ru.levelup.client.api.clickgui.clientgui.theme.Theme;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.misc.StencilUtil;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.render.DrawHelper;
import ru.levelup.client.api.utils.render.ImageUtils;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.protect.UserData;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class ClientGui extends Screen {

    private float x = 0, y = 0, width = 500, height = 300;
    private boolean bol1 = false, preview = false, bol3 = false, bol4 = false, bol5 = false;
    private AnimationUtil anim1;
    private AnimationUtil anim2;
    private AnimationUtil anim3;
    private AnimationUtil anim4[];
    private AnimationUtil anim5;
    private AnimationUtil anim6;
    private AnimationUtil anim7;
    private AnimationUtil anim8;
    private AnimationUtil anim9;
    private AnimationUtil anim10;
    private AnimationUtil anim11;
    private AnimationUtil anim12;
    private AnimationUtil anim13;
    private AnimationUtil anim14;
    private AnimationUtil anim15;
    private AnimationUtil anim16;
    private AnimationUtil anim17;
    private AnimationUtil anim18;
    private AnimationUtil anim19;
    private AnimationUtil anim21;
    private AnimationUtil anim22;
    private AnimationUtil anim23;
    private AnimationUtil anim24;
    private AnimationUtil anim25;
    private AnimationUtil anim26;
    private AnimationUtil anim27;
    private AnimationUtil anim28;
    private Category currentCategory;
    private ResourceLocation imageLocation = ImageUtils.getResourceDinamic(UserData.getGet().avatar);

    public ClientGui(){
        super(new TranslationTextComponent("GUI"));
        anim1 = new AnimationUtil(0, 1, 0.10f);
        anim2 = new AnimationUtil(0, 1, 0.10f);
        anim3 = new AnimationUtil(0, 1, 0.10f);
        int numCategories = Category.values().length;
        anim4 = new AnimationUtil[numCategories];
        for (int i = 0; i < numCategories; i++) {
            anim4[i] = new AnimationUtil(0, 1, 0.05f);
        }
        anim5 = new AnimationUtil(0, 1, 0.15f);
        anim6 = new AnimationUtil(0, 1, 0.15f);
        anim7 = new AnimationUtil(0, 1, 0.15f);
        anim8 = new AnimationUtil(0, 1, 0.15f);
        anim9 = new AnimationUtil(0, 1, 0.15f);
        anim10 = new AnimationUtil(0, 1, 0.15f);
        anim11 = new AnimationUtil(0, 1, 0.15f);
        anim12 = new AnimationUtil(0, 1, 0.15f);
        anim13 = new AnimationUtil(0, 1, 0.15f);
        anim14 = new AnimationUtil(0, 1, 0.15f);
        anim15 = new AnimationUtil(0, 1, 0.15f);
        anim16 = new AnimationUtil(0, 1, 0.15f);
        anim17 = new AnimationUtil(0, 1, 0.15f);
        anim18 = new AnimationUtil(0, 1, 0.15f);
        anim19 = new AnimationUtil(0, 1, 0.15f);
        anim21 = new AnimationUtil(0, 1, 0.15f);
        anim22 = new AnimationUtil(0, 1, 0.15f);
        anim23 = new AnimationUtil(0, 1, 0.15f);
        anim24 = new AnimationUtil(0, 1, 0.15f);
        anim25 = new AnimationUtil(0, 1, 0.15f);
        anim26 = new AnimationUtil(0, 1, 0.15f);
        anim27 = new AnimationUtil(0, 1, 0.15f);
        anim28 = new AnimationUtil(0, 1, 0.15f);
        currentCategory = Category.HOME;
        x = ((float) Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (width / 2);
        y = ((float) Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) - (height / 2);
    }

    public void renderGuiScreen(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks){
        int color1 = ColorUtil.getColor(221, 255);
        DrawHelper.drawRound(x, y, width, height, 4,color1);
        float offestC = 20;
        int colorCategoryBackGround = ColorUtil.getColor(230, 255);
        DrawHelper.drawRound(x + 10, y + 15, 110, 135, 4, colorCategoryBackGround);
        for (int i = 0; i < Category.values().length; i++) {
            Category category = Category.values()[i];
            anim4[i].speed = 0.02f;
            anim4[i].to = currentCategory == category ? 245 : 255;
            int colorCategory = ColorUtil.getColor((int) (anim4[i].getAnim()), 255);
            int colorTextCategory = ColorUtil.getColor(0, 255);
            DrawHelper.drawRound(x + 15, y + offestC, 100, 20, 4, colorCategory);
            Fonts.font20.drawCenteredString(new MatrixStack(), category.getName(),
                    x + 15 + (100 / 2), y + offestC + 2, colorTextCategory);

            offestC += 35;
        }
        { // TODO: PACKEG HOME
            anim2.to = currentCategory == Category.HOME ? 255 : 0;
            anim2.speed = 0.02f;
            if (currentCategory == Category.HOME){
                int colorHomeBackGround = ColorUtil.getColor(230,
                        (int) (anim2.getAnim()));
                DrawHelper.drawRound(x + 130, y + 15, width - 140,
                        height - 25, 4, colorHomeBackGround);
                {
                    int colorHome1 = ColorUtil.getColor(255, (int) (anim2.getAnim()));
                    float x1 = x + 140;
                    float y1 = y + 25;
                    String user_name = UserData.getGet().getName();
                    String user_id = UserData.getGet().getUid();
                    String user_role = UserData.getGet().getRole();
                    float maxTextWidth = Math.max(
                            Math.max(Fonts.font20.getWidth("Name: " + user_name),
                                    Fonts.font20.getWidth("Subscribe to : LifeTime")),
                            Math.max(Fonts.font20.getWidth("Role: " + user_role),
                                    Fonts.font20.getWidth("# " + user_id)));
                    DrawHelper.drawRound(x1, y1, maxTextWidth + 12, 66, 4, colorHome1);
                    ImageUtils.drawImage(ImageUtils.getResourceDinamic(UserData.getGet().getAvatar()),
                            x1, y1, 64, 64, new Color(255, 255, 255, 255).getRGB());
                    { //TODO: USER INFO
                        int colorHomeText = ColorUtil.getColor(0,
                                (int) (anim2.getAnim()));
                        Fonts.font20.drawString("Name: " + user_name,
                                x1 +5, y1 +2, colorHomeText);
                        Fonts.font20.drawString("Subscribe to: LifeTime",
                                x1 +5, y1 +2 + Fonts.font20.getHeight(), colorHomeText);
                        Fonts.font20.drawString("Role: " + user_role,
                                x1 +5, y1 +2 + Fonts.font20.getHeight() * 2, colorHomeText);
                        Fonts.font20.drawString("#" + user_id,
                                x1 +5, y1 +2 + Fonts.font20.getHeight() * 3, colorHomeText);
                    }

                    {
                        int colorHomeText = ColorUtil.getColor(0,
                                (int) (anim2.getAnim()));
                        Fonts.font20.drawString("", x1, y1, colorHomeText);
                    }
                }
            }
            anim3.to = currentCategory == Category.THEME ? 255 : 0;
            anim3.speed = 0.02f;
            float columnIndex = 0;
            float x1 = x + 140;
            float y1 = y + 25;
            float offsetT = 0;
            if (currentCategory == Category.THEME){

                int colorThemeBackGround = ColorUtil.getColor(230,
                        (int) (anim3.getAnim()));
                DrawHelper.drawRound(x + 130, y + 15, width - 140,
                        height - 25, 4, colorThemeBackGround);
                {
                    for (Theme theme : Theme.values()) {
                        anim5.speed = 0.02f;
                        anim5.to = theme == Client.getInstance().getTheme() ? 245 : 255;
                        int color2 = ColorUtil.getColor((int) (anim5.getAnim()),
                                (int) (anim3.getAnim()));
                        int colorThemeText = ColorUtil.getColor(0,
                                (int) (anim3.getAnim()));
                        DrawHelper.drawRound(x1, y1, 100, 55, 2, color2);
                        Fonts.font18.drawString(theme.name(),
                                x1 + 5, y1 + 3, colorThemeText);
                        int i = 1;
                        while (i < Fonts.font18.getWidth(theme.name()) + 17) {
                            i++;
                        }
                        DrawHelper.drawGradientRound(x1 + 6, y1 + 20, 85, 5, 2,
                                ColorUtil.swapAlpha(theme.getColor(25), (int) (anim3.getAnim())),
                                ColorUtil.swapAlpha(theme.getColor(25), (int) (anim3.getAnim())),
                                ColorUtil.swapAlpha(theme.getColor(105), (int) (anim3.getAnim())),
                                ColorUtil.swapAlpha(theme.getColor(105), (int) (anim3.getAnim())));

                        DrawHelper.drawRound(x1 + 6, y1 + 30, 87, 20, 4, ColorUtil.getColor(240, 255));
                        Fonts.icon35.drawString("C", x1 + 8, y1 + 33, ColorUtil.getColor(5, 255));
                        Fonts.font20.drawString("PREVIEW", x1 + 30, y1 + 33, ColorUtil.getColor(5, 255));

                        columnIndex++;
                        if (columnIndex % 3 == 0) {
                            x1 = x + 140;
                            y1 += 75;
                        } else {
                            x1 += 120;
                        }
                    }
                }
            }
            {
                {
                    if (preview) {
                        DrawHelper.drawBlur(2, () ->
                                DrawHelper.drawRound(x, y, width, height, 4, color1));
                    }
                }
                {
                    anim6.speed = 0.02f;
                    anim6.to = preview ? 255 : 0;
                    DrawHelper.drawRound(x + 25, y + 25, width - 45, height - 45, 6, ColorUtil.getColor(255,
                            (int) anim6.getAnim()));
                    StencilUtil.initStencilToWrite();
                    DrawHelper.drawRound(x + 25, y + 25, width - 45, 35,0, ColorUtil.getColor(255,
                            (int) anim6.getAnim()));
                    StencilUtil.readStencilBuffer(1);
                    DrawHelper.drawRound(x + 25, y + 25, width - 45, 80, 6, ColorUtil.getColor(235,
                            (int) anim6.getAnim()));
                    StencilUtil.uninitStencilBuffer();
                    float x2 = x + 25;
                    float y2 = y + 25;
                    float width1 = width - 45;
                    ImageUtils.drawImage(new ResourceLocation("kuzureta/clientgui/preview/close.png"),
                            x2 + width1 - 30, y2 + 5, 25, 25, ColorUtil.getColor(5,(int) anim6.getAnim()));
                    Fonts.font20.drawString("This window shows how the theme will look in interface",
                            x2 + 10, y2 + 12, ColorUtil.getColor(5,(int) anim6.getAnim()));
                    {
                        DrawHelper.drawRound(x2 + 10, y2 + 55, 960 / 4, 540 / 4, 4, ColorUtil.getColor(230,
                                (int) anim6.getAnim()));
                        {
                            DrawHelper.drawRound(x2 + width1 - 187, y2 + 55, 170, 20, 4,ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawCenteredString(new MatrixStack(), "Sunset", (x2 + width1 - 187) + (170 /2)
                                    , y2 + 57, ColorUtil.getColor(5, (int) anim6.getAnim()));
                        }

                        {
                            DrawHelper.drawRound(x2 + width1 - 187, y2 + 95, 170, 20, 4,ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawCenteredString(new MatrixStack(), "Night", (x2 + width1 - 187) + (170 /2)
                                    , y2 + 97, ColorUtil.getColor(5, (int) anim6.getAnim()));
                        }

                        {
                            DrawHelper.drawRound(x2 + width1 - 187, y2 + 135, 170, 20, 4,ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawCenteredString(new MatrixStack(), "Day", (x2 + width1 - 187) + (170 /2)
                                    , y2 + 137, ColorUtil.getColor(5, (int) anim6.getAnim()));
                        }

                        {
                            anim7.speed = 0.15f;
                            anim7.to = bol3 ? 10 : 253;
                            anim8.speed = 0.15f;
                            anim8.to = bol4 ? 10 : 253 * 2;
                            anim9.speed = 0.15f;
                            anim9.to = bol5 ? 10 : 253 * 3;
                            StencilUtil.initStencilToWrite();
                            DrawHelper.drawRound(x2 + 10, y2 + 55, 960 / 4, 540 / 4, 4, ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            StencilUtil.readStencilBuffer(1);
                            ImageUtils.drawImage(new ResourceLocation("kuzureta/clientgui/preview/1.png"),
                                    x2 + anim7.getAnim(), y2 + 55, 960 / 4, 540 / 4, ColorUtil.getColor(230,
                                            (int) anim6.getAnim()));
                            StencilUtil.uninitStencilBuffer();
                            StencilUtil.initStencilToWrite();
                            DrawHelper.drawRound(x2 + 10, y2 + 55, 960 / 4, 540 / 4, 4, ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            StencilUtil.readStencilBuffer(1);
                            ImageUtils.drawImage(new ResourceLocation("kuzureta/clientgui/preview/2.png"),
                                    x2 + anim8.getAnim(), y2 + 55, 960 / 4, 540 / 4, ColorUtil.getColor(230,
                                            (int) anim6.getAnim()));
                            StencilUtil.uninitStencilBuffer();
                            StencilUtil.initStencilToWrite();
                            DrawHelper.drawRound(x2 + 10, y2 + 55, 960 / 4, 540 / 4, 4, ColorUtil.getColor(230,
                                    (int) anim6.getAnim()));
                            StencilUtil.readStencilBuffer(1);
                            ImageUtils.drawImage(new ResourceLocation("kuzureta/clientgui/preview/3.png"),
                                    x2 + anim9.getAnim(), y2 + 55, 960 / 4, 540 / 4, ColorUtil.getColor(230,
                                            (int) anim6.getAnim()));
                            StencilUtil.uninitStencilBuffer();
                        }
                        {
                            DrawHelper.StartScissor(x2 + 10, y2 + 55, 960 / 4, 540 / 4);
                            DrawHelper.drawRound(x2 + 10 + anim7.getAnim(), y2 + 65, 47, 10, 2, ColorUtil.getColor(255,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawString("kuzureta", x2 + 12 + anim7.getAnim(), y2 + 62,
                                    ColorUtil.swapAlpha(Client.getInstance().getTheme().getColor(25),
                                        (int) (anim6.getAnim())));

                            DrawHelper.drawRound(x2 + 10 + anim8.getAnim(), y2 + 65, 47, 10, 2, ColorUtil.getColor(255,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawString("kuzureta", x2 + 12 + anim8.getAnim(), y2 + 62,
                                    ColorUtil.swapAlpha(Client.getInstance().getTheme().getColor(25),
                                            (int) (anim6.getAnim())));

                            DrawHelper.drawRound(x2 + 10 + anim9.getAnim(), y2 + 65, 47, 10, 2, ColorUtil.getColor(255,
                                    (int) anim6.getAnim()));
                            Fonts.font20.drawString("kuzureta", x2 + 12 + anim9.getAnim(), y2 + 62,
                                    ColorUtil.swapAlpha(Client.getInstance().getTheme().getColor(25),
                                            (int) (anim6.getAnim())));
                            DrawHelper.stopScissor();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        anim1.speed = 0.05f;
        anim1.to = bol1 ? 1 : 0;
        DrawHelper.scale(x, y, width, height, anim1.getAnim(), ()-> this.renderGuiScreen(matrixStack, mouseX, mouseY, partialTicks));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        bol4 = true;
        if (preview){
            float x2 = x + 25;
            float y2 = y + 25;
            float width1 = width - 45;
            if (HoverUtils.isHover(x2 + width1 - 30, y2 + 5, 25, 25, mouseX, mouseY)){
                preview = false;
            }
            if (HoverUtils.isHover(x2 + width1 - 187, y2 + 55, 170, 20, mouseX, mouseY)){
                bol4 = false;
                bol5 = false;
                bol3 = true;
            }
            if (HoverUtils.isHover(x2 + width1 - 187, y2 + 95, 170, 20, mouseX, mouseY)){
                bol5 = false;
                bol3 = false;
                bol4 = true;
            }
            if (HoverUtils.isHover(x2 + width1 - 187, y2 + 135, 170, 20, mouseX, mouseY)){
                bol3 = false;
                bol4 = false;
                bol5 = true;
            }
        }
        if (!preview) {
            float offestC = 20;
            for (Category category : Category.values()) {
                if (HoverUtils.isHover(x + 15, y + offestC, 100, 20, mouseX, mouseY)
                        && button == 0) {
                    currentCategory = category;
                    anim2.reset();
                    anim3.reset();
                }
                offestC += 35;
            }
            float columnIndex = 0;
            float x1 = x + 140;
            float y1 = y + 25;
            if (currentCategory == Category.THEME) {
                {
                    for (Theme theme : Theme.values()) {
                        if (HoverUtils.isHover(x1, y1, 69, 25, mouseX, mouseY)) {
                            if (!preview) {
                                Client.getInstance().setTheme(theme);
                            }
                        }
                        if (HoverUtils.isHover(x1 + 6, y1 + 30, 87, 20, mouseX, mouseY)) {
                            preview = true;
                        }
                        columnIndex++;
                        if (columnIndex % 3 == 0) {
                            x1 = x + 140;
                            y1 += 75;
                        } else {
                            x1 += 120;
                        }
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
        bol1 = true;
    }

    @Override
    public void onClose() {
        super.onClose();
        bol1 = false;
    }

    private enum Category{
        HOME("Home"),
        THEME("Theme"),
        FRIEND("Friend"),
        CONFIG("Config");

        private String name;

        Category(String name1){
            this.name = name1;
        }

        public String getName() {
            return name;
        }
    }
}
