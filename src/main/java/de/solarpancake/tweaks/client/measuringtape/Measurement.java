package de.solarpancake.tweaks.client.measuringtape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

/**
 * A finished measurement between two block positions. Sizes are inclusive block counts,
 * so measuring a single block gives 1 x 1 x 1.
 */
public record Measurement(BlockPos pos1, BlockPos pos2) {
    public int sizeX() {
        return Math.abs(pos1.getX() - pos2.getX()) + 1;
    }

    public int sizeY() {
        return Math.abs(pos1.getY() - pos2.getY()) + 1;
    }

    public int sizeZ() {
        return Math.abs(pos1.getZ() - pos2.getZ()) + 1;
    }

    public long volume() {
        return (long) sizeX() * sizeY() * sizeZ();
    }

    /** Box that encloses both blocks completely. */
    public AABB box() {
        return AABB.encapsulatingFullBlocks(pos1, pos2);
    }
}
