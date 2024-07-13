package net.dungeonz.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.dungeonz.block.entity.DungeonGateEntity;
import net.dungeonz.init.BlockInit;
import net.dungeonz.init.ConfigInit;
import net.dungeonz.network.DungeonServerPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DungeonGateBlock extends BlockWithEntity {

    public static BooleanProperty ENABLED = Properties.ENABLED;
    public static final MapCodec<DungeonGateBlock> CODEC = DungeonGateBlock.createCodec(DungeonGateBlock::new);

    public DungeonGateBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ENABLED, true));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DungeonGateEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return DungeonGateBlock.validateTicker(type, BlockInit.DUNGEON_GATE_ENTITY, world.isClient() ? null : DungeonGateEntity::serverTick);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getWorld().getBlockEntity(pos) != null && player.getWorld().getBlockEntity(pos) instanceof DungeonGateEntity) {
            DungeonGateEntity dungeonGateEntity = (DungeonGateEntity) player.getWorld().getBlockEntity(pos);
            if (player.isCreativeLevelTwoOp()) {
                if (!player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() instanceof BlockItem) {
                    dungeonGateEntity.setBlockId(Registries.BLOCK.getId(((BlockItem) player.getStackInHand(hand).getItem()).getBlock()));
                    dungeonGateEntity.markDirty();
                } else if (player.isSneaking()) {
                    if (!world.isClient()) {
                        DungeonServerPacket.writeS2COpenOpScreenPacket((ServerPlayerEntity) player, null, dungeonGateEntity);
                    }
                }
                return ItemActionResult.success(world.isClient());
            } else if (dungeonGateEntity.getUnlockItem() != null && player.getStackInHand(hand).isOf(dungeonGateEntity.getUnlockItem())) {
                if (!world.isClient()) {
                    if (!player.isCreative()) {
                        player.getStackInHand(hand).decrement(1);
                    }
                    dungeonGateEntity.unlockGate(pos);
                }
                return ItemActionResult.success(world.isClient());
            }

        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ENABLED) && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof DungeonGateEntity
                && ((DungeonGateEntity) world.getBlockEntity(pos)).getParticleEffect() != null) {
            ParticleUtil.spawnParticle(world, pos, ((DungeonGateEntity) world.getBlockEntity(pos)).getParticleEffect(), UniformIntProvider.create(0, 1));
        }
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        if (!state.get(ENABLED)) {
            return true;
        }
        return super.isTransparent(state, world, pos);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        if (state.get(ENABLED)) {
            return false;
        }
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(ENABLED) && !ConfigInit.CONFIG.devMode) {
            return VoxelShapes.empty();
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(ENABLED)) {
            return VoxelShapes.empty();
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

}
