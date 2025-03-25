package ru.levelup.client.api.utils.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.potion.Effects;
import ru.levelup.client.api.utils.Utils;

public class MovementUtils extends Utils {
    public static boolean isMoving() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.movementInput.moveForward != 0.0;
    }
    public static float getSpeed() {
        return (float) Math.sqrt(mc.player.getMotion().x * mc.player.getMotion().x + mc.player.getMotion().z * mc.player.getMotion().z);
    }
    public static boolean isOnBlockEdge() {
        ClientPlayerEntity clientPlayerEntity = mc.player;
        return mc.world.getCollisionShapes(clientPlayerEntity, clientPlayerEntity.getBoundingBox().offset(0.0, -0.5, 0.0).expand(0.001, 0.0, 0.001)).findAny().isPresent() && clientPlayerEntity.isOnGround();
    }
    public static void strafe() {
        strafe(getSpeed());
    }
    public static void strafe(float speed) {
        if (!isMoving()) return;
        mc.player.getMotion().x = -Math.sin(direction()) * speed;
        mc.player.getMotion().z = Math.cos(direction()) * speed;
    }
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Effects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(Effects.SPEED).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static double direction() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.player.moveForward < 0) {
            forward = -0.5F;
        } else if (mc.player.moveForward > 0) {
            forward = 0.5F;
        }

        if (mc.player.moveStrafing > 0) {
            rotationYaw -= 90 * forward;
        }

        if (mc.player.moveStrafing < 0) {
            rotationYaw += 90 * forward;
        }

        return Math.toRadians(rotationYaw);
    }
}
