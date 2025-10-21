package com.netroaki.chex.world.library;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.netroaki.chex.item.library.LibraryBookItem;
import com.netroaki.chex.world.library.translation.AncientTextTranslator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.util.RandomSource;

public class LibraryBookGenerator {
  private static final List<String> ANCIENT_AUTHORS =
      ImmutableList.of(
          "Archivist Thel'aran",
          "Loremaster Vaelith",
          "Scribe Nal'themar",
          "Historian Dath'remar",
          "Chronomancer Anachronos",
          "Archon Maladaar",
          "Keeper Uther",
          "Prophet Velen",
          "Archimonde the Defiler",
          "Kael'thas Sunstrider");

  private static final List<String> BOOK_TITLES =
      ImmutableList.of(
          "The Lost Tomes of %s",
          "Chronicles of the %s",
          "The %s Manuscript",
          "Secrets of the %s",
          "The %s Codex",
          "The %s Tome",
          "Legends of the %s",
          "The %s Grimoire",
          "Mysteries of the %s",
          "The %s Archives");

  private static final List<String> BOOK_SUBJECTS =
      ImmutableList.of(
          "Forgotten Kingdoms",
          "Ancient Ones",
          "Eternal Library",
          "Celestial Spheres",
          "Arcane Runes",
          "Eldritch Horrors",
          "Infinite Realms",
          "Temporal Paradoxes",
          "Voidborn Entities",
          "Cosmic Truths");

  private static final List<String> LORE_FRAGMENTS =
      ImmutableList.of(
          "fragment.void_whisper",
          "fragment.star_born",
          "fragment.eldritch_truth",
          "fragment.cosmic_balance",
          "fragment.infinite_library",
          "fragment.temporal_paradox",
          "fragment.arcane_secrets",
          "fragment.forgotten_gods",
          "fragment.reality_fabric",
          "fragment.dimensional_rifts");

  private static final Map<String, List<String>> PAGE_TEMPLATES =
      ImmutableMap.of(
          "history",
              ImmutableList.of(
                  "In the age of %s, the %s rose to power, wielding the %s to shape the world.",
                  "The great war between %s and %s was fought over the %s, a conflict that lasted"
                      + " %d years.",
                  "Ancient texts speak of %s, a legendary figure who discovered the %s and was"
                      + " forever changed.",
                  "The ruins of %s still stand as a testament to the %s civilization that once"
                      + " thrived there.",
                  "Scholars debate the true meaning of the %s, but all agree it holds the key to"
                      + " understanding %s."),
          "spell",
              ImmutableList.of(
                  "To invoke the power of %s, one must first gather %d %s under the light of a %s"
                      + " moon.",
                  "The incantation for %s requires precise pronunciation of the %s runes in the"
                      + " correct sequence.",
                  "Warning: The spell of %s is forbidden by the %s due to its ability to %s.",
                  "The ancient mage %s was said to have mastered the art of %s using only a %s.",
                  "When the stars align in the constellation of %s, the spell of %s can be cast"
                      + " with increased potency."),
          "bestiary",
              ImmutableList.of(
                  "The %s is a fearsome creature that inhabits the %s, known for its ability to"
                      + " %s.",
                  "Few have survived an encounter with the %s, a being of pure %s that feeds on"
                      + " %s.",
                  "The %s was once thought to be a myth until the %s expedition discovered %d"
                      + " specimens in the %s.",
                  "To tame a %s, one must first prove their worth by %s while holding a %s.",
                  "The last known sighting of the %s was recorded in the %s, where it was seen"
                      + " %s."),
          "alchemy",
              ImmutableList.of(
                  "The elixir of %s requires the following rare ingredients: %s, %s, and a single"
                      + " %s.",
                  "When combined under a %s moon, %s and %s will produce a potion that can %s.",
                  "The ancient alchemist %s discovered that %s has the power to %s when exposed to"
                      + " %s.",
                  "Warning: The transmutation of %s into %s is highly unstable and may result in"
                      + " %s.",
                  "The philosopher's stone is said to be hidden within the %s, guarded by %s who"
                      + " %s."));

  private static final List<String> ADJECTIVES =
      ImmutableList.of(
          "forgotten",
          "ancient",
          "mystical",
          "arcane",
          "forbidden",
          "lost",
          "hidden",
          "secret",
          "sacred",
          "cursed");

  private static final List<String> NOUNS =
      ImmutableList.of(
          "knowledge",
          "power",
          "truth",
          "wisdom",
          "secrets",
          "artifacts",
          "spells",
          "rituals",
          "prophecies",
          "realms");

  private static final Random RANDOM = new Random();

