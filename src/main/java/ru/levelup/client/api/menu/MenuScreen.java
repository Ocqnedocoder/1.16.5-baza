package ru.levelup.client.api.menu;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.misc.StencilUtil;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.render.DrawHelper;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuScreen extends Screen {

    private float x = 0, y = 0;
    private TextFieldWidget name, password, altmanager;
    private String waring = "Не верно";
    private static final String TEMP_FILE_NAME = "tempfile.txt";
    private AnimationUtil anim1 = new AnimationUtil(0, 1, 0.10f);
    private AnimationUtil anim2 = new AnimationUtil(0, 1, 0.10f);
    private AnimationUtil anim3 = new AnimationUtil(0, 1, 0.10f);
    private AnimationUtil anim4 = new AnimationUtil(0, 1, 0.10f);
    private AnimationUtil anim5 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim6 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim7 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim8 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim9 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim10 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim11 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim12 = new AnimationUtil(0, 1, 0.15f);
    private boolean openMainMenu = true;
    private String enteredUsername = "";
    private String enteredPassword = "";
    private String altmanagers = "";

    private boolean openAltManager = false;

    public MenuScreen(){
        super(new StringTextComponent(""));
        this.name = new TextFieldWidget(Minecraft.getInstance().fontRenderer,
                width / 2 - 100, height / 2 - 30, 200, 20, new StringTextComponent("Username"));
        this.password = new TextFieldWidget(Minecraft.getInstance().fontRenderer,
                width / 2 - 100, height / 2 - 30, 200, 20, new StringTextComponent("Password"));
        this.altmanager = new TextFieldWidget(Minecraft.getInstance().fontRenderer,
                width / 2 - 100, height / 2 - 30, 200, 20, new StringTextComponent("Password"));
        this.name.setVisible(true);
        this.name.setEnabled(true);
        this.password.setVisible(true);
        this.password.setEnabled(true);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int colorBack = ColorUtil.getColor(28, 255);
        DrawHelper.drawRound(x, 0, Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight(), 0, ColorUtil.getColor(28, 255));
        {
            y = 0;
            anim2.speed = 0.01f;
            anim2.to = openMainMenu ? -(Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) : (Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) - (100 / 2);
            y = anim2.getAnim();
            name.setWidth(200);
            password.setWidth(200);
            name.tick();
            password.tick();
            int color4 = ColorUtil.getColor(20, 255);
            float x3 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (250 / 2);
            DrawHelper.drawRound(x3, y - 10, 250, 100, 6, color4);
            name.x = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (200 / 2) + 5;
            name.y = (int) (y + 7);
            password.x = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (200 / 2) + 5;
            password.y = (int) (y + 37);
            name.setEnableBackgroundDrawing(false);
            password.setEnableBackgroundDrawing(false);
            {
                int color3 = ColorUtil.getColor(35, 255);
                float x2 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (200 / 2);
                DrawHelper.drawRound(x2, y, 200, 20, 4, color3);
                DrawHelper.drawRound(x2, y + 30, 200, 20, 4, color3);

                StencilUtil.initStencilToWrite();
                DrawHelper.drawRound(x2, y, 200, 60, 4, color3);
                StencilUtil.readStencilBuffer(1);
                if (name.getText().length() < 1){
                    Fonts.font20.drawString("Go taping your name", x2 + 5, y + 2, ColorUtil.getColor(255, 255));
                } else{
                    Fonts.font20.drawString(name.getText(), x2 + 5, y + 2, ColorUtil.getColor(255, 255));
                }
                if (password.getText().length() < 1){
                    Fonts.font20.drawString("Go taping your password", x2 + 5, y + 32, ColorUtil.getColor(255, 255));
                } else{
                    Fonts.font20.drawString(password.getText(), x2 + 5, y + 32, ColorUtil.getColor(255, 255));
                }
                StencilUtil.uninitStencilBuffer();

                {
                    float x1 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (50 / 2);
                    anim1.speed = 0.02f;
                    anim1.to = HoverUtils.isHover(x1, y + 60, 50, 20, mouseX, mouseY) ? 40 : 20;
                    int colorBack2 = ColorUtil.getColor((int) (anim1.getAnim()), 255);
                    DrawHelper.drawRound(x1, y + 60, 50, 20, 4, colorBack2);
                    int colorText = ColorUtil.getColor(255, 255);
                    Fonts.font25.drawCenteredString(new MatrixStack(),"Login", x1 + (50 / 2), y + 60, colorText);
                    {
                        if (openMainMenu) {
                            anim3.to = 255;
                        }else{
                            anim3.to = 0;
                        }
                        int color = ColorUtil.getColor(255, (int) (anim3.getAnim()));
                        {
                            anim4.speed = 0.01f;
                            anim4.to = openMainMenu ? openAltManager ? -(Minecraft.getInstance().getMainWindow().getScaledHeight()) :
                                    (Minecraft.getInstance().getMainWindow().getScaledHeight() / 2)
                                            - (130 / 2) : -130;
                            float y = anim4.getAnim();
                            float x4 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (300 / 2);
                            DrawHelper.drawRound(x4, y, 300, 130, 6, color4);
                            {
                                Fonts.font20.drawCenteredString(new MatrixStack(),"Welcome to Ximera Client",
                                        x4 + (300 / 2), y + 10, colorText);
                                {
                                    anim9.speed = 0.02f;
                                    anim10.speed = 0.02f;
                                    anim11.speed = 0.02f;
                                    anim12.speed = 0.02f;
                                    {
                                        anim9.to = HoverUtils.isHover(x4 + 25, y + 35, 110, 20, mouseX, mouseY) ? 40 : 20;
                                        int colorText1 = ColorUtil.getColor((int) anim9.getAnim(), 255);
                                        DrawHelper.drawRound(x4 + 25, y + 35, 110, 20, 4, colorText1);
                                        Fonts.font22.drawCenteredString(new MatrixStack(), "Singleplayer",
                                                x4 + (110 / 2) + 25, y + 36, colorText);
                                    }
                                    {
                                        anim10.to = HoverUtils.isHover(x4 + 165, y + 35, 110, 20, mouseX, mouseY) ? 40 : 20;
                                        int colorText2 = ColorUtil.getColor((int) anim10.getAnim(), 255);
                                        DrawHelper.drawRound(x4 + 165, y + 35, 110, 20, 4, colorText2);
                                        Fonts.font22.drawCenteredString(new MatrixStack(),"Multiplayer",
                                                x4 + (110 / 2) + 165, y + 36, colorText);
                                    }
                                    {
                                        anim11.to = HoverUtils.isHover(x4 + 25, y + 65, 250, 20, mouseX, mouseY) ? 40 : 20;
                                        int colorText2 = ColorUtil.getColor((int) anim11.getAnim(), 255);
                                        DrawHelper.drawRound(x4 + 25, y + 65, 250, 20, 4, colorText2);
                                        Fonts.font22.drawCenteredString(new MatrixStack(),"Alt Manager",
                                                x4 + (250 / 2) + 25, y + 66, colorText);
                                    }
                                    {
                                        anim12.to = HoverUtils.isHover(x4 + 25, y + 95, 250, 20, mouseX, mouseY) ? 40 : 20;
                                        int colorText2 = ColorUtil.getColor((int) anim12.getAnim(), 255);
                                        DrawHelper.drawRound(x4 + 25, y + 95, 250, 20, 4, colorText2);
                                        Fonts.font22.drawCenteredString(new MatrixStack(),"Options",
                                                x4 + (250 / 2) + 25, y + 96, colorText);
                                    }
                                    {
                                        anim8.speed = 0.01f;
                                        anim8.to = openAltManager ? (Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) + 55: 0;
                                        anim5.speed = 0.01f;
                                        anim5.to = openAltManager ? anim8.getAnim()
                                                - (200 / 2) : -(Minecraft.getInstance().getMainWindow().getScaledHeight());
                                        float y1 = anim5.getAnim();
                                        DrawHelper.drawRound(x4, y1, 300, 110, 4, color4);
                                        {
                                            Fonts.font20.drawCenteredString(new MatrixStack(), "Alt Manager",
                                                    x4 + 150, y1 + 5, colorText);
                                            {
                                                altmanager.setX((int) x4 + 54);
                                                altmanager.y = (int) y1 + 42;
                                                altmanager.tick();
                                                altmanager.setWidth(200);
                                                altmanager.setEnableBackgroundDrawing(false);
                                                anim7.speed = 0.05f;
                                                anim7.to = HoverUtils.isHover(x4 + 50, y1 + 35, 200, 20, mouseX, mouseY) ? 40 : 20;
                                                int color6 = ColorUtil.getColor((int) anim7.getAnim(), 255);
                                                DrawHelper.drawRound(x4 + 50, y1 + 35, 200, 20, 4, colorBack2);
                                                StencilUtil.initStencilToWrite();
                                                DrawHelper.drawRound(x4 + 50, y1 + 35, 200, 20, 4, colorBack2);
                                                StencilUtil.readStencilBuffer(1);
                                                if (altmanager.getText().length() < 1){
                                                    Fonts.font20.drawString("Your nickname", x4 + 54, y1 + 37, colorText);
                                                }
                                                Fonts.font20.drawString(altmanager.getText(), x4 + 54, y1 + 37, colorText);
                                                StencilUtil.uninitStencilBuffer();
                                                {
                                                    anim6.speed = 0.05f;
                                                    anim6.to = HoverUtils.isHover(x4 + 50, y1 + 70, 200, 20, mouseX, mouseY) ? 40 : 20;
                                                    int color5 = ColorUtil.getColor((int) anim6.getAnim(), 255);
                                                    DrawHelper.drawRound(x4 + 50, y1 + 70, 200, 20, 2, colorBack2);
                                                    Fonts.font22.drawCenteredString(new MatrixStack(), "Add Alt",
                                                            x4 + 100 + (100 / 2), y1 + 71, colorText);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(openAltManager){
            altmanager.mouseClicked(mouseX, mouseY, button);
            anim8.speed = 0.02f;
            anim8.to = openAltManager ? (Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) + 55 : 0;
            anim5.speed = 0.02f;
            anim5.to = openAltManager ? anim8.getAnim()
                    - (200 / 2) : -210;
            float y1 = anim5.getAnim();
            float x4 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (300 / 2);

            if (HoverUtils.isHover(x4 + 50, y1 + 70, 200, 20, mouseX, mouseY)){
                altmanagers = altmanager.getText();
                Minecraft.getInstance().getSession().username = altmanagers;
                openAltManager = false;
            }
        }
        if (openMainMenu){
            float y = anim4.getAnim();
            float x4 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (300 / 2);
            if (HoverUtils.isHover(x4 + 25, y + 35, 110, 20, mouseX, mouseY)){
                Minecraft.getInstance().displayGuiScreen(new WorldSelectionScreen(this));
            }
            if (HoverUtils.isHover(x4 + 165, y + 35, 110, 20, mouseX, mouseY)){
                Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(this));
            }
            if (HoverUtils.isHover(x4 + 25, y + 65, 250, 20, mouseX, mouseY)){
                openAltManager = true;
            }
            if (HoverUtils.isHover(x4 + 25, y + 95, 250, 20, mouseX, mouseY)){
                Minecraft.getInstance().displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
            }
        }
        name.mouseClicked(mouseX,mouseY,button);
        password.mouseClicked(mouseX,mouseY,button);
        {
            float x1 = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (50 / 2);

        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        name.keyPressed(keyCode, scanCode, modifiers);
        altmanager.keyPressed(keyCode, scanCode, modifiers);
        if(name.getText().contains(waring)){
            name.setText("");
            name.setMaxStringLength(15);
        }
        if(altmanager.getText().contains(waring)){
            altmanager.setText("");
            altmanager.setMaxStringLength(15);
        }
        password.keyPressed(keyCode, scanCode, modifiers);
        if(password.getText().contains(waring)){
            password.setText("");
            password.setMaxStringLength(15);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        name.charTyped(codePoint, modifiers);
        altmanager.charTyped(codePoint, modifiers);
        if(name.getText().contains(waring)){
            name.setText("");
            name.setMaxStringLength(15);
        }
        if(altmanager.getText().contains(waring)){
            altmanager.setText("");
            altmanager.setMaxStringLength(15);
        }
        password.charTyped(codePoint, modifiers);
        if(password.getText().contains(waring)){
            password.setText("");
            password.setMaxStringLength(15);
        }
        return super.charTyped(codePoint, modifiers);
    }

    private static boolean hasLinkExecuted() {
        File tempFile = new File(TEMP_FILE_NAME);
        if (!tempFile.exists()) {
            writeToFile(TEMP_FILE_NAME, "true");
            return false;
        }
        return readFileContent(TEMP_FILE_NAME).equals("false");
    }

    private static void writeToFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFileContent(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
