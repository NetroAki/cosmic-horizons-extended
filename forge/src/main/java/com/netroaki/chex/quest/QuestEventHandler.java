package com.netroaki.chex.quest;

import com.netroaki.chex.CHEX;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class QuestEventHandler {

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
      return;
    }

    ServerPlayer player = (ServerPlayer) event.player;
    QuestManager manager = QuestManager.get();

    // Check for quest updates every second (20 ticks)
    if (player.tickCount % 20 == 0) {
      checkLocationBasedQuests(player);
      checkSurvivalQuests(player);
    }
  }

  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      // Initialize player quests if needed
      QuestManager.get().getPlayerData(player);

      // Start the first quest if they don't have any active
      if (!QuestManager.get().hasStarted(ArrakisQuests.QUEST_LAND_ON_ARRAKIS, player)
          && !QuestManager.get().hasCompleted(ArrakisQuests.QUEST_LAND_ON_ARRAKIS, player)) {
        QuestManager.get().startQuest(player, ArrakisQuests.QUEST_LAND_ON_ARRAKIS);

        // Send welcome message
        player.displayClientMessage(
            Component.literal(
                "§6New Quest: First Steps on Dune\n§7"
                    + "Survive your first moments on the desert planet Arrakis."),
            false);
      }
    }
  }

  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    // Handle quest-related respawn logic
    if (event.getEntity() instanceof ServerPlayer player) {
      // Check if any quests need to be failed on death
      checkFailedQuests(player);
    }
  }

  @SubscribeEvent
  public static void onPlayerInteract(PlayerInteractEvent event) {
    // Handle item use/block interaction for quests
    if (event.getEntity() instanceof ServerPlayer player) {
      // Example: Check if interacting with a quest-related block
      // updateQuestProgress(player, "interact", event.getPos().toString());
    }
  }

  @SubscribeEvent
  public static void onEntityKilled(LivingDeathEvent event) {
    // Handle mob kills for quests
    if (event.getSource().getEntity() instanceof ServerPlayer player) {
      // Example: Check if killing a quest-related mob
      // updateQuestProgress(player, "kill",
      // event.getEntity().getType().getRegistryName().toString());
    }
  }

  private static void checkLocationBasedQuests(ServerPlayer player) {
    // Check if player is in a specific biome or dimension
    // Example: If player is on Arrakis, update quest progress
    if (player.level().dimension().location().getPath().contains("arrakis")) {
      updateQuestProgress(player, "location", "arrakis");
    }
  }

  private static void checkSurvivalQuests(ServerPlayer player) {
    // Check survival conditions (thirst, heat, etc.)
    // This would integrate with the DesertSurvivalHandler we created earlier
  }

  private static void checkFailedQuests(ServerPlayer player) {
    // Check if any quests should fail on player death
  }

  public static void updateQuestProgress(ServerPlayer player, String type, String target) {
    QuestManager manager = QuestManager.get();
    PlayerQuestData data = manager.getPlayerData(player);

    // Check all in-progress quests
    for (Map.Entry<ResourceLocation, QuestProgress> entry : data.questProgress.entrySet()) {
      Quest quest = manager.getQuest(entry.getKey()).orElse(null);
      if (quest == null) continue;

      // Check each objective
      for (QuestObjective objective : quest.objectives) {
        if (matchesObjective(objective, type, target)) {
          // Update progress
          QuestProgress progress = entry.getValue();
          int current = progress.getProgress(objective.getId());
          progress.setProgress(objective.getId(), current + 1);

          // Notify player of progress
          if (objective.getRequiredCount() > 1) {
            player.displayClientMessage(
                Component.literal(
                    String.format(
                        "§a%s: %d/%d",
                        objective.getDescription(),
                        Math.min(current + 1, objective.getRequiredCount()),
                        objective.getRequiredCount())),
                false);
          }

          // Check if quest is complete
          if (quest.isComplete(player)) {
            quest.onComplete(player);
            data.completeQuest(quest.id);
          }

          break;
        }
      }
    }
  }

  private static boolean matchesObjective(QuestObjective objective, String type, String target) {
    // Simple matching logic - would be expanded based on objective types
    return (objective.getType().name().toLowerCase().contains(type)
        && (objective.getTarget() == null || objective.getTarget().equals(target)));
  }
}
