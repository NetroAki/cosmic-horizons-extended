package com.netroaki.chex.menu;

import com.netroaki.chex.item.library.LibraryBookItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class LibraryBookMenu extends AbstractContainerMenu {
  private static final int SLOT_SIZE = 18;
  private static final int INVENTORY_ROWS = 3;
  private static final int INVENTORY_COLUMNS = 9;
  private static final int HOTBAR_SLOTS = 9;

  private final ItemStack bookStack;
  private final IItemHandler playerInventory;

  public LibraryBookMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
    this(windowId, playerInventory, playerInventory.player.getInventory().getItem(data.readInt()));
  }

  public LibraryBookMenu(int windowId, Inventory playerInventory, ItemStack bookStack) {
    // super(CHEXMenuTypes.LIBRARY_BOOK.get(), windowId); // TODO: Fix when
    // LIBRARY_BOOK is implemented
    super(null, windowId); // Placeholder
    this.bookStack = bookStack;
    this.playerInventory = new ItemStackHandler(playerInventory.getContainerSize());

    // Add player inventory
    addPlayerInventory(playerInventory);
  }

  private void addPlayerInventory(Inventory playerInventory) {
    // Main inventory
    for (int row = 0; row < INVENTORY_ROWS; ++row) {
      for (int col = 0; col < INVENTORY_COLUMNS; ++col) {
        int x = 8 + col * SLOT_SIZE;
        int y = 84 + row * SLOT_SIZE;
        int slotIndex = (row + 1) * INVENTORY_COLUMNS + col;
        addSlot(new SlotItemHandler(this.playerInventory, slotIndex, x, y));
      }
    }

    // Hotbar
    for (int col = 0; col < HOTBAR_SLOTS; ++col) {
      int x = 8 + col * SLOT_SIZE;
      int y = 142;
      addSlot(new SlotItemHandler(this.playerInventory, col, x, y));
    }
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return !this.bookStack.isEmpty() && this.bookStack.getItem() instanceof LibraryBookItem;
  }

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      if (index < INVENTORY_COLUMNS * INVENTORY_ROWS + HOTBAR_SLOTS) {
        if (!this.moveItemStackTo(
            itemstack1,
            INVENTORY_COLUMNS * INVENTORY_ROWS + HOTBAR_SLOTS,
            this.slots.size(),
            false)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(
          itemstack1, 0, INVENTORY_COLUMNS * INVENTORY_ROWS + HOTBAR_SLOTS, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }

    return itemstack;
  }

  public ItemStack getBook() {
    return this.bookStack;
  }
}
