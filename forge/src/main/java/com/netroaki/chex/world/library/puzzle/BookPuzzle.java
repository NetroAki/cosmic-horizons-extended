package com.netroaki.chex.world.library.puzzle;

import com.netroaki.chex.capability.ILoreKnowledge;
import com.netroaki.chex.capability.LoreKnowledgeImpl;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Base class for all book-based puzzles in the Infinite Library. Extend this class to create
 * specific puzzle types.
 */
public abstract class BookPuzzle {
  protected final String puzzleId;
  protected final String title;
  protected final String description;
  protected final int difficulty; // 1-10 scale
  protected final List<String> requiredLoreFragments;
  protected final List<String> rewardLoreFragments;

  protected BookPuzzle(
      String puzzleId,
      String title,
      String description,
      int difficulty,
      List<String> requiredLoreFragments,
      List<String> rewardLoreFragments) {
    this.puzzleId = puzzleId;
    this.title = title;
    this.description = description;
    this.difficulty = Math.min(10, Math.max(1, difficulty));
    this.requiredLoreFragments = List.copyOf(requiredLoreFragments);
    this.rewardLoreFragments = List.copyOf(rewardLoreFragments);
  }

  /** Check if the player can attempt this puzzle. */
  public boolean canAttempt(Player player) {
    if (requiredLoreFragments.isEmpty()) {
      return true;
    }

    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);
    return requiredLoreFragments.stream().allMatch(knowledge::hasLoreFragment);
  }

  /**
   * Called when the player attempts to solve the puzzle.
   *
   * @return true if the puzzle was solved successfully
   */
  public boolean attemptSolve(Player player, ItemStack book, String[] solution) {
    if (!canAttempt(player)) {
      player.sendSystemMessage(
          Component.translatable("chex.puzzle.missing_requirements")
              .withStyle(style -> style.withColor(0xFF5555)));
      return false;
    }

    if (isCorrectSolution(solution)) {
      onSolve(player, book);
      return true;
    } else {
      onFail(player);
      return false;
    }
  }

  /** Check if the provided solution is correct. */
  protected abstract boolean isCorrectSolution(String[] solution);

  /** Called when the puzzle is solved successfully. */
  protected void onSolve(Player player, ItemStack book) {
    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);
    rewardLoreFragments.forEach(
        fragment -> {
          if (!knowledge.hasLoreFragment(fragment)) {
            knowledge.addLoreFragment(fragment);
          }
        });

    // Sync to client
    if (player instanceof ServerPlayer serverPlayer) {
      knowledge.sync(serverPlayer);
    }

    player.sendSystemMessage(
        Component.translatable("chex.puzzle.solved").withStyle(style -> style.withColor(0x55FF55)));
  }

  /** Called when the puzzle solution is incorrect. */
  protected void onFail(Player player) {
    player.sendSystemMessage(
        Component.translatable("chex.puzzle.failed").withStyle(style -> style.withColor(0xFF5555)));
  }

  /** Get the puzzle's title. */
  public String getTitle() {
    return title;
  }

  /** Get the puzzle's description. */
  public String getDescription() {
    return description;
  }

  /** Get the puzzle's difficulty (1-10). */
  public int getDifficulty() {
    return difficulty;
  }

  /** Get the list of required lore fragment IDs. */
  public List<String> getRequiredLoreFragments() {
    return requiredLoreFragments;
  }

  /** Get the list of reward lore fragment IDs. */
  public List<String> getRewardLoreFragments() {
    return rewardLoreFragments;
  }

  /** Create a unique ID for a puzzle. */
  public static String createPuzzleId() {
    return "puzzle_" + UUID.randomUUID().toString().substring(0, 8);
  }
}
