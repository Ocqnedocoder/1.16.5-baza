package ru.levelup.client.api.module.impl.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.client.CUseEntityPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventPacketReciew;
import ru.levelup.client.api.event.impl.EventRotation;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.utils.aura.AuraComp;
import ru.levelup.client.api.utils.aura.AuraUtils;
import ru.levelup.client.api.utils.misc.TimerUtil;
import ru.levelup.client.api.utils.player.InventoryUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.MathHelper.wrapDegrees;

@ModuleHendler(name = "KillAura", description = "I beat women and children", c = Module.Category.COMBAT)
public class KillAura extends Module {
    public FloatSetting range = new FloatSetting("Range", 3, 2, 6, 0.1f);
    public FloatSetting prerange = new FloatSetting("Pre-range", 1, 0, 6, 0.5f);
    public static List<PlayerEntity> isBot = new ArrayList<>();
    public BooleanSetting shieldBreaker = new BooleanSetting("Shield Break", BooleanSetting.ToggleState.ON);
    public BooleanSetting shieldDesync = new BooleanSetting("Shield Desync", BooleanSetting.ToggleState.ON);
    public BooleanSetting shieldHelper = new BooleanSetting("Shield Helper", BooleanSetting.ToggleState.ON);
    public BooleanSetting crit = new BooleanSetting("Only Critical", BooleanSetting.ToggleState.ON);
    public BooleanSetting targetesp = new BooleanSetting("target esp", BooleanSetting.ToggleState.ON);
    public static LivingEntity target;
    public static List<LivingEntity> targets = new ArrayList<>();
    public Vector2f rotation;
    public float rotYaw = 0;
    public float rotPitch = 0;
    public TimerUtil timer = new TimerUtil();

    public KillAura() {
        addSetting(range, prerange,  crit, shieldHelper, shieldBreaker,targetesp);
    }

    @EventHendler
    public void onUpdate(EventRotation e) {
        if (mc.player == null) {
            return;
        }
        rotYaw = rotation.x;
        rotPitch = rotation.y;
        if (target != null && (target.getHealth() <= 0 || target.removed)) {
            target = null;
            AuraComp.noSlow = false;
        } else {
            AuraComp.noSlow = true;
            if (mayAttack()) {
                double distanceAura = getDistanceAura(target);
                if (target != null) {
                    if (distanceAura >= range.get() + prerange.get()) {
                        target = null;
                    }
                    if (target != null) {
                        e.setPitch(rotPitch);
                        e.setYaw(rotYaw);
                        mc.player.rotationYawHead = rotYaw;
                    }
                } else {
                    target = null;
                }
            }
        }
    }

    @EventHendler
    public void onUpdate(EventUpdate e) {
        findTarget();
        if (mc.player != null && mc.playerController != null && target != null) {
            rotate();
            if (shieldDesync.get() == BooleanSetting.ToggleState.ON && target.getActiveItemStack().getItem() instanceof ShieldItem && timer.hasReached(MathHelper.getRandomNumberBetween(100, 400))) {
                mc.playerController.onStoppedUsingItem(mc.player);
            }
            double distanceAura = getDistanceAura(target);
            if (distanceAura <= range.get() && mc.player.getCooledAttackStrength(0) >= 0.75f) {
                if (mc.player.isActiveItemStackBlocking()) {
                    mc.playerController.onStoppedUsingItem(mc.player);
                }
                if (mayAttack()) {
                    mc.player.setSprinting(false);
                    rotYaw = rotation.x;
                    rotPitch = rotation.y;
                    if (shieldBreaker.get() == BooleanSetting.ToggleState.ON) {
                        breakShieldAndSwapSlot();
                    }
                    mc.playerController.attackEntity(mc.player, target);
                    mc.player.swingArm(Hand.MAIN_HAND);
                    mc.player.resetCooldown();
                }
            }
        }
    }

    public boolean mayAttack() {
        if (crit.get() == BooleanSetting.ToggleState.ON) {
            return mc.player.fallDistance > 0 && !mc.player.isOnGround();
        } else return true;
    }

    @EventHendler
    public void onPacket(EventPacketReciew e) {
        if (target != null) {
            if (e.getPacket() instanceof CUseEntityPacket) {
                CUseEntityPacket p = (CUseEntityPacket) e.getPacket();
                if (!(p.getAction() == CUseEntityPacket.Action.ATTACK)) {
                    e.isCancelled();
                }
            }
            if (e.getPacket() instanceof CPlayerTryUseItemOnBlockPacket) {
                CPlayerTryUseItemOnBlockPacket p = (CPlayerTryUseItemOnBlockPacket) e.getPacket();
            }
        }
    }

