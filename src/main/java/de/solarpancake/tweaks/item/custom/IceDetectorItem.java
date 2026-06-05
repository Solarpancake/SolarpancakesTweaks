package de.solarpancake.tweaks.item.custom;

import de.solarpancake.tweaks.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IceDetectorItem extends Item {

    // 3x3 chunks = 48x48 blocks, so radius = 24 blocks from center
    private static final int CHUNK_SEARCH_RADIUS = 24;

    public IceDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos positionClicked = context.getClickedPos();
        Player player = context.getPlayer();

        if (!level.isClientSide()) {
            BlockPos nearest = findNearestValuableBlock(level, positionClicked);

            if (nearest != null) {
                BlockState foundState = level.getBlockState(nearest);
                outputValuableCoordinates(nearest, player, foundState.getBlock());
                context.getItemInHand().hurtAndBreak(1, player, context.getHand());
                level.playSound(null, positionClicked, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.5f, 1f);
                spawnFoundParticles(level, positionClicked, foundState);
            } else {
                outputNoValuablesFound(player);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private BlockPos findNearestValuableBlock(Level level, BlockPos origin) {
        List<BlockPos> found = new ArrayList<>();

        int minX = origin.getX() - CHUNK_SEARCH_RADIUS;
        int maxX = origin.getX() + CHUNK_SEARCH_RADIUS;
        int minZ = origin.getZ() - CHUNK_SEARCH_RADIUS;
        int maxZ = origin.getZ() + CHUNK_SEARCH_RADIUS;
        int minY = level.getMinY();
        int maxY = level.getMaxY();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (isValuableBlock(level.getBlockState(pos))) {
                        found.add(pos.immutable());
                    }
                }
            }
        }

        // Return the closest match to where the player clicked
        return found.stream()
                .min(Comparator.comparingDouble(pos -> pos.distSqr(origin)))
                .orElse(null);
    }

    private void spawnFoundParticles(Level level, BlockPos positionClicked, BlockState blockState) {
        ServerLevel serverLevel = (ServerLevel) level;
        for (int i = 0; i < 20; i++) {
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                    positionClicked.getX() + 0.5d, positionClicked.getY() + 1, positionClicked.getZ() + 0.5d, 1,
                    Math.cos(i * 18) * 0.15d, 0.15d, Math.sin(i * 18) * 0.15d, 0.1);
        }
    }

    private void outputNoValuablesFound(Player player) {
        player.sendSystemMessage(Component.translatable("item.solarpancakestweaks.ice_detector.no_valuables"));
    }

    private void outputValuableCoordinates(BlockPos position, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found: ").append(block.getName())
                .append(Component.literal(" at: (" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")")));
    }

    private boolean isValuableBlock(BlockState blockState) {
        return blockState.is(ModBlocks.BUDDING_ICED_CRYSTAL);
    }
}