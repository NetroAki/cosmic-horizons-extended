package com.netroaki.chex.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;

/** Runtime cache + reload entrypoint for chex-suit-hazards.json5 */
public final class SuitHazardsRuntime {
  private static volatile SuitHazardsConfigCore.Config CACHE;
  private static volatile String LAST_STATUS = "Not loaded";
  private static volatile String LAST_ERROR = null;

  private SuitHazardsRuntime() {}

  public static synchronized boolean reload(MinecraftServer server) {
    LAST_ERROR = null;
    try {
      // Prefer external override in /config
      Path cfg = server.getFile("config/chex-suit-hazards.json5").toPath();
      Optional<SuitHazardsConfigCore.Config> loaded = SuitHazardsConfigCore.load(cfg);
      if (loaded.isEmpty()) {
        // Fallback to bundled
        try (InputStream in =
            SuitHazardsRuntime.class.getResourceAsStream(
                "/data/cosmic_horizons_extended/config/chex-suit-hazards.json5")) {
          if (in == null) {
            LAST_ERROR = "Suit hazards config not found";
            LAST_STATUS = "Reload failed";
            return false;
          }
          var tmp = Files.createTempFile("chex-suit-hazards", ".json5");
          try (BufferedReader br =
              new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            Files.writeString(
                tmp,
                br.lines()
                    .reduce(
                        new StringBuilder(),
                        (sb, s) -> sb.append(s).append('\n'),
                        StringBuilder::append)
                    .toString());
          }
          loaded = SuitHazardsConfigCore.load(tmp);
          Files.deleteIfExists(tmp);
          if (loaded.isEmpty()) {
            LAST_ERROR = "Bundled suit hazards failed to parse";
            LAST_STATUS = "Reload failed";
            return false;
          }
        }
      }
      CACHE = loaded.get();
      int rules = CACHE.rules == null ? 0 : CACHE.rules.size();
      int mits = CACHE.mitigations == null ? 0 : CACHE.mitigations.size();
      LAST_STATUS = "Reload OK: " + rules + " rules, " + mits + " mitigations";
      return true;
    } catch (Exception e) {
      LAST_ERROR = e.getMessage();
      LAST_STATUS = "Reload failed";
      return false;
    }
  }

  public static SuitHazardsConfigCore.Config get() {
    return CACHE;
  }

  public static String getLastStatus() {
    return LAST_STATUS;
  }

  public static String getLastError() {
    return LAST_ERROR;
  }

  public static double mitigationForTier(
      int tier, java.util.function.Function<SuitHazardsConfigCore.SuitTierMitigation, Double> f) {
    if (CACHE == null || CACHE.mitigations == null) return 0.0;
    double best = 0.0;
    for (SuitHazardsConfigCore.SuitTierMitigation m : CACHE.mitigations) {
      if (m.tier <= tier) {
        best = Math.max(best, f.apply(m));
      }
    }
    return Math.max(0.0, Math.min(1.0, best));
  }

  public static List<SuitHazardsConfigCore.HazardRule> rules() {
    return CACHE == null ? java.util.List.of() : CACHE.rules;
  }
}
