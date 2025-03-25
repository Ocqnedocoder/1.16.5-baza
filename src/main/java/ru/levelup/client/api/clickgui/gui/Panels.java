package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import ru.levelup.client.Client;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.misc.MathUtils;
import ru.levelup.client.api.utils.misc.StencilUtil;
import ru.levelup.client.api.utils.render.DrawHelper;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.api.utils.render.ColorUtil;

import java.util.*;

public class Panels {

    private AnimationUtil anim1[], anim2 = new AnimationUtil(0, 1, 0.05f), anim8[],anim9[];
    private Map<Module.Category, List<Button>> modsByCategory;
    private float x = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (551 / 2),
            y = (Minecraft.getInstance().getMainWindow().getHeight() / 2) - (440 ), width = 400, height = 255;
    private Module.Category currentCategory = Module.Category.VISUALS;
    public Panels(){
        int numCategories = Module.Category.values().length;
        anim1 = new AnimationUtil[numCategories];
        for (int i = 0; i < numCategories; i++) {
            anim1[i] = new AnimationUtil(0, 1, 0.05f);
        }
        int numModules = Client.getInstance().getManager().getModules().size();
        anim8 = new AnimationUtil[numModules];
        anim9 = new AnimationUtil[numModules];
        for (int i = 0; i < numModules; i++) {
            anim8[i] = new AnimationUtil(0, 1, 0.05f);
            anim9[i] = new AnimationUtil(0, 1, 0.05f);
        }
        modsByCategory = new HashMap<>();
        for (Module.Category category : Module.Category.values()){
            modsByCategory.put(category, new ArrayList<>());
        }
        for (Module module : Client.getInstance().getManager().getModules()){
            modsByCategory.get(module.getCategory()).add(new Button(module));
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int colorBackGround = ColorUtil.getColor(18, 210);
        x = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (400 / 2);
        y = 0;
        anim2.speed = 0.05f;
        anim2.to = 130;
        y = anim2.getAnim();
        DrawHelper.drawBlur(25, ()-> DrawHelper.drawRound(x, y, width, height, 6, colorBackGround));
        DrawHelper.drawRound(x, y, width, height, 6, colorBackGround);
        DrawHelper.drawRound(x, y, 56, height, 6, ColorUtil.getColor(18, 255));
        DrawHelper.drawRound(x + 10, y, 46, height, 0, ColorUtil.getColor(18, 255));
        Fonts.icon75.drawString("A", x + 11, y + 10, ColorUtil.getColor(159, (int) (anim2.getAnim())));
        float offset = 60;
        for (int i = 0; i < Module.Category.values().length; i++) {
            Module.Category category = Module.Category.values()[i];
            anim1[i].speed = 0.02f;
            anim1[i].to = (category == currentCategory) ? 255 : 160;
            int alpha = (int) anim1[i].getAnim();
            int colorIcon = ColorUtil.getColor(159,  alpha);
            float x1 = x;
            if (category == Module.Category.VISUALS) {
                x1 -= 1;
            }
            if (category == Module.Category.PLAYER) {
                x1 += 2;
            }
            Fonts.icon45.drawString(icon(category), x1 + 16, y + offset, colorIcon);
            offset += Fonts.icon45.getHeight() + 10;
            {
                for (int is = 0; is < Client.getInstance().getManager().getModules().size(); is++) {
                    anim8[is].to = modsByCategory.containsKey(currentCategory) ? 8 : 0;
                    anim9[is].to = modsByCategory.containsKey(currentCategory) ? 8 : 0;

                    StencilUtil.initStencilToWrite();
                    DrawHelper.drawRound(x, y, width, height, 6, colorBackGround);
                    StencilUtil.readStencilBuffer(1);
                    Button lastButton = null;
                    List<Button> modButtons = modsByCategory.get(currentCategory);
                    int maxX = 2;
                    int gmx = 0;
                    for (Button currentButton : modButtons) {
                        gmx = (int) MathUtils.reclamp(gmx, 0, maxX - 1);
                        if (modButtons.indexOf(currentButton) >= maxX) {
                            lastButton = modButtons.get(modButtons.indexOf(currentButton) - maxX);
                        }
                        float my = lastButton == null ? y + anim8[is].getAnim() : lastButton.y + lastButton.getHeight() + 5;
                        float mx = modButtons.indexOf(currentButton) > 0 && gmx > 0 ? modButtons.get(modButtons.indexOf(currentButton) - 1).x
                                + modButtons.get(modButtons.indexOf(currentButton) - 1).width  : x + 59;
                        currentButton.setX(mx + anim9[is].getAnim());
                        currentButton.setY(my);
                        currentButton.render(matrixStack, mouseX, mouseY);
                        gmx++;
                    }
                    StencilUtil.uninitStencilBuffer();
                }
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        float offset = 60;
        for (Module.Category category : Module.Category.values()){
            if (HoverUtils.isHover(x + 16, y + offset, 25, 25, mouseX, mouseY) && button == 0){
                for (int is = 0; is < Client.getInstance().getManager().getModules().size(); is++) {
                    anim8[is].reset();
                    anim9[is].reset();
                }
                currentCategory = category;
            }
            offset += Fonts.icon45.getHeight() + 10;
        }
        List<Button> mods = modsByCategory.get(currentCategory);
        mods.forEach(m -> m.mouseClicked(mouseX, mouseY, button));
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        List<Button> mods = modsByCategory.get(currentCategory);
        mods.forEach(m -> m.mouseReleased(mouseX, mouseY, button));
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        List<Button> mods = modsByCategory.get(currentCategory);
        mods.forEach(m -> m.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        List<Button> mods = modsByCategory.get(currentCategory);
        mods.forEach(m -> m.keyPressed(keyCode, scanCode, modifiers));
    }

    public String icon(Module.Category c) {
        switch (c) {
            case COMBAT: return "b";
            case MOVEMENT: return "d";
            case VISUALS: return "c";
            case PLAYER: return "e";
            case MISC: return "f";
            default: return "";
        }
    }
}
