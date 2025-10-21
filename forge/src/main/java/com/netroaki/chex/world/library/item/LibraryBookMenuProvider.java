package com.netroaki.chex.world.library.item;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class LibraryBookMenuProvider implements MenuProvider {
  private final UUID bookId;
  private final ItemStack bookStack;
  private final LibraryBookItem.BookType bookType;

  public LibraryBookMenuProvider(UUID bookId) {
    this.bookId = bookId;
    this.bookStack = ItemStack.EMPTY;
    this.bookType = LibraryBookItem.BookType.LORE;
  }

  public LibraryBookMenuProvider(ItemStack bookStack, LibraryBookItem.BookType bookType) {
    this.bookId = LibraryBookItem.getBookId(bookStack);
    this.bookStack = bookStack;
    this.bookType = bookType;
  }

  public static LibraryBookMenuProvider decode(FriendlyByteBuf buf) {
    ItemStack stack = buf.readItem();
    LibraryBookItem.BookType type = buf.readEnum(LibraryBookItem.BookType.class);
    return new LibraryBookMenuProvider(stack, type);
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("container.cosmic_horizons.library_book");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(
      int containerId, Inventory playerInventory, Player player) {
    return new LibraryBookMenu(containerId, playerInventory, bookId, bookType);
  }

  public void open(ServerPlayer player) {
    NetworkHooks.openScreen(
        player,
        this,
        buf -> {
          buf.writeItem(bookStack);
          buf.writeEnum(bookType);
        });
  }
}
