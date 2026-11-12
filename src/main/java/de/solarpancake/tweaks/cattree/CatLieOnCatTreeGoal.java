package de.solarpancake.tweaks.cattree;

import de.solarpancake.tweaks.attachment.ModAttachments;
import de.solarpancake.tweaks.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class CatLieOnCatTreeGoal extends Goal {
    private static final int START_CHANCE = 400;
    private static final double HOP_UP_DISTANCE = 2.5;
    private static final int MAX_WALK_TICKS = 200;
    private static final int MIN_LIE_TICKS = 1200;
    private static final int EXTRA_LIE_TICKS = 1200;

    private final Cat cat;
    private BlockPos top;
    private boolean onTop;
    private int walkTicks;
    private int lieTicksLeft;

    public CatLieOnCatTreeGoal(Cat cat) {
        this.cat = cat;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (cat.isOrderedToSit() || cat.isLying() || cat.isPassenger()
                || cat.getRandom().nextInt(reducedTickDelay(START_CHANCE)) != 0
                || !CatTreeBinding.isBound(cat)) {
            return false;
        }

        BlockPos base = cat.getExistingDataOrNull(ModAttachments.CAT_TREE_BASE);
        List<BlockPos> freeTops = CatTreeStructure.findTops(cat.level(), base).stream()
                .filter(this::isFree)
                .toList();
        if (freeTops.isEmpty()) {
            return false;
        }
        top = freeTops.get(cat.getRandom().nextInt(freeTops.size()));
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return top != null
                && !cat.isOrderedToSit()
                && CatTreeBinding.isBound(cat)
                && isFree(top)
                && (!onTop || lieTicksLeft > 0);
    }

    @Override
    public void start() {
        onTop = false;
        walkTicks = 0;
        lieTicksLeft = MIN_LIE_TICKS + cat.getRandom().nextInt(EXTRA_LIE_TICKS);
        cat.setInSittingPose(false);
        cat.getNavigation().moveTo(top.getX() + 0.5, top.getY() + 1, top.getZ() + 0.5, 1.1);
    }

    @Override
    public void stop() {
        cat.setLying(false);
        cat.getNavigation().stop();
        top = null;
        onTop = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        cat.setInSittingPose(false);

        if (onTop) {
            lieTicksLeft--;
            if (!cat.isLying()) {
                cat.setLying(true);
            }
            return;
        }

        walkTicks++;
        double dx = cat.getX() - (top.getX() + 0.5);
        double dz = cat.getZ() - (top.getZ() + 0.5);
        boolean closeEnough = dx * dx + dz * dz <= HOP_UP_DISTANCE * HOP_UP_DISTANCE;
        boolean stuck = cat.getNavigation().isDone() || walkTicks > MAX_WALK_TICKS;
        if (closeEnough || stuck) {
            cat.getNavigation().stop();
            cat.teleportTo(top.getX() + 0.5, top.getY() + 1, top.getZ() + 0.5);
            onTop = true;
            cat.setLying(true);
        }
    }

    private boolean isFree(BlockPos pos) {
        Level level = cat.level();
        return level.getBlockState(pos).is(ModBlocks.CAT_TREE_4.get())
                && level.isEmptyBlock(pos.above())
                && level.getEntitiesOfClass(Cat.class, new AABB(pos.above()), other -> other != cat).isEmpty();
    }
}
