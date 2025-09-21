package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class FallbackOres {
  public static final String FILE_NAME = "chex/fallback_ores.json5";
  private static final String SAMPLE =
      "{\n"
          + "  \"gtceu:ores/titanium\": \"minecraft:iron_ore\",\n"
          + "  \"gtceu:ores/tungsten\": \"minecraft:ancient_debris\",\n"
          + "  \"gtceu:ores/molybdenum\": \"minecraft:deepslate_coal_ore\"\n"
          + "}\n";

  private final Map<String, String> tagToBlock = new HashMap<>();

  private FallbackOres() {}

  public static FallbackOres load(Path configDir) {
    FallbackOres data = new FallbackOres();
    try {
      Path path = configDir.resolve(FILE_NAME);
      File file = path.toFile();
      if (!file.exists()) {
        file.getParentFile().mkdirs();
        Files.writeString(path, SAMPLE);
        CHEX.LOGGER.warn("CHEX wrote sample fallback ores to {}.", path);
        return data;
      }
      var core = FallbackOresCore.load(path);
      core.ifPresent(map -> data.tagToBlock.putAll(map));
    } catch (Exception ex) {
      CHEX.LOGGER.error("Failed to read fallback ores: {}", ex.toString());
    }
    return data;
  }

  public String getBlockForTag(String tag) {
    return tagToBlock.get(tag);
  }
}
