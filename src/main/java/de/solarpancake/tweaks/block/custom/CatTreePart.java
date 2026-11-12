package de.solarpancake.tweaks.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface CatTreePart {
    boolean hasOpening(BlockState state, Direction side);
}
