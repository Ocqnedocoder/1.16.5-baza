package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.protect.events.OpenScreenEvent;

import java.util.ArrayList;
import java.util.List;

public class GuiScreen extends Screen {

    private List<Panels> panels = new ArrayList<>();
    private AnimationUtil anim1 = new AnimationUtil(0, 1, 0.01f);
    
    public GuiScreen(){
        super(new StringTextComponent(""));
        panels.add(new Panels());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        panels.forEach(panel -> panel.render(matrixStack, mouseX, mouseY, partialTicks));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        new OpenScreenEvent(this).call();
        panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, button));
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        panels.forEach(panel -> panel.mouseDragged(mouseX, mouseY, button, dragX, dragY));
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        panels.forEach(panel -> panel.keyPressed(keyCode, scanCode, modifiers));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
