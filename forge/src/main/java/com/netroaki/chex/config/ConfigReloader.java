package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.travel.TravelGraph;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ConfigReloader {
    private ConfigReloader() {
    }

    public static void reloadAll() {
        Path cfg = FMLPaths.CONFIGDIR.get();

        // Reload planet registry (derived from datapacks and CHEX registry init)
        com.netroaki.chex.registry.PlanetRegistry.initialize();

        // Travel graph (chex-travel.json5)
        TravelGraph.loadFromConfigOrDefaults();

        // Minerals (chex-minerals.json5)
        var minerals = com.netroaki.chex.config.MineralsConfigCore.load(cfg.resolve("chex/chex-minerals.json5"));
        CHEX.LOGGER.info("[CHEX] Minerals config {}", minerals.isPresent() ? "loaded" : "not found");

        // Fallback ores (fallback_ores.json5)
        var fallback = com.netroaki.chex.config.FallbackOresCore.load(cfg.resolve("chex/fallback_ores.json5"));
        CHEX.LOGGER.info("[CHEX] Fallback ores {} ({} entries)",
                fallback.isPresent() ? "loaded" : "not found",
                fallback.map(java.util.Map::size).orElse(0));

        // Planet overrides (chex-planets.json5)
        var overrides = com.netroaki.chex.config.PlanetOverridesCore.load(cfg.resolve("chex/chex-planets.json5"));
        CHEX.LOGGER.info("[CHEX] Planet overrides {} ({} entries)",
                overrides.isPresent() ? "loaded" : "not found",
                overrides.map(java.util.Map::size).orElse(0));
    }
}
