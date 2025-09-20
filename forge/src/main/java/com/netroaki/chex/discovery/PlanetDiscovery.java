package com.netroaki.chex.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PlanetDiscovery {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private PlanetDiscovery() {
    }

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

            for (ResourceKey<Level> key : dimRegistry.registryKeySet()) {
                ResourceLocation id = key.location();
                String ns = id.getNamespace();
                // Focus on CH namespaces for discovery; skip internal/utility dims
                if (!(ns.equals("cosmos") || ns.equals("cosmichorizons")))
                    continue;

                JsonObject o = new JsonObject();
                o.addProperty("id", id.toString());
                o.addProperty("name", id.getPath());
                o.addProperty("requiredNoduleTier", 1);
                o.addProperty("requiredSuitTag", "chex:suits/suit1");
                o.addProperty("source", ns);
                arr.add(o);
            }

            root.addProperty("count", arr.size());
            root.add("planets", arr);

            Path cfgDir = FMLPaths.CONFIGDIR.get();
            Path outDir = cfgDir.resolve("chex");
            Files.createDirectories(outDir);
            Path out = outDir.resolve("_discovered_planets.json");
            Files.writeString(out, GSON.toJson(root));
            CHEX.LOGGER.info("[CHEX] Wrote discovered planets to {} ({} entries)", out, arr.size());
        } catch (Exception ex) {
            CHEX.LOGGER.warn("[CHEX] Failed to write discovered planets: {}", ex.toString());
        }
    }
}
