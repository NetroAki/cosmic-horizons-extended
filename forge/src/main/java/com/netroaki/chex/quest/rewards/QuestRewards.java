package com.netroaki.chex.quest.rewards;

import com.netroaki.chex.quest.Quest;
import com.netroaki.chex.reputation.FactionReputation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestRewards {
  private static final Logger LOGGER = LogManager.getLogger();

  // Reward types
  public static final int TYPE_ITEM = 0;
  public static final int TYPE_EXPERIENCE = 1;
  public static final int TYPE_REPUTATION = 2;
  public static final int TYPE_COMMAND = 3;
  public static final int TYPE_TITLE = 4;

  // Reward registry
  private static final Map<String, Consumer<ServerPlayer>> REWARD_REGISTRY = new HashMap<>();

  // Register all rewards
  public static void registerRewards() {
    // Main quest rewards
    registerReward(
        "first_steps_complete",
        player -> {
          // Reward for completing the first quest
          giveItem(player, new ItemStack(Items.EMERALD, 5));
          giveExperience(player, 100);
          FactionReputation.get(player).addReputation(FactionReputation.Faction.FREMEN, 50);
          player.sendSystemMessage(Component.literal("You've gained the trust of the Fremen!"));
        });

    // Side quest rewards
    registerReward(
        "water_for_people",
        player -> {
          giveItem(player, new ItemStack(Items.EMERALD, 3));
          FactionReputation.get(player).addReputation(FactionReputation.Faction.FREMEN, 50);
        });

    registerReward(
        "hunter_of_dunes",
        player -> {
          giveItem(player, new ItemStack(Items.EMERALD, 5));
          ItemStack sword = new ItemStack(Items.IRON_SWORD);
          sword.enchant(Enchantments.SHARPNESS, 2);
          giveItem(player, sword);
          FactionReputation.get(player).addReputation(FactionReputation.Faction.FREMEN, 30);
        });

    registerReward(
        "secrets_beneath_sand",
        player -> {
          giveItem(player, new ItemStack(Items.EMERALD, 8));
          giveItem(player, new ItemStack(Items.EXPERIENCE_BOTTLE, 3));
          FactionReputation.get(player).addReputation(FactionReputation.Faction.FREMEN, 20);
        });

    registerReward(
        "spice_trade",
        player -> {
          giveItem(player, new ItemStack(Items.EMERALD, 12));
          giveItem(player, new ItemStack(Items.ENDER_PEARL, 2));
          FactionReputation.get(player).addReputation(FactionReputation.Faction.SPACING_GUILD, 40);
        });

    // Reputation tier rewards
    registerReputationRewards();
  }

  // Register reputation-based rewards
  private static void registerReputationRewards() {
    // Fremen reputation rewards
    registerReputationReward(
        FactionReputation.Faction.FREMEN,
        FactionReputation.Tier.FRIENDLY,
        player -> {
          player.sendSystemMessage(Component.literal("The Fremen now consider you a friend!"));
          // Unlock Fremen-specific trades or quests here
        });

    registerReputationReward(
        FactionReputation.Faction.FREMEN,
        FactionReputation.Tier.HONORED,
        player -> {
          player.sendSystemMessage(Component.literal("You are now honored among the Fremen!"));
          // Unlock advanced Fremen equipment or abilities
          ItemStack crysknife = new ItemStack(Items.IRON_SWORD);
          crysknife.setHoverName(Component.literal("Crysknife"));
          crysknife.enchant(Enchantments.SHARPNESS, 3);
          giveItem(player, crysknife);
        });

    // Spacing Guild reputation rewards
    registerReputationReward(
        FactionReputation.Faction.SPACING_GUILD,
        FactionReputation.Tier.FRIENDLY,
        player -> {
          player.sendSystemMessage(
              Component.literal("The Spacing Guild now considers you a valued associate!"));
          // Unlock Guild-specific trades or quests
        });
  }

  // Helper methods
  private static void registerReward(String id, Consumer<ServerPlayer> reward) {
    REWARD_REGISTRY.put(id, reward);
  }

  private static void registerReputationReward(
      FactionReputation.Faction faction,
      FactionReputation.Tier tier,
      Consumer<ServerPlayer> reward) {
    String rewardId =
        "reputation_" + faction.name().toLowerCase() + "_" + tier.name().toLowerCase();
    registerReward(rewardId, reward);
  }

  public static void giveReward(ServerPlayer player, String rewardId) {
    Consumer<ServerPlayer> reward = REWARD_REGISTRY.get(rewardId);
    if (reward != null) {
      reward.accept(player);
    } else {
      LOGGER.warn("Unknown reward ID: {}", rewardId);
    }
  }

  public static void giveItem(Player player, ItemStack stack) {
    if (!player.getInventory().add(stack)) {
      player.drop(stack, false);
    }
  }

  public static void giveExperience(Player player, int amount) {
    player.giveExperiencePoints(amount);
  }

  public static void processQuestRewards(ServerPlayer player, Quest quest) {
    for (Quest.Reward reward : quest.getRewards()) {
      switch (reward.getType()) {
        case ITEM:
          giveItem(player, reward.getItem().copy());
          break;
        case EXPERIENCE:
          giveExperience(player, reward.getAmount());
          break;
        case REPUTATION:
          if (reward.getFaction() != null) {
            FactionReputation.Faction faction = FactionReputation.Faction.byId(reward.getFaction());
            if (faction != null) {
              FactionReputation.get(player).addReputation(faction, reward.getAmount());
            }
          }
          break;
        case COMMAND:
          // Process command reward (handled by QuestManager)
          break;
      }
    }
  }
}
