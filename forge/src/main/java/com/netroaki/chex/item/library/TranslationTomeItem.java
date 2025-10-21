package com.netroaki.chex.item.library;

// import com.netroaki.chex.world.library.translation.AncientTextTranslator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * A special tome that can be used to translate ancient texts. Consumes durability with each use.
 */
public class TranslationTomeItem extends Item {
  private static final int MAX_DURABILITY = 128;

  public TranslationTomeItem(Properties properties) {
    super(properties.durability(MAX_DURABILITY));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    ItemStack offhand = player.getOffhandItem();

    // Check if holding a book in the offhand
    if (offhand.getItem() instanceof WrittenBookItem) {
      // TODO: Implement AncientTextTranslator
      // if (AncientTextTranslator.tryTranslateBook(player, offhand)) {
      // Successfully translated or already translated
      if (!level.isClientSide) {
        // Damage the tome
        itemStack.hurt(1, level.random, (ServerPlayer) player);

        // Play translation sound
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.ENCHANTMENT_TABLE_USE,
            SoundSource.PLAYERS,
            1.0F,
            1.0F);

        // Award stat
        player.awardStat(Stats.ITEM_USED.get(this));
      }
      return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
      // }
    } else {
      // Show translation knowledge if not holding a book
      if (!level.isClientSide) {
        showTranslationKnowledge((ServerPlayer) player);
      }
      return InteractionResultHolder.consume(itemStack);
    }

    // return InteractionResultHolder.pass(itemStack); // TODO: Fix unreachable
    // statement
  }

  private void showTranslationKnowledge(ServerPlayer player) {
    player.sendSystemMessage(Component.literal("§b=== Translation Knowledge ==="));

    // TODO: Implement AncientTextTranslator
    // for (AncientTextTranslator.AncientLanguage language :
    // AncientTextTranslator.getLanguages()) {
    // float progress = AncientTextTranslator.getTranslationProgress(player,
    // language.getId());
    // boolean canTranslate = AncientTextTranslator.canTranslate(player,
    // language.getId());
    //
    // String status = canTranslate ? "§a[READY]" : "§7[IN PROGRESS]";
    // String progressBar = createProgressBar(progress, 20);
    //
    // player.sendSystemMessage(
    // Component.literal(
    // String.format(
    // "%s %s - %s %s",
    // language.getDisplayName(),
    // status,
    // progressBar,
    // String.format("%.0f%%", progress * 100))));
    // }
    player.sendSystemMessage(Component.literal("§7Translation system not yet implemented"));
  }

  private String createProgressBar(float progress, int length) {
    int filled = (int) (progress * length);
    int empty = length - filled;

    return "§2" + "|".repeat(filled) + "§7" + "|".repeat(empty);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(
        Component.translatable("item.chex.translation_tome.desc").withStyle(ChatFormatting.GRAY));
    tooltip.add(
        Component.translatable("item.chex.translation_tome.usage")
            .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return false;
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return false;
  }

  @Override
  public boolean isRepairable(ItemStack stack) {
    return false;
  }
}
