package ru.levelup.client.api.utils.aura;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AuraUtils {

    private static Minecraft mc = Minecraft.getInstance();

    public static float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }
    public static float getGCDValue() {
        return (float) (getGCD() * 0.15);
    }

    public static float getGCD() {
        float f1;
        return (f1 = (float) (mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }

    public static boolean isValidTarget(Entity e) {
        if (e == mc.player) return false;
        if (e.removed) return false;
        if (e instanceof PlayerEntity) return true;
        return false;
    }

}
