package com.netroaki.chex.capability;

import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

/** Capability interface for tracking collected lore fragments and translation knowledge */
@AutoRegisterCapability
public interface ILoreKnowledge extends INBTSerializable<CompoundTag> {

  /** Check if the player has collected a specific lore fragment */
  boolean hasLoreFragment(ResourceLocation fragmentId);

  /** Add a new lore fragment to the player's collection */
  void addLoreFragment(ResourceLocation fragmentId);

  /** Get all collected lore fragment IDs */
  Set<ResourceLocation> getCollectedFragments();

  /** Get the player's knowledge level in a specific language */
  int getLanguageKnowledge(String language);

  /** Increase the player's knowledge in a specific language */
  void increaseLanguageKnowledge(String language, int amount);

  /** Check if the player can translate a specific language */
  boolean canTranslate(String language);

  /**
   * Attempt to translate a book for the player
   *
   * @return true if the book was successfully translated
   */
  boolean tryTranslateBook(Player player, ItemStack book);

  /** Sync the capability data to the client */
  void sync(ServerPlayer player);
}
