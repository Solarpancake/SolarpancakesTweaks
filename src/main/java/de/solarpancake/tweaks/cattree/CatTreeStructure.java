package de.solarpancake.tweaks.cattree;

import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.block.custom.CatTreePart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class CatTreeStructure {
    private static final int MAX_PIECES = 512;

    private CatTreeStructure() {
    }

    public static List<BlockPos> findTops(Level level, BlockPos base) {
        List<BlockPos> tops = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        visited.add(base);
        queue.add(base);

        while (!queue.isEmpty() && visited.size() <= MAX_PIECES) {
            BlockPos pos = queue.poll();
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof CatTreePart part)) {
                continue;
            }
            if (state.is(ModBlocks.CAT_TREE_4.get())) {
                tops.add(pos);
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighbourPos = pos.relative(direction);
                if (visited.contains(neighbourPos) || !level.isLoaded(neighbourPos)) {
                    continue;
                }
                BlockState neighbour = level.getBlockState(neighbourPos);
                if (neighbour.getBlock() instanceof CatTreePart neighbourPart
                        && (part.hasOpening(state, direction) || neighbourPart.hasOpening(neighbour, direction.getOpposite()))) {
                    visited.add(neighbourPos);
                    queue.add(neighbourPos);
                }
            }
        }
        return tops;
    }
}
