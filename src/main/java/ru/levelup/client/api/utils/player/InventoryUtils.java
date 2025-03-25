package ru.levelup.client.api.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;

public class InventoryUtils {

    private static Minecraft mc = Minecraft.getInstance();

    public static int breakShield(LivingEntity target) {
        int hotBarSlot = getAxe(true);

        if (hotBarSlot != -1) {
            mc.player.connection.sendPacket(new CHeldItemChangePacket(hotBarSlot));
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
            return hotBarSlot;
        }

        int inventorySlot = getAxe(false);

        if (inventorySlot != -1) {
            mc.playerController.pickItem(inventorySlot);
            mc.playerController.attackEntity(mc.player, target);
            mc.player.swingArm(Hand.MAIN_HAND);
            return inventorySlot;
        }

        return -1;
    }

    public static int getAxe(boolean hotBar) {
        int startSlot = hotBar ? 0 : 9;
        int endSlot = hotBar ? 9 : 36;

        for (int i = startSlot; i < endSlot; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);

            if (itemStack.getItem() instanceof AxeItem) {
                return i;
            }
        }

        return -1;
    }

}
