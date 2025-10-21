package com.netroaki.chex.world.library.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class LibraryBookItem extends Item {
  private final BookType bookType;

  public LibraryBookItem(BookType bookType) {
    super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    this.bookType = bookType;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);

    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      // Open the book GUI
      NetworkHooks.openScreen(
          serverPlayer,
          new LibraryBookMenuProvider(getBookId(stack)),
          buf -> {
            buf.writeItem(stack);
            buf.writeEnum(bookType);
          });
    }

    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);

    // Add lore or description based on book type
    switch (bookType) {
      case LORE -> tooltip.add(
          Component.translatable("item.cosmic_horizons.library_book.lore.desc")
              .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
      case KNOWLEDGE -> tooltip.add(
          Component.translatable("item.cosmic_horizons.library_book.knowledge.desc")
              .withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
      case SECRET -> tooltip.add(
          Component.translatable("item.cosmic_horizons.library_book.secret.desc")
              .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
    }
  }

  public BookType getBookType() {
    return bookType;
  }

  public static UUID getBookId(ItemStack stack) {
    if (!stack.hasTag() || !stack.getTag().contains("BookId")) {
      UUID id = UUID.randomUUID();
      stack.getOrCreateTag().putUUID("BookId", id);
      return id;
    }
    return stack.getTag().getUUID("BookId");
  }

  public enum BookType {
    LORE,
    KNOWLEDGE,
    SECRET
  }
}
