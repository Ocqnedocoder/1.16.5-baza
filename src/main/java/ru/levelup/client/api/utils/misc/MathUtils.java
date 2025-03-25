package ru.levelup.client.api.utils.misc;

import net.minecraft.client.MainWindow;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import ru.levelup.client.api.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class MathUtils extends Utils {

    public static int scale = 2;

    public static float harp(float val, float current, float speed) {
        float emi = ((current - val) * (speed/2)) > 0 ? Math.max((speed), Math.min(current - val, ((current - val) * (speed/2)))) : Math.max(current - val, Math.min(-(speed/2), ((current - val) * (speed/2))));
        return val + emi;
    }

    public static int ceiling_double_int(double p_76143_0_) {
        int var2 = (int) p_76143_0_;
        return p_76143_0_ > var2 ? var2 + 1 : var2;
    }

    public static double round(double num, double increment) {
        double v = (double) Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static BigDecimal round2(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }

    public static float reclamp(float val, float min, float max) {
        float res = val;
        res = res < min ? max : (res > max ? min : res);
        return res;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static double sq(double a) {
        return a * a;
    }

    public static int calc(int value) {
        MainWindow mainWindow = MC.getMainWindow();
        return (int) (value * mainWindow.getGuiScaleFactor() / scale);
    }

    public static double easeInOutQuad(double x, int step) {
        return x < 0.5 ? 2.0 * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, step) / 2.0;
    }

    public static double getBps(Entity e) {
        double prevZ = e.getPosZ() - e.lastTickPosZ;
        double prevX = e.getPosX() - e.lastTickPosX;
        double prevY = e.getPosY() - e.lastTickPosY;
        double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ + prevY * prevY);
        double currSpeed = lastDist * 15.3571428571;
        return currSpeed;
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static Vector3d interpolatePlayerPosition(float partialTicks) {
        return new Vector3d(
                MathUtils.interpolate(mc.player.getPosX(), mc.player.prevPosX, partialTicks),
                MathUtils.interpolate(mc.player.getPosY(), mc.player.prevPosY, partialTicks),
                MathUtils.interpolate(mc.player.getPosZ(), mc.player.prevPosZ, partialTicks)
        );
    }
    public static float calculateValue(float percentage, float min, float max) {
        if (!(percentage < 0.0F) && !(percentage > 100.0F)) {
            float range = max - min;
            return percentage / 100.0F * range + min;
        } else {
            throw new IllegalArgumentException("Процентное соотношение должно быть в пределах от 0 до 100");
        }
    }

    public static int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static float getRandomInRange(float min, float max) {
        SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(double min, double max) {
        SecureRandom random = new SecureRandom();
        return random.nextDouble() * (max - min) + min;
    }
}

