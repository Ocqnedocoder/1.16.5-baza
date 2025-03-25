package ru.levelup.client.api.utils.aura;

import net.minecraft.network.IPacket;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.login.client.CEncryptionResponsePacket;
import net.minecraft.network.login.client.CLoginStartPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.network.status.client.CPingPacket;
import net.minecraft.network.status.client.CServerQueryPacket;
import org.apache.commons.lang3.time.StopWatch;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventPacketReciew;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.utils.player.PacketUtil;
import ru.levelup.client.api.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;


public class AuraComp extends Utils {

    public static final ConcurrentLinkedQueue<IPacket<?>> packets = new ConcurrentLinkedQueue<>();
    public static boolean noSlow, dispatch;
    public static ArrayList<Class<?>> exemptedPackets = new ArrayList<>();
    public static StopWatch exemptionWatch = new StopWatch();

    public static void setExempt(Class<?>... packets) {
        exemptedPackets = new ArrayList<>(Arrays.asList(packets));
        exemptionWatch.reset();
    }


    @EventHendler
    public void onPacket(EventPacketReciew e) {
        if (mc.player == null) {
            packets.clear();
            exemptedPackets.clear();
            return;
        }

        if (mc.player.removed || mc.isSingleplayer()) {
            packets.forEach(PacketUtil::sendNoEvent);
            packets.clear();
            noSlow = false;
            exemptedPackets.clear();
            return;
        }

        final IPacket<?> packet = e.getPacket();

        if (packet instanceof CHandshakePacket || packet instanceof CLoginStartPacket ||
                packet instanceof CServerQueryPacket || packet instanceof CPingPacket ||
                packet instanceof CEncryptionResponsePacket) {
            return;
        }

        if (noSlow && !dispatch) {
            if (!e.isCancelled() && exemptedPackets.stream().noneMatch(packetClass ->
                    packetClass == packet.getClass())) {
                packets.add(packet);
                e.isCancelled();
            }
        } else if (packet instanceof CPlayerPacket) {
            packets.forEach(PacketUtil::sendNoEvent);
            packets.clear();
            dispatch = false;
        }
    }

    public static void dispatch() {
        dispatch = true;
    }


    @EventHendler
    public void onUpdate(EventUpdate e) {
        if(mc.player == null){
            packets.clear();
        }
    }


}

