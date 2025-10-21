package com.netroaki.chex.world.library.translation;

import com.netroaki.chex.capability.ILoreKnowledge;
import com.netroaki.chex.capability.LoreKnowledgeImpl;
import java.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Handles the translation of ancient texts in the Infinite Library. Supports multiple ancient
 * languages with different translation difficulties.
 */
public class AncientTextTranslator {
  private static final Map<String, AncientLanguage> LANGUAGES = new HashMap<>();
  private static final Random RANDOM = new Random();
  private static final int KNOWLEDGE_THRESHOLD = 50; // Required knowledge to attempt translation
  private static final int KNOWLEDGE_GAIN = 5; // Knowledge gained per successful translation

  // Register all ancient languages
  static {
    // Ancient Eldritch - difficult, eldritch knowledge
    registerLanguage("eldritch", "Eldritch", 75, 0x4B0082); // Indigo

    // Ancient Draconic - dragon-related texts, medium difficulty
    registerLanguage("draconic", "Draconic", 60, 0x8B0000); // Dark Red

    // Celestial - divine/angelic texts, medium difficulty
    registerLanguage("celestial", "Celestial", 65, 0x4682B4); // Steel Blue

    // Primordial - elemental/primordial language, easier
    registerLanguage("primordial", "Primordial", 50, 0x2E8B57); // Sea Green

    // Voidtongue - language of the void/end, very difficult
    registerLanguage("voidtongue", "Voidtongue", 85, 0x4B0082); // Dark Violet
  }

  private static void registerLanguage(String id, String name, int difficulty, int color) {
    LANGUAGES.put(id, new AncientLanguage(id, name, difficulty, color));
  }

  /**
   * Attempts to translate a book for the player.
   *
   * @return true if the translation was successful or already complete
   */
  public static boolean tryTranslateBook(Player player, ItemStack book) {
    if (book.isEmpty() || book.getItem() != Items.WRITTEN_BOOK) {
      return false;
    }

    CompoundTag tag = book.getTag();
    if (tag == null || !tag.contains("language") || tag.getBoolean("translated")) {
      return true; // Already translated or not an ancient text
    }

    String languageId = tag.getString("language");
    if (!LANGUAGES.containsKey(languageId)) {
      return false; // Unknown language
    }

    AncientLanguage language = LANGUAGES.get(languageId);
    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);

    // Check if player can translate this language
    if (!canTranslate(player, languageId)) {
      if (player instanceof ServerPlayer) {
        player.sendSystemMessage(
            Component.translatable("chex.translate.insufficient_knowledge")
                .withStyle(Style.EMPTY.withColor(0xFF5555)));
      }
      return false;
    }

    // Mark as translated
    tag.putBoolean("translated", true);

    // Increase player's knowledge of this language
    knowledge.increaseLanguageKnowledge(languageId, KNOWLEDGE_GAIN);

    // Sync to client
    if (player instanceof ServerPlayer serverPlayer) {
      knowledge.sync(serverPlayer);
      player.sendSystemMessage(
          Component.translatable("chex.translate.success", language.getDisplayName())
              .withStyle(Style.EMPTY.withColor(0x55FF55)));
    }

    return true;
  }

  /** Checks if a player can translate a specific language. */
  public static boolean canTranslate(Player player, String languageId) {
    if (!LANGUAGES.containsKey(languageId)) {
      return false;
    }

    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);
    AncientLanguage language = LANGUAGES.get(languageId);

    return knowledge.getLanguageKnowledge(languageId) >= language.getDifficulty();
  }

  /**
   * Gets the translation progress for a specific language.
   *
   * @return A value between 0.0 and 1.0 representing the translation progress
   */
  public static float getTranslationProgress(Player player, String languageId) {
    if (!LANGUAGES.containsKey(languageId)) {
      return 0.0f;
    }

    ILoreKnowledge knowledge = LoreKnowledgeImpl.get(player);
    AncientLanguage language = LANGUAGES.get(languageId);
    int knowledgeLevel = knowledge.getLanguageKnowledge(languageId);

    return Math.min(1.0f, (float) knowledgeLevel / language.getDifficulty());
  }

  /** Gets all available ancient languages. */
  public static Collection<AncientLanguage> getLanguages() {
    return Collections.unmodifiableCollection(LANGUAGES.values());
  }

  /** Gets a specific ancient language by ID. */
  public static Optional<AncientLanguage> getLanguage(String languageId) {
    return Optional.ofNullable(LANGUAGES.get(languageId));
  }

  /** Represents an ancient language that can be translated. */
  public static class AncientLanguage {
    private final String id;
    private final String displayName;
    private final int difficulty; // Higher = harder to translate
    private final int color; // Color used for display

    public AncientLanguage(String id, String displayName, int difficulty, int color) {
      this.id = id;
      this.displayName = displayName;
      this.difficulty = difficulty;
      this.color = color;
    }

    public String getId() {
      return id;
    }

    public String getDisplayName() {
      return displayName;
    }

    public int getDifficulty() {
      return difficulty;
    }

    public int getColor() {
      return color;
    }

    public TextColor getTextColor() {
      return TextColor.fromRgb(color);
    }
  }
}
