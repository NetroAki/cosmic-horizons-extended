package com.netroaki.chex.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;

/** Runtime holder + reload entrypoint for chex-minerals.json5. */
public final class MineralsRuntime {
  private static volatile Map<String, java.util.List<MineralsConfigCore.DistributionEntry>> CACHE;
  private static volatile String LAST_STATUS = "Not loaded";
  private static volatile String LAST_ERROR = null;

  private MineralsRuntime() {}

  public static synchronized boolean reload(MinecraftServer server) {
    LAST_ERROR = null;
    try {
      // Preferred: external override in config folder
      Path cfg = server.getFile("config/chex-minerals.json5").toPath();
      Optional<Map<String, java.util.List<MineralsConfigCore.DistributionEntry>>> loaded =
          MineralsConfigCore.load(cfg);
      if (loaded.isEmpty()) {
        // Fallback: bundled datapack resource
        try (InputStream in =
            MineralsRuntime.class.getResourceAsStream(
                "/data/cosmic_horizons_extended/config/chex-minerals.json5")) {
          if (in == null) {
            LAST_ERROR = "Minerals config not found (config override or bundled)";
            LAST_STATUS = "Reload failed";
            return false;
          }
          // Read stream to temp file to reuse JSON5 loader
          Path tmp = Files.createTempFile("chex-minerals", ".json5");
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
          loaded = MineralsConfigCore.load(tmp);
          Files.deleteIfExists(tmp);
          if (loaded.isEmpty()) {
            LAST_ERROR = "Bundled minerals config failed to parse";
            LAST_STATUS = "Reload failed";
            return false;
          }
        }
      }
      CACHE = loaded.get();
      LAST_STATUS = "Reload OK: " + CACHE.size() + " planet entries";
      return true;
    } catch (Exception e) {
      LAST_ERROR = e.getMessage();
      LAST_STATUS = "Reload failed";
      return false;
    }
  }

  public static String getLastStatus() {
    return LAST_STATUS;
  }

  public static String getLastError() {
    return LAST_ERROR;
  }

  public static Map<String, java.util.List<MineralsConfigCore.DistributionEntry>> getCache() {
    return CACHE;
  }
}
