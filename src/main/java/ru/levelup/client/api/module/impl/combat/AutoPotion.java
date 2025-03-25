package ru.levelup.client.api.module.impl.combat;

import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Hand;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventRotation;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.utils.misc.TimerUtil;

import java.util.function.Supplier;

@ModuleHendler(name = "AutoPotion", description = "", c = Module.Category.COMBAT)
public class AutoPotion extends Module{

    private static BooleanSetting strenghts = new BooleanSetting("Strengh", BooleanSetting.ToggleState.ON);
    private static BooleanSetting speed = new BooleanSetting("Speed", BooleanSetting.ToggleState.ON);
    private static BooleanSetting fireResist = new BooleanSetting("Fire Resist", BooleanSetting.ToggleState.ON);

    public boolean isActive;
    private int selectedSlot;
    private float previousPitch;
    private TimerUtil time = new TimerUtil();
    public boolean isActivePotion;
    public static boolean isChangingItem;
    private boolean isItemChangeRequested;
    private int previousSlot = -1;

    public AutoPotion() {
        addSetting(strenghts, speed, fireResist);
    }

    @EventHendler
    public void oneEve(EventUpdate evenUpdate) {
        if (this.isActive()) {
            for (PotionType potionType : PotionType.values()) {
                isActivePotion = potionType.isEnabled();
            }
        } else {
            isActivePotion = false;
        }
        if (this.isActive() && previousPitch == mc.player.lastReportedPitch) {
            int oldItem = mc.player.inventory.currentItem;
            this.selectedSlot = -1;
            for (PotionType potionType : PotionType.values()) {
                if (potionType.isEnabled()) {
                    int slot = this.findPotionSlot(potionType);
                    if (this.selectedSlot == -1) {
                        this.selectedSlot = slot;
                    }

                    this.isActive = true;
                }
            }
            if (this.selectedSlot > 8) {
                mc.playerController.pickItem(this.selectedSlot);
            }
            mc.player.connection.sendPacket(new CHeldItemChangePacket(oldItem));
        }
        if (time.hasTimeElapsed(500L)) {
            try {
                this.reset();
                this.selectedSlot = -2;
            } catch (Exception ignored) {
            }
        }

        changeItemSlot(this.selectedSlot == -2);

    }

    @EventHendler
    public void onEvev(EventRotation e) {
        if (!this.isActive()) {
            return;
        }

        float[] angles = new float[]{mc.player.rotationYaw, 90.0F};
        this.previousPitch = 90.0F;
        e.setYaw(angles[0]);
        e.setPitch(this.previousPitch);
        mc.player.rotationYawHead = angles[0];
        mc.player.renderYawOffset = angles[0];
    }

    private void reset() {
        for (PotionType potionType : PotionType.values()) {
            if (potionType.isPotionSettingEnabled().get()) {
                potionType.setEnabled(this.isPotionActive(potionType));
            }
        }
    }

    private int findPotionSlot(PotionType type) {
        int hbSlot = this.getPotionIndexHb(type.getPotionId());
        if (hbSlot != -1) {
            setPreviousSlot(mc.player.inventory.currentItem);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(hbSlot));
            useItem(Hand.MAIN_HAND);
            type.setEnabled(false);
            time.reset();
            return hbSlot;
        } else {
            int invSlot = this.getPotionIndexInv(type.getPotionId());
            if (invSlot != -1) {
                setPreviousSlot(mc.player.inventory.currentItem);
                mc.playerController.pickItem(invSlot);
                useItem(Hand.MAIN_HAND);
                mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                type.setEnabled(false);
                time.reset();
                return invSlot;
            } else {
                return -1;
            }
        }
    }

    public boolean isActive() {
        for (PotionType potionType : PotionType.values()) {
            if (potionType.isPotionSettingEnabled().get() && potionType.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPotionActive(PotionType type) {
        if (mc.player.isPotionActive(type.getPotion())) {
            this.isActive = false;
            return false;
        } else {
            return this.getPotionIndexInv(type.getPotionId()) != -1 || this.getPotionIndexHb(type.getPotionId()) != -1;
        }
    }

    private int getPotionIndexHb(int id) {
        for (int i = 0; i < 9; ++i) {
            for (EffectInstance potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Effect.get(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int getPotionIndexInv(int id) {
        for (int i = 9; i < 36; ++i) {
            for (EffectInstance potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Effect.get(id) && mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public void onDisable() {
        isActive = false;
        super.onDisable();
    }

    enum PotionType {
        STRENGHT(Effects.STRENGTH, 5, () -> strenghts.get() == BooleanSetting.ToggleState.ON),
        SPEED(Effects.SPEED, 1, () -> speed.get() == BooleanSetting.ToggleState.ON),
        FIRE_RESIST(Effects.STRENGTH, 12, () -> fireResist.get() == BooleanSetting.ToggleState.ON);

        private final Effect potion;
        private final int potionId;
        private final Supplier<Boolean> potionSetting;
        private boolean enabled;

        PotionType(Effect potion, int potionId, Supplier<Boolean> potionSetting) {
            this.potion = potion;
            this.potionId = potionId;
            this.potionSetting = potionSetting;
        }

        public Effect getPotion() {
            return this.potion;
        }

        public int getPotionId() {
            return this.potionId;
        }

        public Supplier<Boolean> isPotionSettingEnabled() {
            return this.potionSetting;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean var1) {
            this.enabled = var1;
        }

    }

    public void changeItemSlot(boolean resetAfter) {
        if (this.isItemChangeRequested && this.previousSlot != -1) {
            isChangingItem = true;
            mc.player.inventory.currentItem = this.previousSlot;
            if (resetAfter) {
                this.isItemChangeRequested = false;
                this.previousSlot = -1;
                isChangingItem = false;
            }
        }
    }

    public void setPreviousSlot(int slot) {
        this.previousSlot = slot;
    }

    public static void useItem(Hand hand) {
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(hand));
        mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
    }

}
