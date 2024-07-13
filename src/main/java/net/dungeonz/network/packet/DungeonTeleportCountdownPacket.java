package net.dungeonz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DungeonTeleportCountdownPacket(int countdownTicks) implements CustomPayload {

    public static final CustomPayload.Id<DungeonTeleportCountdownPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("dungeonz", "dungeon_teleport_countdown_packet"));

    public static final PacketCodec<RegistryByteBuf, DungeonTeleportCountdownPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.countdownTicks);
    }, buf -> new DungeonTeleportCountdownPacket(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
