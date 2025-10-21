package com.netroaki.chex.config;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Parser for chex-suit-hazards.json5 */
public final class SuitHazardsConfigCore {

  public static final class HazardRule {
    public final String idOrTag; // planet id or biome tag
    public final String scope; // "planet" | "biome_tag"
    public final double vacuum;
    public final double thermal;
    public final double radiation;
    public final double corrosive;
    public final double pressure;

    public HazardRule(
        String idOrTag,
        String scope,
        double vacuum,
        double thermal,
        double radiation,
        double corrosive,
        double pressure) {
      this.idOrTag = idOrTag;
      this.scope = scope;
      this.vacuum = vacuum;
      this.thermal = thermal;
      this.radiation = radiation;
      this.corrosive = corrosive;
      this.pressure = pressure;
    }
  }

  public static final class SuitTierMitigation {
    public final int tier;
    public final double vacuum;
    public final double thermal;
    public final double radiation;
    public final double corrosive;
    public final double pressure;

    public SuitTierMitigation(
        int tier,
        double vacuum,
        double thermal,
        double radiation,
        double corrosive,
        double pressure) {
      this.tier = tier;
      this.vacuum = vacuum;
      this.thermal = thermal;
      this.radiation = radiation;
      this.corrosive = corrosive;
      this.pressure = pressure;
    }
  }

  public static final class Config {
    public final List<HazardRule> rules;
    public final List<SuitTierMitigation> mitigations;

    public Config(List<HazardRule> rules, List<SuitTierMitigation> mitigations) {
      this.rules = rules;
      this.mitigations = mitigations;
    }
  }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private SuitHazardsConfigCore() {}

  public static Optional<Config> load(Path file) {
    try {
      if (!Files.exists(file)) return Optional.empty();
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(reader);
        jr.setLenient(true);
        JsonObject root = GSON.fromJson(jr, JsonObject.class);
        if (root == null) return Optional.empty();
        List<HazardRule> rules = new ArrayList<>();
        List<SuitTierMitigation> mitigations = new ArrayList<>();
        if (root.has("rules") && root.get("rules").isJsonArray()) {
          for (JsonElement e : root.getAsJsonArray("rules")) {
            if (!e.isJsonObject()) continue;
            JsonObject o = e.getAsJsonObject();
            String id = o.has("id") ? o.get("id").getAsString() : "";
            String scope = o.has("scope") ? o.get("scope").getAsString() : "planet";
            double vacuum = o.has("vacuum") ? o.get("vacuum").getAsDouble() : 0;
            double thermal = o.has("thermal") ? o.get("thermal").getAsDouble() : 0;
            double radiation = o.has("radiation") ? o.get("radiation").getAsDouble() : 0;
            double corrosive = o.has("corrosive") ? o.get("corrosive").getAsDouble() : 0;
            double pressure = o.has("pressure") ? o.get("pressure").getAsDouble() : 0;
            rules.add(new HazardRule(id, scope, vacuum, thermal, radiation, corrosive, pressure));
          }
        }
        if (root.has("suitMitigations") && root.get("suitMitigations").isJsonArray()) {
          for (JsonElement e : root.getAsJsonArray("suitMitigations")) {
            if (!e.isJsonObject()) continue;
            JsonObject o = e.getAsJsonObject();
            int tier = o.has("tier") ? o.get("tier").getAsInt() : 0;
            double vacuum = o.has("vacuum") ? o.get("vacuum").getAsDouble() : 0;
            double thermal = o.has("thermal") ? o.get("thermal").getAsDouble() : 0;
            double radiation = o.has("radiation") ? o.get("radiation").getAsDouble() : 0;
            double corrosive = o.has("corrosive") ? o.get("corrosive").getAsDouble() : 0;
            double pressure = o.has("pressure") ? o.get("pressure").getAsDouble() : 0;
            mitigations.add(
                new SuitTierMitigation(tier, vacuum, thermal, radiation, corrosive, pressure));
          }
        }
        return Optional.of(new Config(rules, mitigations));
      }
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
