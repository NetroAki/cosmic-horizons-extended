package com.netroaki.chex.item.library;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.items.CHEXItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LibraryBookItem extends Item {
  public static final Codec<BookContent> CONTENT_CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.STRING.fieldOf("title").forGetter(BookContent::getTitle),
                      Codec.STRING.fieldOf("author").forGetter(BookContent::getAuthor),
                      Codec.list(Codec.STRING).fieldOf("pages").forGetter(BookContent::getPages),
                      Codec.INT
                          .optionalFieldOf("generation", 0)
                          .forGetter(BookContent::getGeneration),
                      Codec.STRING
                          .optionalFieldOf("lore_fragment", "")
                          .forGetter(BookContent::getLoreFragment),
                      Codec.BOOL
                          .optionalFieldOf("is_translated", false)
                          .forGetter(BookContent::isTranslated),
                      Codec.STRING
                          .optionalFieldOf("original_language", "Ancient")
                          .forGetter(BookContent::getOriginalLanguage),
                      Codec.list(Codec.STRING)
                          .optionalFieldOf("discovered_by")
                          .forGetter(BookContent::getDiscoveredBy))
                  .apply(instance, BookContent::new));

  public LibraryBookItem(Properties properties) {
    super(properties);
  }

  public static ItemStack createBook(BookContent content) {
    ItemStack stack = new ItemStack(CHEXItems.LIBRARY_BOOK.get());
    CompoundTag tag = new CompoundTag();
    CONTENT_CODEC
        .encodeStart(NbtOps.INSTANCE, content)
        .result()
        .ifPresent(nbt -> tag.put("BookContent", nbt));
    stack.setTag(tag);
    return stack;
  }

  public static Optional<BookContent> getBookContent(ItemStack stack) {
    if (stack.getItem() != CHEXItems.LIBRARY_BOOK.get() || !stack.hasTag()) {
      return Optional.empty();
    }

    CompoundTag tag = stack.getOrCreateTag();
    if (!tag.contains("BookContent")) {
      return Optional.empty();
    }

    return CONTENT_CODEC.parse(NbtOps.INSTANCE, tag.get("BookContent")).result();
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    getBookContent(stack)
        .ifPresent(
            content -> {
              tooltip.add(
                  Component.literal("by " + content.getAuthor()).withStyle(ChatFormatting.GRAY));
              if (content.getGeneration() > 0) {
                tooltip.add(
                    Component.translatable(
                            "item.cosmic_horizons.library_book.generation."
                                + content.getGeneration())
                        .withStyle(ChatFormatting.GOLD));
              }
              if (!content.getLoreFragment().isEmpty()) {
                tooltip.add(
                    Component.translatable("item.cosmic_horizons.library_book.lore_fragment")
                        .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
              }
              if (!content.isTranslated()) {
                tooltip.add(
                    Component.translatable("item.cosmic_horizons.library_book.untranslated")
                        .withStyle(ChatFormatting.RED));
              }
            });
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    BlockPos blockpos = context.getClickedPos();
    BlockState blockstate = level.getBlockState(blockpos);

    if (blockstate.is(Blocks.LECTERN)) {
      return LecternBlock.tryPlaceBook(
              context.getPlayer(), level, blockpos, blockstate, context.getItemInHand())
          ? InteractionResult.sidedSuccess(level.isClientSide)
          : InteractionResult.PASS;
    } else {
      return InteractionResult.PASS;
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);

    if (level.isClientSide) {
      // Open the book GUI on the client
      // This would be handled by a client-side packet
      player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
      return InteractionResultHolder.success(itemstack);
    }

    // On server, just update stats
    player.awardStat(Stats.ITEM_USED.get(this));

    // If the book is not translated, try to translate it if the player has the
    // knowledge
    getBookContent(itemstack)
        .ifPresent(
            content -> {
              if (!content.isTranslated() && player.getRandom().nextFloat() < 0.1f) {
                // 10% chance to gain insight when reading an untranslated book
                // This would be expanded with the knowledge system
                player.displayClientMessage(
                    Component.translatable("message.cosmic_horizons.gain_insight")
                        .withStyle(ChatFormatting.GOLD),
                    true);
              }
            });

    return InteractionResultHolder.consume(itemstack);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return getBookContent(stack).map(content -> !content.getLoreFragment().isEmpty()).orElse(false);
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return false;
  }

  public static class BookContent {
    private final String title;
    private final String author;
    private final List<String> pages;
    private final int generation;
    private final String loreFragment;
    private final boolean isTranslated;
    private final String originalLanguage;
    private final Optional<List<String>> discoveredBy;

    public BookContent(
        String title,
        String author,
        List<String> pages,
        int generation,
        String loreFragment,
        boolean isTranslated,
        String originalLanguage,
        Optional<List<String>> discoveredBy) {
      this.title = title;
      this.author = author;
      this.pages = pages;
      this.generation = generation;
      this.loreFragment = loreFragment;
      this.isTranslated = isTranslated;
      this.originalLanguage = originalLanguage;
      this.discoveredBy = discoveredBy;
    }

    // Getters
    public String getTitle() {
      return title;
    }

    public String getAuthor() {
      return author;
    }

    public List<String> getPages() {
      return pages;
    }

    public int getGeneration() {
      return generation;
    }

    public String getLoreFragment() {
      return loreFragment;
    }

    public boolean isTranslated() {
      return isTranslated;
    }

    public String getOriginalLanguage() {
      return originalLanguage;
    }

    public Optional<List<String>> getDiscoveredBy() {
      return discoveredBy;
    }

    public BookContent withTranslated(boolean translated) {
      return new BookContent(
          title,
          author,
          pages,
          generation,
          loreFragment,
          translated,
          originalLanguage,
          discoveredBy);
    }

    public BookContent withDiscoveredBy(String playerName) {
      List<String> discoverers = discoveredBy.orElseGet(ArrayList::new);
      if (!discoverers.contains(playerName)) {
        List<String> newDiscoverers = new ArrayList<>(discoverers);
        newDiscoverers.add(playerName);
        return new BookContent(
            title,
            author,
            pages,
            generation,
            loreFragment,
            isTranslated,
            originalLanguage,
            Optional.of(newDiscoverers));
      }
      return this;
    }
  }

  public static class BookContentBuilder {
    private String title;
    private String author = "Unknown";
    private List<String> pages = new ArrayList<>();
    private int generation = 0;
    private String loreFragment = "";
    private boolean isTranslated = false;
    private String originalLanguage = "Ancient";
    private List<String> discoveredBy = new ArrayList<>();

    public BookContentBuilder setTitle(String title) {
      this.title = title;
      return this;
    }

    public BookContentBuilder setAuthor(String author) {
      this.author = author;
      return this;
    }

    public BookContentBuilder addPage(String page) {
      this.pages.add(page);
      return this;
    }

    public BookContentBuilder setPages(List<String> pages) {
      this.pages = new ArrayList<>(pages);
      return this;
    }

    public BookContentBuilder setGeneration(int generation) {
      this.generation = generation;
      return this;
    }

    public BookContentBuilder setLoreFragment(String loreFragment) {
      this.loreFragment = loreFragment;
      return this;
    }

    public BookContentBuilder setTranslated(boolean translated) {
      this.isTranslated = translated;
      return this;
    }

    public BookContentBuilder setOriginalLanguage(String language) {
      this.originalLanguage = language;
      return this;
    }

    public BookContentBuilder addDiscoverer(String playerName) {
      if (!this.discoveredBy.contains(playerName)) {
        this.discoveredBy.add(playerName);
      }
      return this;
    }

    public BookContent build() {
      return new BookContent(
          title,
          author,
          pages,
          generation,
          loreFragment,
          isTranslated,
          originalLanguage,
          discoveredBy.isEmpty() ? Optional.empty() : Optional.of(discoveredBy));
    }
  }
}
