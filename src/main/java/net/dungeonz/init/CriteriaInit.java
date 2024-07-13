package net.dungeonz.init;

import net.dungeonz.criteria.DungeonBossCriterion;
import net.minecraft.advancement.criterion.Criteria;

public class CriteriaInit {

    public static final DungeonBossCriterion DUNGEON_COMPLETION = Criteria.register("dungeonz:dungeon_completion", new DungeonBossCriterion());

    public static void init() {
    }
}
