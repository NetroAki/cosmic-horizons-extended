package com.netroaki.chex.quest;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.quest.rewards.QuestRewards;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public class QuestManager extends SavedData {
  private static final String DATA_NAME = CHEX.MOD_ID + "_quests";
  private static final String TAG_QUESTS = "Quests";
  private static final String TAG_PLAYER_DATA = "PlayerData";
  private static final String TAG_ACTIVE_QUESTS = "ActiveQuests";
  private static final String TAG_COMPLETED_QUESTS = "CompletedQuests";

  private static QuestManager instance;
  private final Map<ResourceLocation, Quest> allQuests = new HashMap<>();
  private final Map<UUID, PlayerQuestData> playerData = new HashMap<>();
  private final Map<String, List<BiConsumer<ServerPlayer, Object>>> eventHandlers = new HashMap<>();

  // Singleton pattern
  public static QuestManager get() {
    if (instance == null) {
      throw new IllegalStateException("QuestManager not initialized!");
    }
    return instance;
  }

  public static void init(DimensionDataStorage storage) {
    instance = storage.computeIfAbsent(QuestManager::load, QuestManager::new, DATA_NAME);
    registerDefaultEventHandlers();
  }

  private static void registerDefaultEventHandlers() {
    QuestManager manager = get();

    // Register event handlers for different objective types
    manager.registerEventHandler(
        "kill",
        (player, event) -> {
          if (event instanceof LivingDeathEvent deathEvent) {
            ResourceLocation entityId = deathEvent.getEntity().getType().getRegistryName();
            if (entityId != null) {
              manager.updateObjectiveProgress(player, "kill", entityId.toString(), 1);
            }
          }
        });

    manager.registerEventHandler(
        "collect",
        (player, event) -> {
          if (event instanceof PlayerInteractEvent.RightClickItem clickEvent) {
            ItemStack stack = clickEvent.getItemStack();
            ResourceLocation itemId = stack.getItem().getRegistryName();
            if (itemId != null) {
              manager.updateObjectiveProgress(
                  player, "collect", itemId.toString(), stack.getCount());
            }
          }
        });

    manager.registerEventHandler(
        "interact",
        (player, event) -> {
          if (event instanceof PlayerInteractEvent.RightClickBlock clickEvent) {
            if (clickEvent.getLevel() instanceof ServerLevel) {
              String blockId =
                  clickEvent
                      .getLevel()
                      .getBlockState(clickEvent.getPos())
                      .getBlock()
                      .getRegistryName()
                      .toString();
              manager.updateObjectiveProgress(player, "interact", blockId, 1);
            }
          }
        });
  }

  public void registerEventHandler(String eventType, BiConsumer<ServerPlayer, Object> handler) {
    eventHandlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
  }

  public void fireEvent(ServerPlayer player, String eventType, Object event) {
    for (BiConsumer<ServerPlayer, Object> handler :
        eventHandlers.getOrDefault(eventType, Collections.emptyList())) {
      handler.accept(player, event);
    }
  }

  // Register a new quest
  public void registerQuest(Quest quest) {
    if (allQuests.containsKey(quest.id)) {
      CHEX.LOGGER.warn("Duplicate quest registration for {}", quest.id);
      return;
    }
    allQuests.put(quest.id, quest);
    setDirty();
  }

  // Get a quest by ID
  public Optional<Quest> getQuest(ResourceLocation id) {
    return Optional.ofNullable(allQuests.get(id));
  }

  // Get all quests
  public Collection<Quest> getAllQuests() {
    return Collections.unmodifiableCollection(allQuests.values());
  }

  // Get player's quest data
  public PlayerQuestData getPlayerData(ServerPlayer player) {
    return playerData.computeIfAbsent(player.getUUID(), uuid -> new PlayerQuestData(uuid));
  }

  // Start a quest for a player
  public boolean startQuest(ServerPlayer player, ResourceLocation questId) {
    Quest quest = allQuests.get(questId);
    if (quest == null) {
      CHEX.LOGGER.warn("Attempted to start unknown quest: {}", questId);
      return false;
    }

    PlayerQuestData data = getPlayerData(player);
    if (data.startQuest(quest)) {
      setDirty();
      player.sendSystemMessage(Component.literal("§aQuest started: §e" + quest.title));
      return true;
    }
    return false;
  }

  // Complete a quest for a player
  public void completeQuest(ServerPlayer player, String questId) {
    Quest quest = getQuest(questId);
    if (quest == null) {
      LOGGER.warn("Tried to complete unknown quest: {}", questId);
      return;
    }

    PlayerQuestData data = getOrCreatePlayerData(player);
    if (!data.completeQuest(questId)) {
      LOGGER.warn(
          "Player {} cannot complete quest {} - requirements not met",
          player.getName().getString(),
          questId);
      return;
    }

    // Process rewards using the QuestRewards system
    QuestRewards.processQuestRewards(player, quest);

    // Process any command rewards separately
    for (Quest.Reward reward : quest.getRewards()) {
      if (reward.getType() == Quest.Reward.Type.COMMAND && reward.getCommand() != null) {
        player
            .server
            .getCommands()
            .performPrefixedCommand(
                player.createCommandSourceStack(),
                reward.getCommand().replace("@p", player.getName().getString()));
      }
    }

    // Save player data
    savePlayerData(player, data);

    // Notify the player
    player.sendSystemMessage(Component.literal("Quest completed: ").append(quest.getTitle()));
  }

  // Update objective progress for a player
  public void updateObjectiveProgress(ServerPlayer player, String type, String target, int amount) {
    PlayerQuestData data = getPlayerData(player);
    boolean updated = false;

    for (Map.Entry<ResourceLocation, PlayerQuestData.QuestProgress> entry :
        data.questProgress.entrySet()) {
      Quest quest = allQuests.get(entry.getKey());
      if (quest != null) {
        for (QuestObjective objective : quest.objectives) {
          if (objective.getType().equals(type)
              && (objective.getTarget().equals(target) || target.equals("*"))
              && !objective.isComplete()) {

            objective.incrementProgress(amount);
            updated = true;

            // Notify player of progress
            if (objective.isComplete()) {
              player.sendSystemMessage(
                  Component.literal("§aObjective complete: §e" + objective.getDescription()));
            }
          }
        }

        // Check if quest is complete
        if (quest.isComplete(player)) {
          completeQuest(player, quest.id);
        }
      }
    }

    if (updated) {
      setDirty();
    }
  }

  // Get available quests for a player
  public List<Quest> getAvailableQuests(ServerPlayer player) {
    PlayerQuestData data = getPlayerData(player);
    return allQuests.values().stream()
        .filter(quest -> !data.hasStarted(quest.id) && !data.hasCompleted(quest.id))
        .filter(quest -> arePrerequisitesMet(player, quest))
        .collect(Collectors.toList());
  }

  // Check if all prerequisites are met for a quest
  private boolean arePrerequisitesMet(ServerPlayer player, Quest quest) {
    if (quest.prerequisites.isEmpty()) {
      return true;
    }

    PlayerQuestData data = getPlayerData(player);
    return quest.prerequisites.stream()
        .allMatch(
            prereqId ->
                data.hasCompleted(prereqId)
                    || (data.hasStarted(prereqId) && allQuests.get(prereqId).isComplete(player)));
  }

  // Event handlers
  @SubscribeEvent
  public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      // Initialize player data if needed
      get().getPlayerData(player);
    }
  }

  @SubscribeEvent
  public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
    // Clean up player data if needed
  }

  // NBT Serialization
  @Override
  public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
    // Save quest definitions
    ListTag questsList = new ListTag();
    allQuests.forEach(
        (id, quest) -> {
          CompoundTag questTag = new CompoundTag();
          questTag.putString("id", id.toString());
          // Add more quest data as needed
          questsList.add(questTag);
        });
    nbt.put("Quests", questsList);

    // Save player data
    CompoundTag playersTag = new CompoundTag();
    playerData.forEach(
        (uuid, data) -> {
          playersTag.put(uuid.toString(), data.save(new CompoundTag()));
        });
    nbt.put("PlayerData", playersTag);

    return nbt;
  }

  public static QuestManager load(CompoundTag nbt) {
    QuestManager manager = new QuestManager();
    // Load quest definitions (optional lightweight parse)
    if (nbt.contains("Quests", Tag.TAG_LIST)) {
      ListTag questsList = nbt.getList("Quests", Tag.TAG_COMPOUND);
      for (Tag tag : questsList) {
        CompoundTag questTag = (CompoundTag) tag;
        ResourceLocation id = new ResourceLocation(questTag.getString("id"));
        // Future: reconstruct quest definitions if persisted
      }
    }
    // Load player data
    if (nbt.contains("PlayerData", Tag.TAG_COMPOUND)) {
      CompoundTag playersTag = nbt.getCompound("PlayerData");
      for (String uuidStr : playersTag.getAllKeys()) {
        UUID uuid = UUID.fromString(uuidStr);
        PlayerQuestData data = new PlayerQuestData(uuid);
        data.load(playersTag.getCompound(uuidStr));
        manager.playerData.put(uuid, data);
      }
    }
    return manager;
  }

  // Quest progress tracking
  public static class QuestProgress {
    private final Quest quest;
    private final Map<String, Integer> progress = new HashMap<>();
    private long startTime;
    private long timeSpent; // For time-based objectives

    private final ResourceLocation questId;
    private final Map<String, Integer> objectiveProgress = new HashMap<>();

    public QuestProgress(Quest quest) {
      this.questId = quest.id;
      // Initialize progress for all objectives
      quest.objectives.forEach(obj -> objectiveProgress.put(obj.getId(), 0));
    }

    public QuestProgress(ResourceLocation questId) {
      this.questId = questId;
    }

    public boolean isComplete() {
      // Check if all objectives are complete
      return objectiveProgress.entrySet().stream()
          .allMatch(
              entry -> {
                // Get the objective and check if it's complete
                // This assumes we have access to the quest definition
                return true; // Simplified for now
              });
    }

    public void setProgress(String objectiveId, int progress) {
      objectiveProgress.put(objectiveId, progress);
    }

    public int getProgress(String objectiveId) {
      return objectiveProgress.getOrDefault(objectiveId, 0);
    }

    public CompoundTag save(CompoundTag nbt) {
      CompoundTag progressTag = new CompoundTag();
      objectiveProgress.forEach((id, progress) -> progressTag.putInt(id, progress));
      nbt.put("Progress", progressTag);
      return nbt;
    }

    public void load(CompoundTag nbt) {
      if (nbt.contains("Progress", Tag.TAG_COMPOUND)) {
        CompoundTag progressTag = nbt.getCompound("Progress");
        progressTag
            .getAllKeys()
            .forEach(key -> objectiveProgress.put(key, progressTag.getInt(key)));
      }
    }
  }

  // Helper method to mark data as dirty
  private static void setDirty() {
    if (instance != null) {
      instance.setDirty();
    }
  }
}
