package com.netroaki.chex.quest;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class QuestObjective {
  private final String id;
  private final Component description;
  private final int targetCount;
  private int currentCount;
  private final String type;
  private final String target;
  private final ItemStack icon;
  private final boolean hidden;
  private final boolean optional;

  public QuestObjective(
      String description, String type, String target, int current, int targetCount) {
    this(description, type, target, current, targetCount, ItemStack.EMPTY, false, false);
  }

  public QuestObjective(
      String description,
      String type,
      String target,
      int current,
      int targetCount,
      ItemStack icon) {
    this(description, type, target, current, targetCount, icon, false, false);
  }

  public QuestObjective(
      String description,
      String type,
      String target,
      int current,
      int targetCount,
      ItemStack icon,
      boolean hidden,
      boolean optional) {
    this.id = type + "_" + target.replace(':', '_');
    this.description = Component.literal(description);
    this.type = type;
    this.target = target;
    this.currentCount = current;
    this.targetCount = targetCount;
    this.icon = icon != null ? icon : getDefaultIcon(type, target);
    this.hidden = hidden;
    this.optional = optional;
  }

  private ItemStack getDefaultIcon(String type, String target) {
    return switch (type) {
      case "collect" -> getItemStackFromTarget(target);
      case "kill" -> new ItemStack(Items.IRON_SWORD);
      case "interact" -> getBlockOrItemStack(target);
      case "travel" -> new ItemStack(Items.COMPASS);
      case "craft" -> getItemStackFromTarget(target);
      default -> new ItemStack(Items.BOOK);
    };
  }

  private ItemStack getItemStackFromTarget(String target) {
    ResourceLocation rl = ResourceLocation.tryParse(target);
    if (rl != null) {
      Item item = BuiltInRegistries.ITEM.get(rl);
      if (item != Items.AIR) {
        return new ItemStack(item);
      }
    }
    return new ItemStack(Items.PAPER);
  }

  private ItemStack getBlockOrItemStack(String target) {
    ResourceLocation rl = ResourceLocation.tryParse(target);
    if (rl != null) {
      // Try to get block first
      Block block = BuiltInRegistries.BLOCK.get(rl);
      if (block != Blocks.AIR) {
        return new ItemStack(block.asItem());
      }
      // Then try item
      Item item = BuiltInRegistries.ITEM.get(rl);
      if (item != Items.AIR) {
        return new ItemStack(item);
      }
    }
    return new ItemStack(Items.STONE_BUTTON);
  }

  public String getId() {
    return id;
  }

  public Component getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }

  public String getTarget() {
    return target;
  }

  public int getCurrentCount() {
    return currentCount;
  }

  public int getTargetCount() {
    return targetCount;
  }

  public ItemStack getIcon() {
    return icon.copy();
  }

  public boolean isComplete() {
    return currentCount >= targetCount;
  }

  public boolean isHidden() {
    return hidden;
  }

  public boolean isOptional() {
    return optional;
  }

  public void setProgress(int count) {
    this.currentCount = Math.min(count, targetCount);
  }

  public void incrementProgress(int amount) {
    this.currentCount = Math.min(currentCount + amount, targetCount);
  }

  public float getProgress() {
    return targetCount > 0 ? (float) currentCount / targetCount : 1.0f;
  }

  public Component getProgressText() {
    if (isComplete()) {
      return Component.translatable("quest.objective.complete", description);
    }
    return Component.translatable(
        "quest.objective.progress", description, currentCount, targetCount);
  }
}
