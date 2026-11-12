package de.solarpancake.tweaks.menu;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.entity.BlenderBlockEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import static de.solarpancake.tweaks.block.entity.BlenderBlockEntity.CAN_SLOT;
import static de.solarpancake.tweaks.block.entity.BlenderBlockEntity.ICE_SLOT;
import static de.solarpancake.tweaks.block.entity.BlenderBlockEntity.INGREDIENT_SLOT;
import static de.solarpancake.tweaks.block.entity.BlenderBlockEntity.RESULT_SLOT;
import static de.solarpancake.tweaks.block.entity.BlenderBlockEntity.SLOT_COUNT;

/**
 * Blender GUI, laid out like a brewing stand: ingredient on top, can on the left, ice on the right,
 * all three feed into the result slot at the bottom.
 */
public class BlenderMenu extends AbstractContainerMenu {
    /* slot positions (item corner), also used by BlenderScreen */
    public static final int INGREDIENT_X = 80, INGREDIENT_Y = 17;
    public static final int CAN_X = 44, CAN_Y = 36;
    public static final int ICE_X = 116, ICE_Y = 36;
    public static final int RESULT_X = 80, RESULT_Y = 57;

    private static final Identifier EMPTY_SLOT_CAN = Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "container/slot/empty_can");
    private static final Identifier EMPTY_SLOT_ICE = Identifier.fromNamespaceAndPath(Solarpancakestweaks.MODID, "container/slot/crushed_ice");

    private static final int INV_START = SLOT_COUNT;
    private static final int INV_END = INV_START + 27;
    private static final int HOTBAR_END = INV_END + 9;

    private final Container blender;
    private final ContainerData data;

    /** Client side: contents get synced from the server. */
    public BlenderMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(BlenderBlockEntity.DATA_COUNT));
    }

    public BlenderMenu(int containerId, Inventory inventory, Container blender, ContainerData data) {
        super(ModMenuTypes.BLENDER_MENU.get(), containerId);
        checkContainerSize(blender, SLOT_COUNT);
        checkContainerDataCount(data, BlenderBlockEntity.DATA_COUNT);
        this.blender = blender;
        this.data = data;

        // order must match the slot indices in BlenderBlockEntity
        addSlot(new InputSlot(blender, CAN_SLOT, CAN_X, CAN_Y, EMPTY_SLOT_CAN));
        addSlot(new InputSlot(blender, INGREDIENT_SLOT, INGREDIENT_X, INGREDIENT_Y, null));
        addSlot(new InputSlot(blender, ICE_SLOT, ICE_X, ICE_Y, EMPTY_SLOT_ICE));
        addSlot(new ResultSlot(blender, RESULT_SLOT, RESULT_X, RESULT_Y));
        addDataSlots(data);
        addStandardInventorySlots(inventory, 8, 84);
    }

    /** 0..1, how far the current drink is. */
    public float getProgress() {
        return Math.min(1f, data.get(BlenderBlockEntity.DATA_PROGRESS) / (float) BlenderBlockEntity.BLEND_TIME);
    }

    @Override
    public boolean stillValid(Player player) {
        return blender.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (slotIndex < SLOT_COUNT) {
            // blender -> player inventory
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (BlenderBlockEntity.isValidForSlot(CAN_SLOT, stack)) {
            if (!moveItemStackTo(stack, CAN_SLOT, CAN_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (BlenderBlockEntity.isValidForSlot(ICE_SLOT, stack)) {
            if (!moveItemStackTo(stack, ICE_SLOT, ICE_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, INGREDIENT_SLOT, INGREDIENT_SLOT + 1, false)) {
            // ingredient slot full/taken: shuffle between inventory and hotbar like vanilla
            boolean fromInventory = slotIndex < INV_END;
            if (!moveItemStackTo(stack, fromInventory ? INV_END : INV_START, fromInventory ? HOTBAR_END : INV_END, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, original);
        return original;
    }

    /** Input slot that only takes what belongs there, with an optional ghost icon while empty. */
    private static class InputSlot extends Slot {
        private final @Nullable Identifier emptyIcon;

        InputSlot(Container container, int slot, int x, int y, @Nullable Identifier emptyIcon) {
            super(container, slot, x, y);
            this.emptyIcon = emptyIcon;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return BlenderBlockEntity.isValidForSlot(getContainerSlot(), stack);
        }

        @Override
        public @Nullable Identifier getNoItemIcon() {
            return emptyIcon;
        }
    }

    /** Output only. */
    private static class ResultSlot extends Slot {
        ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack carried) {
            carried.onCraftedBy(player, carried.getCount());
            super.onTake(player, carried);
        }
    }
}