  public static LibraryBookItem.BookContent generateBookContent(
      RandomSource random, boolean includeLore) {
    // Determine if this is an ancient text (30% chance)
    boolean isAncientText = random.nextFloat() < 0.3f;
    String languageId = "common";
    boolean isTranslated = true;
    String originalLanguage = "Common";

    // If it's an ancient text, select a random ancient language
    if (isAncientText) {
      List<String> languageIds =
          new ArrayList<>(
              AncientTextTranslator.getLanguages().stream().map(lang -> lang.getId()).toList());
      languageId = getRandomElement(languageIds, random);
      isTranslated = random.nextFloat() < 0.3f; // 30% chance to be pre-translated
      originalLanguage =
          "Ancient " + languageId.substring(0, 1).toUpperCase() + languageId.substring(1);
    }

    // Generate book title and author
    String title =
        String.format(
            getRandomElement(BOOK_TITLES, random), getRandomElement(BOOK_SUBJECTS, random));
    String author = getRandomElement(ANCIENT_AUTHORS, random);

    // Determine book type and generate appropriate pages
    String bookType = getRandomElement(new ArrayList<>(PAGE_TEMPLATES.keySet()), random);
    List<String> pages = generatePages(bookType, 3 + random.nextInt(5), random);

    // Add a lore fragment with a small chance (higher for ancient texts)
    List<String> loreFragments = new ArrayList<>();
    float loreChance = isAncientText ? 0.5f : 0.1f; // 50% for ancient, 10% for common
    if (includeLore && random.nextFloat() < loreChance) {
      loreFragments.add(getRandomElement(LORE_FRAGMENTS, random));
    }

    // Determine generation (0 = original, 1 = copy, 2 = copy of a copy, etc.)
    int generation = 0;
    if (!isAncientText && random.nextFloat() < 0.5f) {
      generation = random.nextInt(3); // 0, 1, or 2 (only for common books)
    }

    // Determine the discoverers (for ancient texts)
    List<String> discoverers = new ArrayList<>();
    if (isAncientText) {
      discoverers.add(getRandomElement(ANCIENT_AUTHORS, random));
      // 10% chance to have multiple discoverers
      while (random.nextFloat() < 0.1f) {
        String newDiscoverer;
        do {
          newDiscoverer = getRandomElement(ANCIENT_AUTHORS, random);
        } while (discoverers.contains(newDiscoverer) && discoverers.size() < 3);
        discoverers.add(newDiscoverer);
      }
    }

    return new LibraryBookItem.BookContent(
        title,
        author,
        pages,
        generation,
        loreFragments,
        isTranslated,
        originalLanguage,
        discoverers,
        languageId,
        isAncientText);
  }

  private static List<String> generatePages(String bookType, int pageCount, RandomSource random) {
    List<String> templates = PAGE_TEMPLATES.get(bookType);
    if (templates == null || templates.isEmpty()) {
      return Collections.singletonList("This book appears to be blank...");
    }

    return IntStream.range(0, pageCount)
        .mapToObj(
            i -> {
              String template = getRandomElement(templates, random);
              return fillTemplate(template, random);
            })
        .collect(Collectors.toList());
  }

  private static String fillTemplate(String template, RandomSource random) {
    // This is a simplified version - in a real implementation, you'd want to handle
    // different placeholders more intelligently
    String[] parts = template.split("%s");
    if (parts.length == 1) {
      return template; // No placeholders to fill
    }

    StringBuilder result = new StringBuilder(parts[0]);
    for (int i = 1; i < parts.length; i++) {
      // Simple placeholder replacement - in a real implementation, you'd want to be more
      // sophisticated
      String replacement;
      switch (random.nextInt(5)) {
        case 0 -> replacement =
            getRandomElement(ADJECTIVES, random) + " " + getRandomElement(NOUNS, random);
        case 1 -> replacement = getRandomElement(ANCIENT_AUTHORS, random);
        case 2 -> replacement = getRandomElement(BOOK_SUBJECTS, random);
        case 3 -> replacement = String.valueOf(100 + random.nextInt(900));
        default -> replacement = getRandomElement(NOUNS, random);
      }
      result.append(replacement).append(parts[i]);
    }

    return result.toString();
  }

  private static <T> T getRandomElement(List<T> list, RandomSource random) {
    if (list == null || list.isEmpty()) {
      throw new IllegalArgumentException("List cannot be null or empty");
    }
    return list.get(random.nextInt(list.size()));
  }

  // Helper method to generate a book with a specific lore fragment for testing
  public static LibraryBookItem.BookContent generateBookWithLore(
      String loreKey, RandomSource random) {
    LibraryBookItem.BookContent content = generateBook(random, false);
    return new LibraryBookItem.BookContentBuilder()
        .setTitle(content.getTitle())
        .setAuthor(content.getAuthor())
        .setPages(content.getPages())
        .setGeneration(content.getGeneration())
        .setLoreFragment(loreKey)
        .setTranslated(content.isTranslated())
        .setOriginalLanguage(content.getOriginalLanguage())
        .build();
  }
}
