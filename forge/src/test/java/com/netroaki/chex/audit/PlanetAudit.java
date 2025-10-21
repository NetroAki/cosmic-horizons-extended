package com.netroaki.chex.audit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Audits planet definitions and discovery logs for consistency and correctness. */
public class PlanetAudit {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private final List<PlanetDefinition> planets = new ArrayList<>();
  private final List<String> issues = new ArrayList<>();
  private final Map<String, List<PlanetDefinition>> duplicateIds = new HashMap<>();
  private final Set<String> missingLocalizations = new HashSet<>();
  private Map<String, String> localizations = new HashMap<>();

  public static void main(String[] args) {
    new PlanetAudit().run();
  }

  public void run() {
    System.out.println("Starting planet definition audit...");

    try {
      // Load localizations
      loadLocalizations();

      // Extract and validate planet definitions
      extractPlanetDefinitions();
      checkDuplicateIds();
      checkLocalizations();

      // Generate and save report
      String report = generateReport();
      System.out.println("\n" + report);

      Path reportPath = Paths.get("reports/planet_audit_report.txt");
      Files.createDirectories(reportPath.getParent());
      Files.write(reportPath, report.getBytes(StandardCharsets.UTF_8));

      System.out.println("\nAudit complete! Report saved to: " + reportPath.toAbsolutePath());
    } catch (Exception e) {
      System.err.println("Error during audit: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void loadLocalizations() throws IOException {
    Path langFile =
        Paths.get("forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json");
    if (Files.exists(langFile)) {
      String json = new String(Files.readAllBytes(langFile), StandardCharsets.UTF_8);
      JsonObject langJson = JsonParser.parseString(json).getAsJsonObject();
      langJson.entrySet().forEach(e -> localizations.put(e.getKey(), e.getValue().getAsString()));
    } else {
      issues.add("Warning: Could not find language file at " + langFile);
    }
  }

  private void extractPlanetDefinitions() throws IOException {
    Path planetFile =
        Paths.get("forge/src/main/java/com/netroaki/chex/registry/PlanetRegistry.java");
    if (!Files.exists(planetFile)) {
      issues.add("Error: Could not find PlanetRegistry.java at " + planetFile);
      return;
    }

    String content = new String(Files.readAllBytes(planetFile), StandardCharsets.UTF_8);
    Pattern pattern = Pattern.compile("new\\s+PlanetDef\\s*\\(([^;]+?)\\)", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(content);

    while (matcher.find()) {
      try {
        String argsStr = matcher.group(1).replaceAll("\\s+", " ").trim();
        // Split by commas not inside square brackets
        String[] args = argsStr.split(",(?![^\\[\\]]*\\])");

        // Parse ID (first argument)
        String id = parseResourceLocation(args[0]);
        if (id == null) {
          issues.add("Could not parse planet ID from: " + args[0]);
          continue;
        }

        // Parse name (second argument)
        String name = args[1].trim().replaceAll("^\"|\"$", "");

        planets.add(
            new PlanetDefinition(
                id,
                name,
                Paths.get("forge/src/main/java/com/netroaki/chex/registry/PlanetRegistry.java"),
                countLines(content.substring(0, matcher.start()))));

      } catch (Exception e) {
        issues.add(
            "Error parsing planet definition: "
                + e.getMessage()
                + "\nArgs: "
                + (matcher.group(1).length() > 100
                    ? matcher.group(1).substring(0, 100) + "..."
                    : matcher.group(1)));
      }
    }
  }

  private String parseResourceLocation(String input) {
    Pattern pattern =
        Pattern.compile(
            "ResourceLocation\\.fromNamespaceAndPath\\(\\s*\"([^\"]+)\"\\s*,\\s*\"([^\"]+)\"\\s*\\)");
    Matcher matcher = pattern.matcher(input);
    return matcher.find() ? matcher.group(1) + ":" + matcher.group(2) : null;
  }

  private int countLines(String text) {
    return (int) text.chars().filter(c -> c == '\n').count() + 1;
  }

  private void checkDuplicateIds() {
    Map<String, List<PlanetDefinition>> idMap = new HashMap<>();

    for (PlanetDefinition planet : planets) {
      idMap.computeIfAbsent(planet.id, k -> new ArrayList<>()).add(planet);
    }

    idMap.entrySet().stream()
        .filter(entry -> entry.getValue().size() > 1)
        .forEach(entry -> duplicateIds.put(entry.getKey(), entry.getValue()));
  }

  private void checkLocalizations() {
    for (PlanetDefinition planet : planets) {
      String nameKey = "planet." + planet.id.replace(":", ".");
      if (!localizations.containsKey(nameKey)) {
        missingLocalizations.add(planet.id + ": Missing name localization '" + nameKey + "'");
      }
    }
  }

  private String generateReport() {
    StringBuilder report = new StringBuilder();
    report.append("=".repeat(80)).append("\n");
    report.append("PLANET DEFINITION AUDIT REPORT\n");
    report.append("=".repeat(80)).append("\n\n");

    report.append("Total planets found: ").append(planets.size()).append("\n\n");

    // Report duplicate IDs
    if (!duplicateIds.isEmpty()) {
      report.append("[ERROR] DUPLICATE PLANET IDs FOUND:\n");
      for (Map.Entry<String, List<PlanetDefinition>> entry : duplicateIds.entrySet()) {
        report.append(
            String.format("\n  %s (%d occurrences):\n", entry.getKey(), entry.getValue().size()));
        for (PlanetDefinition planet : entry.getValue()) {
          report.append(
              String.format("    - %s:%d\n", planet.sourceFile.getFileName(), planet.lineNumber));
        }
      }
      report.append("\n");
    } else {
      report.append("✅ No duplicate planet IDs found\n\n");
    }

    // Report missing localizations
    if (!missingLocalizations.isEmpty()) {
      report.append("[ERROR] MISSING LOCALIZATIONS:\n");
      missingLocalizations.stream()
          .sorted()
          .forEach(loc -> report.append("  - ").append(loc).append("\n"));
      report.append("\n");
    } else {
      report.append("✅ All planets have proper localizations\n\n");
    }

    // Report discovery log status
    report.append("DISCOVERY LOG STATUS:\n");
    report.append("  [WARN] Discovery log validation not yet implemented\n");

    // Report any issues encountered during processing
    if (!issues.isEmpty()) {
      report.append("\n").append("[WARN] ISSUES ENCOUNTERED:").append("\n");
      issues.forEach(issue -> report.append("  - ").append(issue).append("\n"));
    }

    return report.toString();
  }

  private static class PlanetDefinition {
    final String id;
    final String name;
    final Path sourceFile;
    final int lineNumber;

    PlanetDefinition(String id, String name, Path sourceFile, int lineNumber) {
      this.id = id;
      this.name = name;
      this.sourceFile = sourceFile;
      this.lineNumber = lineNumber;
    }
  }
}
