package com.netroaki.chex.world.library.puzzle;

import com.google.common.collect.ImmutableList;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** A word search puzzle where players must find hidden words in a grid of letters. */
public class WordSearchPuzzle extends BookPuzzle {
  private final char[][] grid;
  private final Set<String> words;
  private final Map<String, List<int[]>> wordPositions;
  private final int size;

  public WordSearchPuzzle(
      String puzzleId,
      String title,
      String description,
      int difficulty,
      List<String> requiredLoreFragments,
      List<String> rewardLoreFragments,
      List<String> words,
      int gridSize) {
    super(puzzleId, title, description, difficulty, requiredLoreFragments, rewardLoreFragments);
    this.words = new HashSet<>(words);
    this.size = Math.max(10, Math.min(20, gridSize)); // Clamp between 10 and 20
    this.grid = new char[size][size];
    this.wordPositions = new HashMap<>();

    initializeGrid();
    placeWords();
    fillEmptySpaces();
  }

  private void initializeGrid() {
    for (int i = 0; i < size; i++) {
      Arrays.fill(grid[i], ' ');
    }
  }

  private void placeWords() {
    List<String> wordsToPlace = new ArrayList<>(words);
    Collections.shuffle(wordsToPlace);

    for (String word : wordsToPlace) {
      if (word.length() > size) continue; // Skip words that are too long

      boolean placed = false;
      int attempts = 0;
      final int maxAttempts = 50;

      while (!placed && attempts < maxAttempts) {
        attempts++;

        // Try different directions (0=horizontal, 1=vertical, 2=diagonal)
        int direction = new Random().nextInt(3);
        boolean reverse = new Random().nextBoolean();
        String wordToPlace = reverse ? new StringBuilder(word).reverse().toString() : word;

        int row = new Random().nextInt(size);
        int col = new Random().nextInt(size);

        if (canPlaceWord(wordToPlace, row, col, direction)) {
          placeWord(wordToPlace, row, col, direction);
          wordPositions.put(word, getWordPositions(wordToPlace, row, col, direction));
          placed = true;
        }
      }
    }
  }

  private boolean canPlaceWord(String word, int row, int col, int direction) {
    int dr = 0, dc = 0;

    switch (direction) {
      case 0 -> dc = 1; // Horizontal
      case 1 -> dr = 1; // Vertical
      case 2 -> {
        dc = 1;
        dr = 1;
      } // Diagonal
    }

    // Check bounds
    if (row + (word.length() - 1) * dr >= size || col + (word.length() - 1) * dc >= size) {
      return false;
    }

    // Check for conflicts
    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      int r = row + i * dr;
      int c2 = col + i * dc;

      if (grid[r][c2] != ' ' && grid[r][c2] != c) {
        return false;
      }
    }

    return true;
  }

  private void placeWord(String word, int row, int col, int direction) {
    int dr = 0, dc = 0;

    switch (direction) {
      case 0 -> dc = 1; // Horizontal
      case 1 -> dr = 1; // Vertical
      case 2 -> {
        dc = 1;
        dr = 1;
      } // Diagonal
    }

    for (int i = 0; i < word.length(); i++) {
      int r = row + i * dr;
      int c = col + i * dc;
      grid[r][c] = word.charAt(i);
    }
  }

  private List<int[]> getWordPositions(String word, int row, int col, int direction) {
    List<int[]> positions = new ArrayList<>();
    int dr = 0, dc = 0;

    switch (direction) {
      case 0 -> dc = 1; // Horizontal
      case 1 -> dr = 1; // Vertical
      case 2 -> {
        dc = 1;
        dr = 1;
      } // Diagonal
    }

    for (int i = 0; i < word.length(); i++) {
      positions.add(new int[] {row + i * dr, col + i * dc});
    }

    return positions;
  }

  private void fillEmptySpaces() {
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (grid[i][j] == ' ') {
          grid[i][j] = letters.charAt(random.nextInt(letters.length()));
        }
      }
    }
  }

  @Override
  protected boolean isCorrectSolution(String[] solution) {
    if (solution.length != 2) return false;

    String word = solution[0].toUpperCase();
    if (!words.contains(word)) return false;

    // Parse coordinates (format: "row,col")
    String[] coords = solution[1].split(",");
    if (coords.length != 2) return false;

    try {
      int row = Integer.parseInt(coords[0].trim());
      int col = Integer.parseInt(coords[1].trim());

      // Check if the word starts at these coordinates
      List<int[]> positions = wordPositions.get(word);
      if (positions == null || positions.isEmpty()) return false;

      int[] startPos = positions.get(0);
      return startPos[0] == row && startPos[1] == col;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public void onSolve(Player player, ItemStack book) {
    super.onSolve(player, book);
    player.sendSystemMessage(
        Component.translatable("chex.puzzle.word_search.solved")
            .withStyle(style -> style.withColor(0x55FF55)));
  }

  public char[][] getGrid() {
    char[][] copy = new char[size][size];
    for (int i = 0; i < size; i++) {
      System.arraycopy(grid[i], 0, copy[i], 0, size);
    }
    return copy;
  }

  public Set<String> getWords() {
    return new HashSet<>(words);
  }

  public static class Builder {
    private String puzzleId = createPuzzleId();
    private String title = "Word Search";
    private String description = "Find the hidden words in the grid.";
    private int difficulty = 5;
    private final List<String> requiredLoreFragments = new ArrayList<>();
    private final List<String> rewardLoreFragments = new ArrayList<>();
    private final List<String> words = new ArrayList<>();
    private int gridSize = 15;

    public Builder withId(String id) {
      this.puzzleId = id;
      return this;
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder withDifficulty(int difficulty) {
      this.difficulty = Math.min(10, Math.max(1, difficulty));
      return this;
    }

    public Builder addRequiredLoreFragment(String fragmentId) {
      this.requiredLoreFragments.add(fragmentId);
      return this;
    }

    public Builder addRewardLoreFragment(String fragmentId) {
      this.rewardLoreFragments.add(fragmentId);
      return this;
    }

    public Builder addWord(String word) {
      this.words.add(word.toUpperCase());
      return this;
    }

    public Builder withGridSize(int size) {
      this.gridSize = Math.max(5, Math.min(20, size));
      return this;
    }

    public WordSearchPuzzle build() {
      if (words.isEmpty()) {
        throw new IllegalStateException("Word search must have at least one word");
      }

      return new WordSearchPuzzle(
          puzzleId,
          title,
          description,
          difficulty,
          ImmutableList.copyOf(requiredLoreFragments),
          ImmutableList.copyOf(rewardLoreFragments),
          ImmutableList.copyOf(words),
          gridSize);
    }
  }
}
