package com.netroaki.chex.world.library.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.library.item.LibraryBookItem;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BookContentGenerator extends SimpleJsonResourceReloadListener {
  private static final Gson GSON =
      new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  private static BookContentGenerator INSTANCE;

  private final Map<LibraryBookItem.BookType, List<BookTemplate>> templates = new HashMap<>();
  private final Map<String, List<String>> wordBanks = new HashMap<>();

  public BookContentGenerator() {
    super(GSON, "library_books");
    INSTANCE = this;
  }

  public static BookContentGenerator getInstance() {
    return INSTANCE;
  }

  @Override
  protected void apply(
      Map<ResourceLocation, JsonElement> resources,
      @NotNull ResourceManager manager,
      @NotNull ProfilerFiller profiler) {
    templates.clear();
    wordBanks.clear();

    // Load word banks first
    resources.entrySet().stream()
        .filter(entry -> entry.getKey().getPath().startsWith("word_banks/"))
        .forEach(
            entry -> {
              String bankName = entry.getKey().getPath().substring("word_banks/".length());
              loadWordBank(bankName, entry.getValue().getAsJsonObject());
            });

    // Then load book templates
    resources.entrySet().stream()
        .filter(entry -> entry.getKey().getPath().startsWith("templates/"))
        .forEach(
            entry -> {
              String typeName = entry.getKey().getPath().substring("templates/".length());
              try {
                LibraryBookItem.BookType type =
                    LibraryBookItem.BookType.valueOf(typeName.toUpperCase());
                loadTemplate(type, entry.getValue().getAsJsonObject());
              } catch (IllegalArgumentException e) {
                CHEX.LOGGER.error("Unknown book type: {}", typeName);
              }
            });
  }

  private void loadWordBank(String bankName, JsonObject json) {
    List<String> words = new ArrayList<>();
    json.getAsJsonArray("words").forEach(element -> words.add(element.getAsString()));
    wordBanks.put(bankName, words);
  }

  private void loadTemplate(LibraryBookItem.BookType type, JsonObject json) {
    BookTemplate template =
        BookTemplate.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, CHEX.LOGGER::error);
    templates.computeIfAbsent(type, k -> new ArrayList<>()).add(template);
  }

  public BookContent generateContent(LibraryBookItem.BookType type, long seed) {
    RandomSource random = RandomSource.create(seed);
    List<BookTemplate> typeTemplates = templates.getOrDefault(type, Collections.emptyList());

    if (typeTemplates.isEmpty()) {
      return BookContent.EMPTY;
    }

    BookTemplate template = typeTemplates.get(random.nextInt(typeTemplates.size()));
    return template.generateContent(random, this);
  }

  public String getRandomWord(String wordBank, RandomSource random) {
    List<String> words = wordBanks.get(wordBank);
    if (words == null || words.isEmpty()) {
      return "[MISSING_WORD:" + wordBank + "]";
    }
    return words.get(random.nextInt(words.size()));
  }

  @Override
  protected @NotNull CompletableFuture<Void> prepare(
      @NotNull ResourceManager manager,
      @NotNull ProfilerFiller profiler,
      @NotNull Executor executor) {
    return super.prepare(manager, profiler, executor)
        .thenRun(
            () ->
                CHEX.LOGGER.info(
                    "Loaded {} book templates and {} word banks",
                    templates.values().stream().mapToInt(List::size).sum(),
                    wordBanks.size()));
  }

  public static void generateBookContent(ItemStack stack) {
    if (!(stack.getItem() instanceof LibraryBookItem bookItem)) {
      return;
    }

    if (!stack.hasTag() || !stack.getTag().contains("Content")) {
      long seed = stack.hasTag() ? stack.getTag().getLong("Seed") : 0;
      if (seed == 0) {
        seed = new Random().nextLong();
        stack.getOrCreateTag().putLong("Seed", seed);
      }

      BookContent content = getInstance().generateContent(bookItem.getBookType(), seed);
      stack.getOrCreateTag().put("Content", content.serializeNBT());
    }
  }
}
