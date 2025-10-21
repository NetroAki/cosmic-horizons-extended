package com.netroaki.chex.quest.arrakis;

import com.netroaki.chex.quest.Quest;
import com.netroaki.chex.quest.QuestObjective;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArrakisSideQuests {
  public static final ResourceLocation QUEST_FREMEN_AID =
      new ResourceLocation("cosmic_horizons_extended", "fremen_aid");
  public static final ResourceLocation QUEST_SANDWORM_HUNTER =
      new ResourceLocation("cosmic_horizons_extended", "sandworm_hunter");
  public static final ResourceLocation QUEST_RUIN_EXPLORER =
      new ResourceLocation("cosmic_horizons_extended", "ruin_explorer");
  public static final ResourceLocation QUEST_SPICE_TRADER =
      new ResourceLocation("cosmic_horizons_extended", "spice_trader");

  public static void registerQuests() {
    // 1. Fremen Aid - Help the Fremen with their water crisis
    Quest fremenAid =
        new Quest(
            QUEST_FREMEN_AID,
            Component.literal("Water for the People"),
            Component.literal(
                "The Fremen are in desperate need of water. Collect and deliver water to their"
                    + " sietch."),
            Quest.QuestType.SIDE,
            List.of(
                new QuestObjective(
                    "Collect 8 Water Buckets", "collect", "minecraft:water_bucket", 0, 8),
                new QuestObjective(
                    "Deliver water to the Fremen Sietch",
                    "interact",
                    "faction:fremen_elder",
                    0,
                    1)),
            List.of(
                new Quest.Reward(Quest.Reward.Type.ITEM, new ItemStack(Items.EMERALD, 3), 1.0f),
                new Quest.Reward(
                    Quest.Reward.Type.REPUTATION, "fremen", 50) // +50 Fremen reputation
                ),
            1, // Level 1 quest
            List.of() // No prerequisites
            );

    // 2. Sandworm Hunter - Hunt a juvenile sandworm
    Quest sandwormHunter =
        new Quest(
            QUEST_SANDWORM_HUNTER,
            Component.literal("Hunter of the Dunes"),
            Component.literal(
                "The Fremen need help controlling the sandworm population. Hunt a juvenile"
                    + " sandworm."),
            Quest.QuestType.SIDE,
            List.of(
                new QuestObjective(
                    "Kill a Juvenile Sandworm", "kill", "cosmic_horizons:sandworm_juvenile", 0, 1)),
            List.of(
                new Quest.Reward(Quest.Reward.Type.ITEM, new ItemStack(Items.EMERALD, 5), 1.0f),
                new Quest.Reward(
                    Quest.Reward.Type.REPUTATION, "fremen", 30), // +30 Fremen reputation
                new Quest.Reward(
                    Quest.Reward.Type.ITEM,
                    new ItemStack(Items.IRON_SWORD).enchant(Enchantments.SHARPNESS, 2),
                    1.0f)),
            2, // Level 2 quest
            List.of() // No prerequisites
            );

    // 3. Ruin Explorer - Discover ancient ruins
    Quest ruinExplorer =
        new Quest(
            QUEST_RUIN_EXPLORER,
            Component.literal("Secrets Beneath the Sand"),
            Component.literal(
                "Explore the ancient ruins scattered across the desert and recover lost"
                    + " artifacts."),
            Quest.QuestType.SIDE,
            List.of(
                new QuestObjective(
                    "Find 3 Ancient Ruins", "interact", "cosmic_horizons:ancient_ruin", 0, 3),
                new QuestObjective(
                    "Recover Ancient Artifact",
                    "collect",
                    "cosmic_horizons:ancient_artifact",
                    0,
                    1)),
            List.of(
                new Quest.Reward(Quest.Reward.Type.ITEM, new ItemStack(Items.EMERALD, 8), 1.0f),
                new Quest.Reward(
                    Quest.Reward.Type.REPUTATION, "fremen", 20), // +20 Fremen reputation
                new Quest.Reward(
                    Quest.Reward.Type.ITEM, new ItemStack(Items.EXPERIENCE_BOTTLE, 3), 1.0f)),
            2, // Level 2 quest
            List.of() // No prerequisites
            );

    // 4. Spice Trader - Trade spice with the Guild
    Quest spiceTrader =
        new Quest(
            QUEST_SPICE_TRADER,
            Component.literal("The Spice Trade"),
            Component.literal(
                "The Spacing Guild is paying top prices for Spice Melange. Harvest and sell them 16"
                    + " units."),
            Quest.QuestType.SIDE,
            List.of(
                new QuestObjective(
                    "Harvest 16 Spice Melange", "collect", "cosmic_horizons:spice_melange", 0, 16),
                new QuestObjective(
                    "Deliver to Guild Representative",
                    "interact",
                    "faction:guild_representative",
                    0,
                    1)),
            List.of(
                new Quest.Reward(Quest.Reward.Type.ITEM, new ItemStack(Items.EMERALD, 12), 1.0f),
                new Quest.Reward(
                    Quest.Reward.Type.REPUTATION,
                    "spacing_guild",
                    40), // +40 Spacing Guild reputation
                new Quest.Reward(
                    Quest.Reward.Type.ITEM, new ItemStack(Items.ENDER_PEARL, 2), 1.0f)),
            3, // Level 3 quest
            List.of(ArrakisQuests.QUEST_FIRST_STEPS) // Requires completing first main quest
            );

    // Register all side quests
    QuestManager.get().registerQuest(fremenAid);
    QuestManager.get().registerQuest(sandwormHunter);
    QuestManager.get().registerQuest(ruinExplorer);
    QuestManager.get().registerQuest(spiceTrader);
  }

  // Event handler for quest progress updates
  @SubscribeEvent
  public static void onEntityKilled(LivingDeathEvent event) {
    if (event.getSource().getEntity() instanceof ServerPlayer player) {
      ResourceLocation entityId = event.getEntity().getType().getRegistryName();
      if (entityId != null) {
        // Check for sandworm kill objective
        if (entityId.toString().equals("cosmic_horizons:sandworm_juvenile")) {
          QuestManager.get()
              .updateObjectiveProgress(
                  player, QUEST_SANDWORM_HUNTER, "kill", entityId.toString(), 1);
        }
      }
    }
  }

  // Helper method to check if a player is in the Arrakis dimension
  private static boolean isOnArrakis(ServerPlayer player) {
    return player != null
        && player.level.dimension().location().toString().equals("cosmic_horizons:arrakis");
  }
}
