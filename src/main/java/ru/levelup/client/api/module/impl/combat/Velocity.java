package ru.levelup.client.api.module.impl.combat;

import net.minecraft.client.GameSettings;
import net.minecraft.network.play.server.SConfirmTransactionPacket;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventPacket;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.ModeSetting;

@ModuleHendler(name = "Velocity", description = "", c = Module.Category.COMBAT)
public class Velocity extends Module{

}
