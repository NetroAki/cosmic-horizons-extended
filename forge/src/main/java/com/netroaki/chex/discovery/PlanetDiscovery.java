package com.netroaki.chex.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.PlanetRegistry;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLPaths;

public final class PlanetDiscovery {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Set<ResourceLocation> EXPECTED_PLANETS =
      Set.of(
          ResourceLocation.fromNamespaceAndPath("cosmos", "earth_moon"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "mercury_wasteland"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "venuslands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "marslands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "jupiterlands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "europa_lands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "saturn_lands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "uranus_lands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "neptune_lands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "plutowastelands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "alpha_system"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "b_1400_centauri"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "j_1407blands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "j_1900"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "glacio_lands"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "gaia_bh_1"),
          ResourceLocation.fromNamespaceAndPath("cosmos", "solar_system"));

  private PlanetDiscovery() {}

  public static void discoverAndWrite(MinecraftServer server) {
    try {
      var dimRegistryOpt = server.registryAccess().registry(Registries.DIMENSION);
      if (dimRegistryOpt.isEmpty()) {
        CHEX.LOGGER.warn("[CHEX] Dimension registry not available for discovery");
        return;
      }
      var dimRegistry = dimRegistryOpt.get();

      JsonObject root = new JsonObject();
      JsonArray arr = new JsonArray();
      Set<ResourceLocation> discovered = new HashSet<>();

      for (ResourceKey<Level> key : dimRegistry.registryKeySet()) {
        ResourceLocation id = key.location();
        String ns = id.getNamespace();
        // Focus on CH namespaces for discovery; skip internal/utility dims
        if (!(ns.equals("cosmos") || ns.equals("cosmichorizons"))) continue;

        JsonObject o = new JsonObject();
        o.addProperty("id", id.toString());
        o.addProperty("name", id.getPath());
        o.addProperty("requiredNoduleTier", 1);
        o.addProperty("requiredSuitTag", "chex:suits/suit1");
        o.addProperty("source", ns);
        arr.add(o);
        discovered.add(id);
      }

      Set<ResourceLocation> missing = new HashSet<>(EXPECTED_PLANETS);
      missing.removeAll(discovered);
      if (!missing.isEmpty()) {
        CHEX.LOGGER.warn(
            "[CHEX] Missing expected Cosmic Horizons dimensions ({}):", missing.size());
        missing.forEach(id -> CHEX.LOGGER.warn(" - {}", id));
      }

      root.addProperty("count", arr.size());
      root.add("planets", arr);

      Path cfgDir = FMLPaths.CONFIGDIR.get();
      Path outDir = cfgDir.resolve("chex");
      Files.createDirectories(outDir);
      Path out = outDir.resolve("_discovered_planets.json");
      Files.writeString(out, GSON.toJson(root));
      CHEX.LOGGER.info("[CHEX] Wrote discovered planets to {} ({} entries)", out, arr.size());
      PlanetRegistry.reloadDiscoveredPlanets();
    } catch (Exception ex) {
      CHEX.LOGGER.warn("[CHEX] Failed to write discovered planets: {}", ex.toString());
    }
  }
}
