package net.dungeonz.mixin;

import com.mojang.authlib.GameProfile;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.dungeonz.access.ServerPlayerAccess;
import net.dungeonz.dimension.DungeonPlacementHandler;
import net.dungeonz.init.DimensionInit;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {

    @Shadow
    @Mutable
    @Final
    public MinecraftServer server;

    private BlockPos dungeonPortalBlockPos = new BlockPos(0, 0, 0);
    private BlockPos dungeonSpawnBlockPos = new BlockPos(0, 0, 0);
    private ServerWorld oldWorld = null;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtMixin(NbtCompound nbt, CallbackInfo info) {
        this.dungeonPortalBlockPos = new BlockPos(nbt.getInt("DungeonPortalBlockPosX"), nbt.getInt("DungeonPortalBlockPosY"), nbt.getInt("DungeonPortalBlockPosZ"));
        this.dungeonSpawnBlockPos = new BlockPos(nbt.getInt("DungeonSpawnBlockPosX"), nbt.getInt("DungeonSpawnBlockPosY"), nbt.getInt("DungeonSpawnBlockPosZ"));
        if (nbt.contains("DungeonRegistryKey")) {
            this.oldWorld = this.server.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(nbt.getString("DungeonRegistryKey"))));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtMixin(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("DungeonPortalBlockPosX", this.dungeonPortalBlockPos.getX());
        nbt.putInt("DungeonPortalBlockPosY", this.dungeonPortalBlockPos.getY());
        nbt.putInt("DungeonPortalBlockPosZ", this.dungeonPortalBlockPos.getZ());

        nbt.putInt("DungeonSpawnBlockPosX", this.dungeonSpawnBlockPos.getX());
        nbt.putInt("DungeonSpawnBlockPosY", this.dungeonSpawnBlockPos.getY());
        nbt.putInt("DungeonSpawnBlockPosZ", this.dungeonSpawnBlockPos.getZ());

        if (this.oldWorld != null) {
            nbt.putString("DungeonRegistryKey", this.oldWorld.getRegistryKey().getValue().toString());
        }
    }

    @Inject(method = "onDisconnect", at = @At("TAIL"))
    private void onDisconnectMixin(CallbackInfo info) {
        if (this.world.getRegistryKey() == DimensionInit.DUNGEON_WORLD) {
            ServerWorld oldWorld = this.getOldServerWorld();
            if (oldWorld != null) {
                FabricDimensions.teleport(this, oldWorld, DungeonPlacementHandler.leave((ServerPlayerEntity) (Object) this, oldWorld));
            }
        }
    }

    @Override
    public void setDungeonInfo(ServerWorld world, BlockPos portalPos, BlockPos playerPos) {
        this.dungeonPortalBlockPos = new BlockPos(portalPos);
        this.dungeonSpawnBlockPos = new BlockPos(playerPos);
        this.oldWorld = world;
    }

    @Nullable
    @Override
    public ServerWorld getOldServerWorld() {
        return this.oldWorld;
    }

    @Override
    public BlockPos getDungeonPortalBlockPos() {
        return this.dungeonPortalBlockPos;
    }

    @Override
    public BlockPos getDungeonSpawnBlockPos() {
        return this.dungeonSpawnBlockPos;
    }

}
