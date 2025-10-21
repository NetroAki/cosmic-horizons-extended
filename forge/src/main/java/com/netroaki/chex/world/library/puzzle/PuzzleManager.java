package com.netroaki.chex.world.library.puzzle;

import com.google.common.collect.ImmutableMap;
import com.netroaki.chex.CHEX;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/**
 * Manages all book-based puzzles in the Infinite Library. Handles puzzle registration, state
 * tracking, and player progress.
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PuzzleManager {
  private static final Map<String, BookPuzzle> REGISTERED_PUZZLES = new ConcurrentHashMap<>();
  private static final Map<UUID, Set<String>> SOLVED_PUZZLES = new ConcurrentHashMap<>();

  /**
   * Register a new puzzle.
   *
   * @param puzzle The puzzle to register
   * @return true if the puzzle was registered successfully, false if a puzzle with the same ID
   *     already exists
   */
  public static boolean registerPuzzle(BookPuzzle puzzle) {
    if (puzzle == null || puzzle.puzzleId == null || puzzle.puzzleId.isEmpty()) {
      CHEX.LOGGER.error("Cannot register puzzle with null or empty ID");
      return false;
    }

    if (REGISTERED_PUZZLES.containsKey(puzzle.puzzleId)) {
      CHEX.LOGGER.warn("Puzzle with ID {} is already registered", puzzle.puzzleId);
      return false;
    }

    REGISTERED_PUZZLES.put(puzzle.puzzleId, puzzle);
    return true;
  }

  /**
   * Get a puzzle by its ID.
   *
   * @param puzzleId The ID of the puzzle to retrieve
   * @return The puzzle, or null if not found
   */
  @Nullable
  public static BookPuzzle getPuzzle(String puzzleId) {
    return REGISTERED_PUZZLES.get(puzzleId);
  }

  /**
   * Get all registered puzzles.
   *
   * @return An immutable map of all registered puzzles
   */
  public static Map<String, BookPuzzle> getAllPuzzles() {
    return ImmutableMap.copyOf(REGISTERED_PUZZLES);
  }

  /**
   * Check if a player has solved a specific puzzle.
   *
   * @param player The player to check
   * @param puzzleId The ID of the puzzle to check
   * @return true if the player has solved the puzzle, false otherwise
   */
  public static boolean hasPlayerSolved(Player player, String puzzleId) {
    return SOLVED_PUZZLES.getOrDefault(player.getUUID(), Collections.emptySet()).contains(puzzleId);
  }

  /**
   * Get all puzzles that the player can currently attempt.
   *
   * @param player The player to check
   * @return A map of puzzle IDs to puzzles that the player can attempt
   */
  public static Map<String, BookPuzzle> getAvailablePuzzles(Player player) {
    Map<String, BookPuzzle> available = new HashMap<>();

    for (Map.Entry<String, BookPuzzle> entry : REGISTERED_PUZZLES.entrySet()) {
      String puzzleId = entry.getKey();
      BookPuzzle puzzle = entry.getValue();

      if (!hasPlayerSolved(player, puzzleId) && puzzle.canAttempt(player)) {
        available.put(puzzleId, puzzle);
      }
    }

    return available;
  }

  /**
   * Attempt to solve a puzzle.
   *
   * @param player The player attempting the puzzle
   * @param puzzleId The ID of the puzzle to attempt
   * @param book The book item stack (for context)
   * @param solution The player's solution
   * @return true if the puzzle was solved successfully, false otherwise
   */
  public static boolean attemptPuzzle(
      Player player, String puzzleId, ItemStack book, String[] solution) {
    BookPuzzle puzzle = getPuzzle(puzzleId);
    if (puzzle == null) {
      CHEX.LOGGER.warn(
          "Player {} attempted non-existent puzzle {}", player.getScoreboardName(), puzzleId);
      return false;
    }

    if (hasPlayerSolved(player, puzzleId)) {
      player.sendSystemMessage(
          Component.translatable("chex.puzzle.already_solved")
              .withStyle(style -> style.withColor(0xAAAAAA)));
      return false;
    }

    boolean solved = puzzle.attemptSolve(player, book, solution);
    if (solved) {
      SOLVED_PUZZLES
          .computeIfAbsent(player.getUUID(), k -> ConcurrentHashMap.newKeySet())
          .add(puzzleId);

      // Sync to client if needed
      if (player instanceof ServerPlayer serverPlayer) {
        syncPlayerData(serverPlayer);
      }
    }

    return solved;
  }

  /**
   * Reset a player's puzzle progress.
   *
   * @param player The player whose progress to reset
   */
  public static void resetPlayerProgress(Player player) {
    SOLVED_PUZZLES.remove(player.getUUID());

    // Sync to client if needed
    if (player instanceof ServerPlayer serverPlayer) {
      syncPlayerData(serverPlayer);
    }
  }

  /**
   * Sync puzzle data to the client.
   *
   * @param player The player to sync data for
   */
  public static void syncPlayerData(ServerPlayer player) {
    // TODO: Implement network sync
    // This would typically involve sending a packet to the client
    // with the player's solved puzzle IDs
  }

  /**
   * Load a player's puzzle data.
   *
   * @param player The player whose data to load
   */
  public static void loadPlayerData(Player player) {
    // TODO: Load from persistent storage
    // This would typically involve reading from the player's NBT data
  }

  /**
   * Save a player's puzzle data.
   *
   * @param player The player whose data to save
   */
  public static void savePlayerData(Player player) {
    // TODO: Save to persistent storage
    // This would typically involve writing to the player's NBT data
  }

  @SubscribeEvent
  public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      loadPlayerData(player);
    }
  }

  @SubscribeEvent
  public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      savePlayerData(player);
    }
  }

  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      // Re-sync data on respawn
      syncPlayerData(player);
    }
  }

  /** Register default puzzles. */
  public static void registerDefaultPuzzles() {
    // Example word search puzzle
    WordSearchPuzzle wordSearch =
        new WordSearchPuzzle.Builder()
            .withId("word_search_ancient_languages")
            .withTitle("Ancient Languages Word Search")
            .withDescription("Find the names of ancient languages hidden in the grid.")
            .withDifficulty(4)
            .addWord("ELDRITCH")
            .addWord("DRACONIC")
            .addWord("CELESTIAL")
            .addWord("PRIMORDIAL")
            .addWord("VOIDTONGUE")
            .withGridSize(15)
            .addRewardLoreFragment("chex:languages/word_search_complete")
            .build();

    registerPuzzle(wordSearch);

    // Add more default puzzles here
  }

  /** Initialize the puzzle system. */
  public static void init() {
    // Register default puzzles
    registerDefaultPuzzles();

    CHEX.LOGGER.info("Registered {} book puzzles", REGISTERED_PUZZLES.size());
  }
}
