package net.dungeonz.criteria;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class DungeonBossCriterion extends AbstractCriterion<DungeonBossCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, String dungeonType, String difficulty) {
        this.trigger(player, conditions -> conditions.matches(player, dungeonType, difficulty));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, String dungeonType, String difficulty) implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder
                .create(instance -> instance
                        .group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                Codec.STRING.fieldOf("dungeon_type").forGetter(Conditions::dungeonType), Codec.STRING.fieldOf("difficulty").forGetter(Conditions::difficulty))
                        .apply(instance, Conditions::new));

        public boolean matches(ServerPlayerEntity player, String dungeonType, String difficulty) {
            return this.dungeonType.equals(dungeonType) && this.difficulty.equals(difficulty);
        }

    }

}
