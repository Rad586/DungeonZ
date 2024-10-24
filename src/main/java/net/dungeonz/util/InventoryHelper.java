package net.dungeonz.util;

import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class InventoryHelper {

    public static void fillInventoryWithLoot(MinecraftServer server, ServerWorld world, BlockPos pos, String lootTableString, boolean luck) {
        // Clear inventory
        ((Inventory) world.getBlockEntity(pos)).clear();
        // Generate loot
        LootTable lootTable = server.getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(lootTableString)));
        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        if (luck) {
            builder.luck(1.1f);
        }
        lootTable.supplyInventory((Inventory) world.getBlockEntity(pos), builder.build(LootContextTypes.CHEST), world.getRandom().nextLong());
    }

    public static boolean hasRequiredItemStacks(PlayerInventory playerInventory, List<ItemStack> requiredItemStacks) {
        if (playerInventory.player.isCreative()) {
            return true;
        }
        for (int i = 0; i < requiredItemStacks.size(); i++) {
            int requiredCount = requiredItemStacks.get(i).getCount();
            for (int u = 0; u < playerInventory.main.size(); u++) {
                if (ItemStack.areItemsEqual(playerInventory.main.get(u), requiredItemStacks.get(i))) {
                    requiredCount -= playerInventory.main.get(u).getCount();
                    if (requiredCount <= 0) {
                        break;
                    }
                }
            }
            if (requiredCount > 0) {
                return false;
            }
        }
        return true;
    }

    public static void decrementRequiredItemStacks(PlayerInventory playerInventory, List<ItemStack> requiredItemStacks) {
        if (!requiredItemStacks.isEmpty() && !playerInventory.player.isCreative()) {
            for (int i = 0; i < requiredItemStacks.size(); i++) {
                int requiredCount = requiredItemStacks.get(i).getCount();
                for (int u = 0; u < playerInventory.main.size(); u++) {
                    if (ItemStack.areItemsEqual(playerInventory.main.get(u), requiredItemStacks.get(i))) {
                        if (playerInventory.main.get(u).getCount() >= requiredCount) {
                            playerInventory.main.get(u).decrement(requiredCount);
                            break;
                        }
                        requiredCount -= playerInventory.main.get(u).getCount();
                        playerInventory.main.get(u).setCount(0);

                        if (requiredCount <= 0) {
                            break;
                        }
                    }
                }
            }
        }
    }

}