    private void breakShieldAndSwapSlot() {
        LivingEntity targetEntity = target;
        if (!(targetEntity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) targetEntity;
        if (!target.isActiveItemStackBlocking(2) || player.isSpectator() || player.isCreative()) {
            return;
        }
        ItemStack offhandItem = target.getHeldItemOffhand();
        ItemStack mainhandItem = target.getHeldItemMainhand();
        if (offhandItem.getItem() == Items.SHIELD || mainhandItem.getItem() == Items.SHIELD) {
            int slot = InventoryUtils.breakShield(player);
            if (slot > 8) {
                mc.playerController.pickItem(slot);
            }
        }
    }

    public Vector3d getVecTarget() {
        Vector3d eyesPos = mc.player.getEyePosition(1.0f);
        Vector3d targetPos = target.getEyePosition(1.0f);
        RayTraceContext rayTraceContext = new RayTraceContext(eyesPos, targetPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, mc.player);
        BlockRayTraceResult blockRayTraceResult = mc.world.rayTraceBlocks(rayTraceContext);
        if (blockRayTraceResult.getType() == RayTraceResult.Type.MISS) {
            return targetPos;
        } else {
            return blockRayTraceResult.getHitVec();
        }
    }
    public void rotate() {
        Vector3d vecTarget = getVecTarget();
        double targetX = vecTarget.x, targetY = vecTarget.y, targetZ = vecTarget.z;
        double x = (targetX - mc.player.getPosX() + target.getPosX() - target.prevPosX );
        double y = (targetY - mc.player.getPosY() - mc.player.getEyeHeight() + 0.3f + 1 + target.getPosY() - target.prevPosY);
        double z = (targetZ - mc.player.getPosZ() - target.getPosZ() - target.prevPosZ );
        double yaw = AuraUtils.getFixedRotation((float) ((wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90)) + MathHelper.getRandomNumberBetween(-2, 2)));
        double pitch = AuraUtils.getFixedRotation((float) wrapDegrees(Math.toDegrees(-Math.atan2(y, Math.hypot(x, z)))) + MathHelper.getRandomNumberBetween(-2, 2));
        pitch = MathHelper.clamp((float) pitch, -90, 90);
        rotation = new Vector2f((float) yaw, (float) pitch);
    }

    public float getDistanceAura(Entity entityIn) {
        float f = (float) (mc.player.getPosX() - (entityIn.getPosX() + mc.player.razXZ(entityIn)[0]));
        float f1 = (float) (mc.player.getPosY() - entityIn.getPosY());
        float f2 = (float) (mc.player.getPosZ() - entityIn.getPosZ() + mc.player.razXZ(entityIn)[1]);
        return net.minecraft.util.math.MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public boolean isValidTarget(Entity e) {
        if (e == mc.player) return false;
        if (e.removed) return false;
        if (checkBot((PlayerEntity) e)) return false;
        if (e instanceof PlayerEntity) return true;
        return false;
    }

    public void findTarget() {
        targets.clear();
        List<LivingEntity> validTargets = mc.world.getPlayers().stream().filter(entity ->
                entity instanceof LivingEntity).map(entity -> (LivingEntity) entity).filter(entity ->
                mc.player.getDistance(entity) <= range.get() + this.prerange.get()).filter(this::isValidTarget).filter(entity ->
                mc.player != entity).collect(Collectors.toList());
        if (validTargets.isEmpty()) {
            return;
        }
        target = validTargets.get(0);
    }


    @EventHendler
    public void onRenderWorldLast(EventUpdate evt) {
        Iterator<PlayerEntity> iterator = isBot.iterator();
        while (iterator.hasNext()) {
            PlayerEntity bot = iterator.next();
            if (areMovementsSimilar(mc.player, bot)) {
                iterator.remove();
            }
        }
    }


    private boolean areMovementsSimilar(PlayerEntity player1, PlayerEntity player2) {
        double positionThreshold = 1.0;
        double velocityThreshold = 0.1;

        return player1.getDistanceSq(player2) < positionThreshold * positionThreshold &&
                player1.getMotion().distanceTo(player2.getMotion()) < velocityThreshold;
    }


    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            rotYaw = mc.player.rotationYaw;
            rotPitch = mc.player.rotationPitch;
        }
        target = null;
        targets.clear();
        isBot.clear();
    }

    public static boolean checkBot(PlayerEntity entity) {
        return isBot.contains(entity);
    }
}