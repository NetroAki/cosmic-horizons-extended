package com.netroaki.chex.world.library.item;

import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class LibraryBookMenu extends AbstractContainerMenu {
  private static final int CONTAINER_SIZE = 1;
  private final Container container;
  private final UUID bookId;
  private final LibraryBookItem.BookType bookType;
  private final Player player;

  public LibraryBookMenu(
      int containerId, Inventory playerInventory, UUID bookId, LibraryBookItem.BookType bookType) {
    this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE), bookId, bookType);
  }

  public LibraryBookMenu(int containerId, Inventory playerInventory, FriendlyByteBuf buf) {
    this(
        containerId,
        playerInventory,
        new SimpleContainer(CONTAINER_SIZE),
        buf.readUUID(),
        buf.readEnum(LibraryBookItem.BookType.class));
  }

  public LibraryBookMenu(
      int containerId,
      Inventory playerInventory,
      Container container,
      UUID bookId,
      LibraryBookItem.BookType bookType) {
    super(CHEXMenuTypes.LIBRARY_BOOK.get(), containerId);
    this.container = container;
    this.bookId = bookId;
    this.bookType = bookType;
    this.player = playerInventory.player;

    // Add slots for the book interface
    this.addSlot(
        new Slot(container, 0, 80, 20) {
          @Override
          public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof LibraryBookItem;
          }
        });

    // Add player inventory
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    // Add hotbar
    for (int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);

    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();

      if (index < CONTAINER_SIZE) {
        if (!this.moveItemStackTo(itemstack1, CONTAINER_SIZE, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, CONTAINER_SIZE, false)) {
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

  @Override
  public void removed(Player player) {
    super.removed(player);
    if (!player.level().isClientSide) {
      // Handle any cleanup when the menu is closed
    }
  }

  public UUID getBookId() {
    return bookId;
  }

  public LibraryBookItem.BookType getBookType() {
    return bookType;
  }

  public Player getPlayer() {
    return player;
  }
}
