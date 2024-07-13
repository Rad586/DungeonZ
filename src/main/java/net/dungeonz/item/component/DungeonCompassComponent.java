package net.dungeonz.item.component;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.BlockPos;

public record DungeonCompassComponent(String dungeonType, boolean hasDungeon, Optional<BlockPos> dungeonPos) {

    public static final DungeonCompassComponent DEFAULT = new DungeonCompassComponent("", false, Optional.of(BlockPos.ofFloored(0D, 0D, 0D)));

    public static final Codec<DungeonCompassComponent> CODEC = RecordCodecBuilder
            .create(instance -> instance.group(Codec.STRING.fieldOf("dungeon_type").forGetter(DungeonCompassComponent::dungeonType),
                    Codec.BOOL.fieldOf("has_dungeon").forGetter(DungeonCompassComponent::hasDungeon), BlockPos.CODEC.optionalFieldOf("dungeon_pos").forGetter(DungeonCompassComponent::dungeonPos))
                    .apply(instance, DungeonCompassComponent::new));

    public static final PacketCodec<ByteBuf, DungeonCompassComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, DungeonCompassComponent::dungeonType, PacketCodecs.BOOL,
            DungeonCompassComponent::hasDungeon, BlockPos.PACKET_CODEC.collect(PacketCodecs::optional), DungeonCompassComponent::dungeonPos, DungeonCompassComponent::new);

}
