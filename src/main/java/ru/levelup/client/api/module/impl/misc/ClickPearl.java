package ru.levelup.client.api.module.impl.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.util.Hand;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.MouseInputEvent;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;

@ModuleHendler(name = "ClickPearl", description = "", c = Module.Category.MISC)
public class ClickPearl extends Module {

    @EventHendler
    public void evtt(MouseInputEvent evt) {
        if (evt.getButton() == 2) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(i));
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                }
            }
        }
    }

}
