package net.dungeonz.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.dungeonz.DungeonzMain;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

public class Dungeon {

    private final String dungeonTypeId;

    private final HashMap<Integer, List<EntityType<?>>> blockIdEntityMap;
    private final HashMap<Integer, Float> blockIdEntitySpawnChance;
    private final HashMap<Integer, Integer> blockIdBlockReplacement;

    private final HashMap<String, Float> difficultyMobModificator;
    private final HashMap<String, List<String>> difficultyLootTableIds;
    private final HashMap<String, Float> difficultyBossModificator;
    private final HashMap<String, String> difficultyBossLootTable;

    private final EntityType<?> bossEntityType;
    private final int bossBlockId;
    private final int bossLootBlockId;

    private final int exitBlockId;

    private final int maxGroupSize;
    private final int cooldown;

    private final Identifier dungeonStructurePoolId;

    public Dungeon(String dungeonTypeId, HashMap<Integer, List<EntityType<?>>> blockIdEntityMap, HashMap<Integer, Float> blockIdEntitySpawnChance, HashMap<Integer, Integer> blockIdBlockReplacement,
            HashMap<String, Float> difficultyMobModificator, HashMap<String, List<String>> difficultyLootTableIds, HashMap<String, Float> difficultyBossModificator,
            HashMap<String, String> difficultyBossLootTable, EntityType<?> bossEntityType, int bossBlockId, int bossLootBlockId, int exitBlockId, int maxGroupSize, int cooldown,
            Identifier dungeonStructurePoolId) {
        this.dungeonTypeId = dungeonTypeId;
        this.blockIdEntityMap = blockIdEntityMap;
        this.blockIdEntitySpawnChance = blockIdEntitySpawnChance;
        this.blockIdBlockReplacement = blockIdBlockReplacement;
        this.difficultyMobModificator = difficultyMobModificator;
        this.difficultyLootTableIds = difficultyLootTableIds;
        this.difficultyBossModificator = difficultyBossModificator;
        this.difficultyBossLootTable = difficultyBossLootTable;
        this.bossEntityType = bossEntityType;
        this.bossBlockId = bossBlockId;
        this.bossLootBlockId = bossLootBlockId;
        this.exitBlockId = exitBlockId;
        this.maxGroupSize = maxGroupSize;
        this.cooldown = cooldown;
        this.dungeonStructurePoolId = dungeonStructurePoolId;
    }

    public String getDungeonTypeId() {
        return this.dungeonTypeId;
    }

    public Identifier getStructurePoolId() {
        return this.dungeonStructurePoolId;
    }

    public HashMap<Integer, List<EntityType<?>>> getBlockIdEntityMap() {
        return this.blockIdEntityMap;
    }

    public HashMap<Integer, Float> getBlockIdEntitySpawnChanceMap() {
        return this.blockIdEntitySpawnChance;
    }

    public HashMap<Integer, Integer> getBlockIdBlockReplacementMap() {
        return this.blockIdBlockReplacement;
    }

    public HashMap<String, Float> getDifficultyMobModificatorMap() {
        return this.difficultyMobModificator;
    }

    public HashMap<String, List<String>> getDifficultyLootTableIdMap() {
        return this.difficultyLootTableIds;
    }

    public HashMap<String, Float> getDifficultyBossModificatorMap() {
        return this.difficultyBossModificator;
    }

    public HashMap<String, String> getDifficultyBossLootTableMap() {
        return this.difficultyBossLootTable;
    }

    public EntityType<?> getBossEntityType() {
        return this.bossEntityType;
    }

    public int getBossBlockId() {
        return this.bossBlockId;
    }

    public int getBossLootBlockId() {
        return this.bossLootBlockId;
    }

    public int getExitBlockId() {
        return this.exitBlockId;
    }

    public int getMaxGroupSize() {
        return this.maxGroupSize;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public List<String> getDifficultyList() {
        return new ArrayList<>(this.difficultyMobModificator.keySet());
    }

    public boolean containsBlockId(int blockId) {
        if (this.blockIdEntityMap.containsKey(blockId)) {
            return true;
        }
        return false;
    }

    public static void addDungeon(Dungeon dungeon) {
        if (!DungeonzMain.dungeons.contains(dungeon)) {
            DungeonzMain.dungeons.add(dungeon);
        }
    }

    @Nullable
    public static Dungeon getDungeon(String dungeonTypeId) {
        for (int i = 0; i < DungeonzMain.dungeons.size(); i++) {
            if (DungeonzMain.dungeons.get(i).getDungeonTypeId().equals(dungeonTypeId)) {
                return DungeonzMain.dungeons.get(i);
            }
        }
        return null;
    }

}
