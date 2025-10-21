package com.netroaki.chex.integration.jei;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public record PlanetResourcesRecipe(ResourceLocation planetId, List<String> resources) {
  private static final Logger LOGGER = LogUtils.getLogger();

  public static List<PlanetResourcesRecipe> fromBundledMinerals() {
    List<PlanetResourcesRecipe> out = new ArrayList<>();
    try (InputStream in =
        PlanetResourcesRecipe.class.getResourceAsStream(
            "/data/cosmic_horizons_extended/config/chex-minerals.json5")) {
      if (in == null) return out;
      StringBuilder sb = new StringBuilder();
      try (BufferedReader br =
          new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        String line;
        while ((line = br.readLine()) != null) {
          // strip simple // comments
          int idx = line.indexOf("//");
          if (idx >= 0) line = line.substring(0, idx);
          sb.append(line).append('\n');
        }
      }
      String json = sb.toString().replaceAll("/\\*.*?\\*/", ""); // strip /* */
      JsonObject root = JsonParser.parseString(json).getAsJsonObject();
      JsonObject minerals = root.getAsJsonObject("minerals");
      for (Map.Entry<String, JsonElement> e : minerals.entrySet()) {
        String pid = e.getKey();
        JsonObject planet = e.getValue().getAsJsonObject();
        List<String> lines = new ArrayList<>();
        if (planet.has("mineralTiers")) {
          for (JsonElement te : planet.getAsJsonArray("mineralTiers")) {
            JsonObject tier = te.getAsJsonObject();
            if (tier.has("distributions")) {
              for (JsonElement de : tier.getAsJsonArray("distributions")) {
                JsonObject dist = de.getAsJsonObject();
                String tag =
                    dist.has("tag")
                        ? dist.get("tag").getAsString()
                        : (dist.has("block")
                            ? dist.get("block").getAsString()
                            : dist.has("id") ? dist.get("id").getAsString() : "");
                int count = dist.has("count") ? dist.get("count").getAsInt() : 0;
                lines.add(tag + " x" + count);
                if (lines.size() >= 8) break; // cap for display brevity
              }
            }
            if (lines.size() >= 8) break;
          }
        }
        out.add(new PlanetResourcesRecipe(new ResourceLocation(pid), lines));
        if (out.size() >= 8) {
          // limit entries to keep JEI list manageable without pagination setup
          // still enough to demonstrate integration
          break;
        }
      }
    } catch (Exception ex) {
      LOGGER.warn("Failed reading bundled minerals for JEI: {}", ex.toString());
    }
    return out;
  }
}
