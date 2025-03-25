package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import org.lwjgl.glfw.GLFW;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.setting.Setting;
import ru.levelup.client.api.module.setting.impl.AimCenterSetting;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.module.setting.impl.ModeSetting;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.render.DrawHelper;

import java.util.ArrayList;

public class Button {

    private Module module;
    float x = 0;
    float y = 0;
    float width;
    private float height;
    private float seth;
    private AnimationUtil enableAnimation = new AnimationUtil(0, 0, 0.15f);
    private AnimationUtil openSettingAnimation = new AnimationUtil(0, 0, 0.15f);
    private boolean openSetting;
    private boolean bindingModule;
    private ArrayList<Component> components = new ArrayList<>();

    public Button(Module module1) {
        this.module = module1;
        this.width = 157;
        this.height = 20;
        for (Setting setting : module.getSettings()) {
            Component component = createSetting(setting);
            if (component != null) {
                components.add(component);
            }
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        int colorBackGround = ColorUtil.getColor(18, 255);
        int colorText = ColorUtil.getColor(159, 255);
        DrawHelper.drawRound(x, y, width, getHeight(), 4, colorBackGround);
        Fonts.font20.drawString(bindingModule ? "Binding..." : module.getName(), x + 3, y + 2, colorText);
        enableAnimation.speed = 0.05f;
        enableAnimation.to = module.getToggled() ? 7 : 1;
        DrawHelper.drawRound(x + (width - 20), y + 6, 15, 8, 3, ColorUtil.getColor(25, 255));
        DrawHelper.drawRound(x + (width - 20) + enableAnimation.getAnim(), y + 7, 6, 6, 2, ColorUtil.getColor(35, 255));

        float pos = 0;
        for (Component component : components) {
            if (openSetting){
                component.renderSetting(matrixStack, x + 5, y + pos + 25, mouseX, mouseY);
                pos += component.height + 6;
            }
        }
        openSettingAnimation.to = openSetting ? pos + 4 : 0;
        seth = openSettingAnimation.getAnim();
    }

    public void mouseClicked(double mouseX, double mouseY, int button){
        if (button == 0) {
            if (HoverUtils.isHover(x, y, width, 20, mouseX, mouseY)){
                module.toggled();
            }
        }
        if (button == 1) {
            if (HoverUtils.isHover(x, y, width, 20, mouseX, mouseY)){
                openSetting = !openSetting;
            }
        }
        if (HoverUtils.isHover(x, y, 120, 20, mouseX, mouseY) && button == 2){
            bindingModule = true;
        }
        if(openSetting){
            components.forEach(component -> component.mouseClicked(mouseX, mouseY, button));
        }
    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (modifiers == 0 && bindingModule) {
            if (keyCode != GLFW.GLFW_KEY_RIGHT_SHIFT && keyCode != GLFW.GLFW_KEY_DELETE && keyCode != GLFW.GLFW_KEY_ESCAPE) {
                module.setKey(keyCode);
            } else {
                module.setKey((keyCode == GLFW.GLFW_KEY_DELETE) ? 0 : module.getKey());
            }
            bindingModule = false;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, button));
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        components.forEach(component -> component.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));

    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getHeight() {
        return height + seth;
    }

    private Component createSetting(Setting setting) {
        if (setting instanceof BooleanSetting) {
            return new BoolCopm(setting);
        }
        if (setting instanceof AimCenterSetting){
            return new AimComp(setting);
        }
        if (setting instanceof ModeSetting){
            return new ModeCopm(setting);
        }
        if (setting instanceof FloatSetting){
            return new FloatComp(setting);
        }
        return null;
    }
}
