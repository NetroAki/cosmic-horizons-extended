package com.netroaki.chex.capability;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LoreKnowledgeImpl implements ILoreKnowledge {
  private final Set<ResourceLocation> collectedFragments = new HashSet<>();
  private final Map<String, Integer> languageKnowledge = new HashMap<>();
  private static final int KNOWLEDGE_THRESHOLD = 100;

  @Override
  public boolean hasLoreFragment(ResourceLocation fragmentId) {
    return collectedFragments.contains(fragmentId);
  }

  @Override
  public void addLoreFragment(ResourceLocation fragmentId) {
    if (fragmentId != null && !collectedFragments.contains(fragmentId)) {
      collectedFragments.add(fragmentId);
      // Gain knowledge when discovering new lore
      increaseLanguageKnowledge("Ancient", 5);
    }
  }

  @Override
  public Set<ResourceLocation> getCollectedFragments() {
    return Collections.unmodifiableSet(collectedFragments);
  }

  @Override
  public int getLanguageKnowledge(String language) {
    return languageKnowledge.getOrDefault(language, 0);
  }

  @Override
  public void increaseLanguageKnowledge(String language, int amount) {
    if (amount <= 0) return;

    int current = getLanguageKnowledge(language);
    int newAmount = Math.min(100, current + amount); // Cap at 100
    languageKnowledge.put(language, newAmount);
  }

  @Override
  public boolean canTranslate(String language) {
    return getLanguageKnowledge(language) >= KNOWLEDGE_THRESHOLD;
  }

  @Override
  public boolean tryTranslateBook(Player player, ItemStack book) {
    if (book.isEmpty() || book.getItem() != Items.WRITTEN_BOOK) {
      return false;
    }

    // Check if the book is in an ancient language
    CompoundTag tag = book.getTag();
    if (tag != null && tag.contains("language") && !tag.getBoolean("translated")) {
      String language = tag.getString("language");
      if (canTranslate(language)) {
        // Mark as translated
        tag.putBoolean("translated", true);

        // Grant experience for translating
        if (player instanceof ServerPlayer serverPlayer) {
          serverPlayer.giveExperiencePoints(10);
          sync(serverPlayer);
        }

        return true;
      }
    }
    return false;
  }

  @Override
  public void sync(ServerPlayer player) {
    if (player.level().isClientSide) return;
    LoreKnowledgeProvider.syncCapability(player);
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();

    // Save collected fragments
    ListTag fragmentsList = new ListTag();
    for (ResourceLocation fragment : collectedFragments) {
      fragmentsList.add(StringTag.valueOf(fragment.toString()));
    }
    nbt.put("Fragments", fragmentsList);

    // Save language knowledge
    CompoundTag knowledgeNbt = new CompoundTag();
    languageKnowledge.forEach(knowledgeNbt::putInt);
    nbt.put("LanguageKnowledge", knowledgeNbt);

    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    // Load collected fragments
    collectedFragments.clear();
    if (nbt.contains("Fragments", Tag.TAG_LIST)) {
      ListTag fragmentsList = nbt.getList("Fragments", Tag.TAG_STRING);
      for (int i = 0; i < fragmentsList.size(); i++) {
        try {
          collectedFragments.add(new ResourceLocation(fragmentsList.getString(i)));
        } catch (Exception e) {
          CosmicHorizonsExpanded.LOGGER.error(
              "Failed to load lore fragment: {}", fragmentsList.getString(i), e);
        }
      }
    }

    // Load language knowledge
    languageKnowledge.clear();
    if (nbt.contains("LanguageKnowledge", Tag.TAG_COMPOUND)) {
      CompoundTag knowledgeNbt = nbt.getCompound("LanguageKnowledge");
      for (String key : knowledgeNbt.getAllKeys()) {
        languageKnowledge.put(key, knowledgeNbt.getInt(key));
      }
    }
  }

  public static ILoreKnowledge get(Player player) {
    return player
        .getCapability(LoreKnowledgeProvider.LORE_KNOWLEDGE_CAPABILITY)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Player " + player.getName().getString() + " has no LoreKnowledge capability"));
  }
}
