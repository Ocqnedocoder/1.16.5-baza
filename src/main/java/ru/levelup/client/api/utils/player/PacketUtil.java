package ru.levelup.client.api.utils.player;

import net.minecraft.network.IPacket;
import ru.levelup.client.api.utils.Utils;

public final class PacketUtil extends Utils {

    public static void send(final IPacket<?> packet) {
        mc.player.connection.sendPacket(packet);
    }
    public static void sendNoEvent(final IPacket<?> packet) {
        mc.player.connection.sendPacket(packet);
    }
    public static void receiveNoEvent(final IPacket<?> packet) {
        mc.player.connection.sendPacket(packet);
    }

    public static class TimedPacket {
        private final IPacket<?> packet;
        private final long time;

        public TimedPacket(final IPacket<?> packet, final long time) {
            this.packet = packet;
            this.time = time;
        }

        public IPacket<?> getPacket() {
            return packet;
        }

        public long getTime() {
            return time;
        }
    }
}

