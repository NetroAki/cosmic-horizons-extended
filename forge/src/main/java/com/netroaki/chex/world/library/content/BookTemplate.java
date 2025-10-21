package com.netroaki.chex.world.library.content;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.RandomSource;

public record BookTemplate(
    String titleTemplate, List<PageTemplate> pages, Map<String, String> variables) {
  public static final Codec<BookTemplate> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.STRING.fieldOf("title").forGetter(BookTemplate::titleTemplate),
                      PageTemplate.CODEC.listOf().fieldOf("pages").forGetter(BookTemplate::pages),
                      Codec.unboundedMap(Codec.STRING, Codec.STRING)
                          .optionalFieldOf("variables", Map.of())
                          .forGetter(BookTemplate::variables))
                  .apply(instance, BookTemplate::new));

  private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

  public BookContent generateContent(RandomSource random, BookContentGenerator generator) {
    // Process variables
    Map<String, String> resolvedVars = new HashMap<>();
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      resolvedVars.put(entry.getKey(), resolveVariables(entry.getValue(), random, generator));
    }

    // Generate title
    String title = resolveVariables(titleTemplate, random, generator, resolvedVars);

    // Generate pages
    List<String> generatedPages = new ArrayList<>();
    for (PageTemplate page : pages) {
      generatedPages.add(page.generateContent(random, generator, resolvedVars));
    }

    return new BookContent(title, generatedPages);
  }

  private String resolveVariables(
      String input, RandomSource random, BookContentGenerator generator) {
    return resolveVariables(input, random, generator, Map.of());
  }

  private String resolveVariables(
      String input,
      RandomSource random,
      BookContentGenerator generator,
      Map<String, String> additionalVars) {
    if (input == null) return "";

    String result = input;
    Matcher matcher = VARIABLE_PATTERN.matcher(result);
    StringBuffer sb = new StringBuffer();

    while (matcher.find()) {
      String varName = matcher.group(1);
      String replacement =
          additionalVars.getOrDefault(varName, variables.getOrDefault(varName, ""));

      // Handle special variables
      if (replacement.startsWith("wordbank:")) {
        replacement = generator.getRandomWord(replacement.substring(9), random);
      } else if (replacement.startsWith("choose:")) {
        String[] options = replacement.substring(7).split(",");
        replacement = options[random.nextInt(options.length)].trim();
      }

      matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
    }

    matcher.appendTail(sb);
    return sb.toString();
  }

  public record PageTemplate(String template, List<PageComponent> components) {
    public static final Codec<PageTemplate> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        Codec.STRING.fieldOf("template").forGetter(PageTemplate::template),
                        PageComponent.CODEC
                            .listOf()
                            .optionalFieldOf("components", List.of())
                            .forGetter(PageTemplate::components))
                    .apply(instance, PageTemplate::new));

    public String generateContent(
        RandomSource random, BookContentGenerator generator, Map<String, String> variables) {
      String result = template;
      for (PageComponent component : components) {
        result = component.apply(result, random, generator, variables);
      }
      return result;
    }
  }

  public interface PageComponent
      extends Function<
          String,
          Function<
              RandomSource,
              Function<BookContentGenerator, Function<Map<String, String>, String>>>> {
    Codec<PageComponent> CODEC =
        Codec.STRING.dispatch(
            "type",
            component -> component instanceof TextComponent ? "text" : "replace",
            type ->
                switch (type) {
                  case "text" -> TextComponent.CODEC;
                  case "replace" -> ReplaceComponent.CODEC;
                  default -> throw new IllegalStateException("Unknown component type: " + type);
                });

    @Override
    default Function<
            RandomSource, Function<BookContentGenerator, Function<Map<String, String>, String>>>
        apply(String input) {
      return random -> generator -> vars -> apply(input, random, generator, vars);
    }

    String apply(
        String input,
        RandomSource random,
        BookContentGenerator generator,
        Map<String, String> variables);
  }

  public record TextComponent(String text) implements PageComponent {
    public static final Codec<TextComponent> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(Codec.STRING.fieldOf("text").forGetter(TextComponent::text))
                    .apply(instance, TextComponent::new));

    @Override
    public String apply(
        String input,
        RandomSource random,
        BookContentGenerator generator,
        Map<String, String> variables) {
      return input.replace("${text}", text);
    }
  }

  public record ReplaceComponent(String target, String replacement) implements PageComponent {
    public static final Codec<ReplaceComponent> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        Codec.STRING.fieldOf("target").forGetter(ReplaceComponent::target),
                        Codec.STRING
                            .fieldOf("replacement")
                            .forGetter(ReplaceComponent::replacement))
                    .apply(instance, ReplaceComponent::new));

    @Override
    public String apply(
        String input,
        RandomSource random,
        BookContentGenerator generator,
        Map<String, String> variables) {
      return input.replace(target, replacement);
    }
  }
}
