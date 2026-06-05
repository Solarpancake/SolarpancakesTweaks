package de.solarpancake.tweaks.block.custom;

import com.mojang.serialization.MapCodec;
import de.solarpancake.tweaks.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingIcedCrystalBlock extends AmethystBlock {
    public static final MapCodec<BuddingIcedCrystalBlock> CODEC = simpleCodec(BuddingIcedCrystalBlock::new);
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public MapCodec<BuddingIcedCrystalBlock> codec() {
        return CODEC;
    }

    public BuddingIcedCrystalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            Direction growDirection = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos growPos = pos.relative(growDirection);
            BlockState relativeState = level.getBlockState(growPos);
            Block nextStage = null;

            if (canClusterGrowAtState(relativeState)) {
                nextStage = ModBlocks.SMALL_ICED_CRYSTAL_BUD.get();
            } else if (relativeState.is(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get())
                    && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
                nextStage = ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get();
            } else if (relativeState.is(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get())
                    && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
                nextStage = ModBlocks.LARGE_ICED_CRYSTAL_BUD.get();
            } else if (relativeState.is(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get())
                    && relativeState.getValue(AmethystClusterBlock.FACING) == growDirection) {
                nextStage = ModBlocks.ICED_CRYSTAL_CLUSTER.get();
            }

            if (nextStage != null) {
                BlockState targetState = nextStage.defaultBlockState()
                        .setValue(AmethystClusterBlock.FACING, growDirection)
                        .setValue(AmethystClusterBlock.WATERLOGGED,
                                relativeState.getFluidState().is(Fluids.WATER));
                level.setBlockAndUpdate(growPos, targetState);
            }
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().isFull();
    }
}
