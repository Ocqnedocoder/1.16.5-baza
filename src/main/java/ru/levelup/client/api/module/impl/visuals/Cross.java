package ru.levelup.client.api.module.impl.visuals;

import net.minecraft.client.MainWindow;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import ru.levelup.client.api.event.AnimationMath;
import ru.levelup.client.api.event.Event;
import ru.levelup.client.api.event.EventRender;
import ru.levelup.client.api.module.IMinecraft;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.render.DrawHelper;

import java.awt.*;

@ModuleHendler(name = "Cross", description = "Custom cross hair", c = Module.Category.VISUALS)

public class Cross extends Module{
    private float circleAnimation = 0.0F;


    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            handleCrosshairRender();
        }
    }

    private void handleCrosshairRender() {
        if (IMinecraft.mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON) {
            return;
        }

        final MainWindow mainWindow = IMinecraft.mc.getMainWindow();

        final float x = (float) mainWindow.getScaledWidth() / 2.0F;
        final float y = (float) mainWindow.getScaledHeight() / 2.0F;

        final float calculateCooldown = IMinecraft.mc.player.getCooledAttackStrength(1.0F);
        final float endRadius = MathHelper.clamp(calculateCooldown * 360, 0, 360);

        this.circleAnimation = AnimationMath.lerp(this.circleAnimation, -endRadius, 4);

        final int mainColor = ColorUtil.toRGBA(30, 30, 30, 255);

        DrawHelper.drawCircle(x, y, 0, 360, 3.5f, 3,false , mainColor);
        DrawHelper.drawCircle(x, y, 0, circleAnimation, 3.5f, 3, false, Color.BLACK.getRGB());
    }
}
