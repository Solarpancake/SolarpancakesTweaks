package de.solarpancake.tweaks.cattree;

import de.solarpancake.tweaks.attachment.ModAttachments;
import de.solarpancake.tweaks.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.Level;

public final class CatTreeBinding {
    public static final int RADIUS = 5;

    private CatTreeBinding() {
    }

    public static void bind(Cat cat, BlockPos base) {
        cat.removeLeash();
        cat.setOrderedToSit(false);
        cat.setInSittingPose(false);
        cat.getNavigation().stop();
        cat.setHomeTo(base, RADIUS);
        cat.setData(ModAttachments.CAT_TREE_BASE, base.immutable());
    }

    public static void unbind(Cat cat) {
        cat.removeData(ModAttachments.CAT_TREE_BASE);

        if (!cat.isLeashed()) {
            cat.clearHome();
        }
    }

    public static boolean isBound(Cat cat) {
        BlockPos base = cat.getExistingDataOrNull(ModAttachments.CAT_TREE_BASE);
        if (base == null) {
            return false;
        }

        Level level = cat.level();
        boolean baseGone = level.isLoaded(base) && !level.getBlockState(base).is(ModBlocks.CAT_TREE_1.get());
        if (cat.isLeashed() || baseGone) {
            if (!level.isClientSide()) {
                unbind(cat);
            }
            return false;
        }
        return true;
    }
}
