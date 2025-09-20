package com.netroaki.chex.config;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.TravelConfigCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class TravelConfigLoader {
    private static final String FILE_NAME = "chex/chex-travel.json5";

    private TravelConfigLoader() {
    }

    public static Optional<Map<Integer, Set<ResourceLocation>>> load() {
        try {
            Path cfgDir = FMLPaths.CONFIGDIR.get();
            Path path = cfgDir.resolve(FILE_NAME);
            if (!Files.exists(path)) {
                writeSample(path);
                return Optional.empty();
            }
            var core = TravelConfigCore.load(path);
            if (core.isEmpty())
                return Optional.empty();
            Map<Integer, Set<ResourceLocation>> out = new HashMap<>();
            core.get().forEach((tier, ids) -> {
                Set<ResourceLocation> rls = new HashSet<>();
                for (String id : ids) {
                    try {
                        rls.add(ResourceLocation.parse(id));
                    } catch (Exception ignored) {
                    }
                }
                if (!rls.isEmpty())
                    out.put(tier, rls);
            });
            return Optional.of(out);
        } catch (Exception ex) {
            CHEX.LOGGER.warn("[CHEX] Failed to load chex-travel.json5: {}", ex.toString());
            return Optional.empty();
        }
    }

    private static void writeSample(Path path) {
        try {
            Files.createDirectories(path.getParent());
            String sample = "{\n" +
                    "  // Travel graph override; list planets per tier\n" +
                    "  \"tiers\": [\n" +
                    "    { \"tier\": 1, \"planets\": [\"cosmos:earth_moon\", \"cosmos:mercury\"] },\n" +
                    "    { \"tier\": 2, \"planets\": [\"cosmos:venus\"] }\n" +
                    "  ]\n" +
                    "}\n";
            Files.writeString(path, sample);
            CHEX.LOGGER.info("[CHEX] Wrote sample travel config to {}", path);
        } catch (Exception ex) {
            CHEX.LOGGER.warn("[CHEX] Failed to write sample travel config: {}", ex.toString());
        }
    }
}
