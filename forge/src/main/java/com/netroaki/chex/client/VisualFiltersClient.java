package com.netroaki.chex.client;

import com.mojang.logging.LogUtils;
import com.netroaki.chex.config.VisualFiltersConfigCore;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VisualFiltersClient {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static volatile VisualFiltersConfigCore.Config CFG;

  static {
    reload();
  }

  public static void reload() {
    try {
      // Try external override
      Path cfg =
          Minecraft.getInstance()
              .gameDirectory
              .toPath()
              .resolve("config/chex-visual-filters.json5");
      Optional<VisualFiltersConfigCore.Config> loaded = VisualFiltersConfigCore.load(cfg);
      if (loaded.isEmpty()) {
        try (InputStream in =
            VisualFiltersClient.class.getResourceAsStream(
                "/data/cosmic_horizons_extended/config/chex-visual-filters.json5")) {
          if (in != null) {
            var tmp = Files.createTempFile("chex-visual-filters", ".json5");
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
            loaded = VisualFiltersConfigCore.load(tmp);
            Files.deleteIfExists(tmp);
          }
        }
      }
      CFG = loaded.orElse(null);
    } catch (Exception e) {
      LOGGER.warn("CHEX VisualFilters: reload failed: {}", e.toString());
      CFG = null;
    }
  }

  @SubscribeEvent
  public static void onFogColor(ViewportEvent.ComputeFogColor event) {
    if (CFG == null || CFG.entries == null) return;
    ResourceKey<Level> dim = event.getCamera().getEntity().level().dimension();
    String dimId = dim.location().toString();
    for (VisualFiltersConfigCore.Entry e : CFG.entries) {
      if (e.dimension.equals(dimId)) {
        // Apply fog color override (simple linear blend with current color)
        float r = (float) e.fogR;
        float g = (float) e.fogG;
        float b = (float) e.fogB;
        // Blend 50% for subtle effect
        event.setRed(event.getRed() * 0.5f + r * 0.5f);
        event.setGreen(event.getGreen() * 0.5f + g * 0.5f);
        event.setBlue(event.getBlue() * 0.5f + b * 0.5f);
        break;
      }
    }
  }
}
