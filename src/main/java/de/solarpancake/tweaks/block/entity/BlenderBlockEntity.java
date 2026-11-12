package de.solarpancake.tweaks.block.entity;

import de.solarpancake.tweaks.item.ModItems;
import de.solarpancake.tweaks.menu.BlenderMenu;
import de.solarpancake.tweaks.recipe.BlenderRecipe;
import de.solarpancake.tweaks.recipe.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

/**
 * Blender: works on its own like a furnace / brewing stand.
 * <p>
 * Slots: empty can + ingredient + crushed iced crystal -> result. As long as all three are present, the ingredient
 * matches a {@link BlenderRecipe} and the result fits into the output slot, it blends for {@link #BLEND_TIME} ticks
 * and then uses up one of each input.
 * <p>
 * Automation (hoppers, pipes, AE2): every input side (top + 4 sides) accepts all three inputs, each item only goes
 * into its own slot (non-recipe items are refused). The bottom side only gives out the result.
 */
public class BlenderBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int CAN_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;
    public static final int ICE_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    public static final int SLOT_COUNT = 4;

    public static final int DATA_PROGRESS = 0;
    public static final int DATA_COUNT = 1;
    /** Ticks for one drink (5 seconds). */
    public static final int BLEND_TIME = 100;

    private static final int[] SLOTS_FOR_INPUT = {CAN_SLOT, INGREDIENT_SLOT, ICE_SLOT};
    private static final int[] SLOTS_FOR_DOWN = {RESULT_SLOT};
    private static final Component DEFAULT_NAME = Component.translatable("container.solarpancakestweaks.blender");

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int progress;
    private final RecipeManager.CachedCheck<SingleRecipeInput, BlenderRecipe> quickCheck =
            RecipeManager.createCheck(ModRecipes.BLENDING_TYPE.get());

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return dataId == DATA_PROGRESS ? progress : 0;
        }

        @Override
        public void set(int dataId, int value) {
            if (dataId == DATA_PROGRESS) {
                progress = value;
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public BlenderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLENDER.get(), pos, state);
    }

    /* ---------- blending ---------- */

    public static void serverTick(Level level, BlockPos pos, BlockState state, BlenderBlockEntity blender) {
        ItemStack result = blender.findResult((ServerLevel) level);
        int oldProgress = blender.progress;

        if (result.isEmpty() || !blender.canOutput(result)) {
            blender.progress = 0;
        } else if (++blender.progress >= BLEND_TIME) {
            blender.progress = 0;
            blender.blend(result);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.2f);
        }

        if (oldProgress != blender.progress) {
            setChanged(level, pos, state);
        }
    }

    /** The result of the current inputs, or EMPTY if something is missing / no recipe matches. */
    private ItemStack findResult(ServerLevel level) {
        if (items.get(CAN_SLOT).isEmpty() || items.get(ICE_SLOT).isEmpty() || items.get(INGREDIENT_SLOT).isEmpty()) {
            return ItemStack.EMPTY;
        }
        SingleRecipeInput input = new SingleRecipeInput(items.get(INGREDIENT_SLOT));
        Optional<RecipeHolder<BlenderRecipe>> recipe = quickCheck.getRecipeFor(input, level);
        return recipe.map(holder -> holder.value().assemble(input)).orElse(ItemStack.EMPTY);
    }

    private boolean canOutput(ItemStack result) {
        ItemStack current = items.get(RESULT_SLOT);
        if (current.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(current, result)
                && current.getCount() + result.getCount() <= Math.min(current.getMaxStackSize(), getMaxStackSize());
    }

    private void blend(ItemStack result) {
        ItemStack current = items.get(RESULT_SLOT);
        if (current.isEmpty()) {
            items.set(RESULT_SLOT, result.copy());
        } else {
            current.grow(result.getCount());
        }
        items.get(CAN_SLOT).shrink(1);
        items.get(ICE_SLOT).shrink(1);

        // ingredient: keep its crafting remainder (e.g. a bucket), like the brewing stand does
        ItemStack ingredient = items.get(INGREDIENT_SLOT);
        ItemStackTemplate remainder = ingredient.getCraftingRemainder();
        ingredient.shrink(1);
        if (remainder != null) {
            if (ingredient.isEmpty()) {
                items.set(INGREDIENT_SLOT, remainder.create());
            } else if (level != null) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), remainder.create());
            }
        }
    }

    /** True if some blending recipe takes this item (server only, the client has no recipes). */
    private boolean isBlendingIngredient(ItemStack stack) {
        return level instanceof ServerLevel serverLevel
                && quickCheck.getRecipeFor(new SingleRecipeInput(stack), serverLevel).isPresent();
    }

    /* ---------- slot rules ---------- */

    /** Which item may go into which slot. The ingredient slot takes anything that isn't a can or ice. */
    public static boolean isValidForSlot(int slot, ItemStack stack) {
        boolean can = stack.is(ModItems.EMPTY_CAN.get());
        boolean ice = stack.is(ModItems.CRUSHED_ICED_CRYSTAL.get());
        return switch (slot) {
            case CAN_SLOT -> can;
            case ICE_SLOT -> ice;
            case INGREDIENT_SLOT -> !can && !ice;
            default -> false;
        };
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return isValidForSlot(slot, stack);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_INPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        // hoppers/pipes only push real recipe ingredients, so junk never clogs the ingredient slot
        return canPlaceItem(slot, stack) && (slot != INGREDIENT_SLOT || isBlendingIngredient(stack));
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == RESULT_SLOT;
    }

    /* ---------- container boilerplate ---------- */

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BlenderMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
        progress = input.getShortOr("BlendTime", (short) 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putShort("BlendTime", (short) progress);
    }
}
