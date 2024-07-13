package net.dungeonz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record DungeonEffectPacket(BlockPos portalBlockPos, boolean disableEffects) implements CustomPayload {

    public static final CustomPayload.Id<DungeonEffectPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("dungeonz", "dungeon_effect_packet"));

    public static final PacketCodec<RegistryByteBuf, DungeonEffectPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeBlockPos(value.portalBlockPos);
        buf.writeBoolean(value.disableEffects);
    }, buf -> new DungeonEffectPacket(buf.readBlockPos(), buf.readBoolean()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
