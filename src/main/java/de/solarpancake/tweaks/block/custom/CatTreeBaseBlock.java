package de.solarpancake.tweaks.block.custom;

import de.solarpancake.tweaks.cattree.CatTreeBinding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class CatTreeBaseBlock extends CatTreePostBlock {
    private static final double LEASHED_CAT_SEARCH_RANGE = 12.0;

    public CatTreeBaseBlock(Properties properties) {
        super(properties, Direction.UP);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        List<Cat> cats = level.getEntitiesOfClass(Cat.class, new AABB(pos).inflate(LEASHED_CAT_SEARCH_RANGE),
                cat -> cat.getLeashHolder() == player);
        if (cats.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            for (Cat cat : cats) {
                CatTreeBinding.bind(cat, pos);
                if (!player.hasInfiniteMaterials()) {
                    player.getInventory().placeItemBackInInventory(new ItemStack(Items.LEAD));
                }
            }
            player.sendOverlayMessage(Component.translatable("message.solarpancakestweaks.cat_tree.bound", cats.size()));
        }
        return InteractionResult.SUCCESS;
    }
}
