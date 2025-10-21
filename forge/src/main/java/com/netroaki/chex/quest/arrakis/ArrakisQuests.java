package com.netroaki.chex.quest.arrakis;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.quest.Quest;
import com.netroaki.chex.quest.QuestManager;
import com.netroaki.chex.quest.QuestObjective;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArrakisQuests {
  public static final String NAMESPACE = "arrakis";

  // Quest IDs
  public static final ResourceLocation QUEST_LAND_ON_ARRAKIS =
      CHEX.id(NAMESPACE + ":land_on_arrakis");
  public static final ResourceLocation QUEST_FIND_SIETCH = CHEX.id(NAMESPACE + ":find_sietch");
  public static final ResourceLocation QUEST_SPICE_HARVEST = CHEX.id(NAMESPACE + ":harvest_spice");
  public static final ResourceLocation QUEST_FACE_SHAI_HULUD =
      CHEX.id(NAMESPACE + ":face_shai_hulud");

  @SubscribeEvent
  public static void registerQuests(FMLCommonSetupEvent event) {
    event.enqueueWork(
        () -> {
          // 1. Land on Arrakis
          Quest landOnArrakis =
              new Quest(
                  QUEST_LAND_ON_ARRAKIS,
                  "First Steps on Dune",
                  "You've arrived on the desert planet Arrakis. Survive the harsh conditions and"
                      + " find shelter.",
                  Quest.QuestType.MAIN_STORY,
                  100,
                  List.of(new ItemStack(Items.WATER_BUCKET))) {
                @Override
                public boolean isComplete(ServerPlayer player) {
                  // Check if player has spent at least 5 minutes on Arrakis
                  return player.level().getGameTime() - getStartTime(player) >= 6000; // 5 minutes
                }

                @Override
                public void onComplete(ServerPlayer player) {
                  // Grant rewards
                  player.giveExperiencePoints(xpReward);
                  itemRewards.forEach(
                      item -> {
                        if (!player.addItem(item)) {
                          player.drop(item, false);
                        }
                      });

                  // Notify player
                  player.displayClientMessage(
                      Component.literal("§6Quest Complete: " + title + "!")
                          .append("\n§7" + "You've survived your first moments on Arrakis."),
                      false);

                  // Start next quest
                  QuestManager.get().startQuest(player, QUEST_FIND_SIETCH);
                }
              };

          // Add objectives
          landOnArrakis.addObjective(
              new QuestObjective(
                  "survive",
                  "Survive on Arrakis",
                  QuestObjective.ObjectiveType.CUSTOM,
                  "arrakis:survive",
                  1,
                  new ItemStack(Items.CLOCK)));

          // Register the quest
          QuestManager.get().registerQuest(landOnArrakis);

          // 2. Find Sietch Tabr
          Quest findSietch =
              new Quest(
                  QUEST_FIND_SIETCH,
                  "Sanctuary in the Dunes",
                  "The Fremen of Sietch Tabr may offer shelter and knowledge. Find their hidden"
                      + " base.",
                  Quest.QuestType.MAIN_STORY,
                  250,
                  List.of(new ItemStack(Items.MAP))) {
                // Implementation would be similar to above
                @Override
                public boolean isComplete(ServerPlayer player) {
                  return false;
                }

                @Override
                public void onComplete(ServerPlayer player) {}
              };

          // 3. Harvest Spice
          Quest harvestSpice =
              new Quest(
                  QUEST_SPICE_HARVEST,
                  "The Spice Must Flow",
                  "Harvest Spice Melange, the most valuable substance in the universe.",
                  Quest.QuestType.MAIN_STORY,
                  500,
                  List.of(new ItemStack(Items.GOLD_INGOT, 3))) {
                @Override
                public boolean isComplete(ServerPlayer player) {
                  return false;
                }

                @Override
                public void onComplete(ServerPlayer player) {}
              };

          // 4. Face Shai-Hulud
          Quest faceShaiHulud =
              new Quest(
                  QUEST_FACE_SHAI_HULUD,
                  "Ride the Maker",
                  "Prove your worth by summoning and riding a sandworm, the great Shai-Hulud.",
                  Quest.QuestType.MAIN_STORY,
                  1000,
                  List.of(new ItemStack(Items.DIAMOND))) {
                @Override
                public boolean isComplete(ServerPlayer player) {
                  return false;
                }

                @Override
                public void onComplete(ServerPlayer player) {}
              };

          // Register all quests
          QuestManager.get().registerQuest(findSietch);
          QuestManager.get().registerQuest(harvestSpice);
          QuestManager.get().registerQuest(faceShaiHulud);

          CHEX.LOGGER.info("Registered Arrakis quests");
        });
  }
}
