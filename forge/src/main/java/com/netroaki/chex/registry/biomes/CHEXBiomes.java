package com.netroaki.chex.registry.biomes;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.config.CHEXConfig;
import com.netroaki.chex.worldgen.region.CHEXRegion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import terrablender.api.Regions;

/**
 * Biome registry and bootstrap helpers for CHEX. The palette is still a work
 * in progress, so each biome currently exposes distinctive colours while we
 * iterate on dedicated features and mob rules. Generation/mob settings remain
 * empty placeholders but the structure gives us a single spot to elaborate on
 * once the world-gen JSONs land.
 */
public final class CHEXBiomes {

    private CHEXBiomes() {
    }

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, CHEX.MOD_ID);

    public static final ResourceKey<Biome> NEUTRON_STAR_CRUSHED_BASALT_PLAINS = key("neutron_star_crushed_basalt_plains");
    public static final ResourceKey<Biome> SHATTERED_DYSON_HABITAT_DOME = key("shattered_dyson_habitat_dome");
    public static final ResourceKey<Biome> CRYSTALIS_PRISM_RIDGES = key("crystalis_prism_ridges");
    public static final ResourceKey<Biome> INFERNO_PRIME_VOLCANIC_PLAINS = key("inferno_prime_volcanic_plains");
    public static final ResourceKey<Biome> AQUA_MUNDUS_ABYSSAL_TRENCH = key("aqua_mundus_abyssal_trench");

    public static final ResourceKey<Biome> ARRAKIS_GREAT_DUNES = key("arrakis_great_dunes");
    public static final ResourceKey<Biome> ARRAKIS_SPICE_MINES = key("arrakis_spice_mines");
    public static final ResourceKey<Biome> ARRAKIS_POLAR_ICE_CAPS = key("arrakis_polar_ice_caps");
    public static final ResourceKey<Biome> ARRAKIS_SIETCH_STRONGHOLDS = key("arrakis_sietch_strongholds");
    public static final ResourceKey<Biome> ARRAKIS_STORMLANDS = key("arrakis_stormlands");

    public static final ResourceKey<Biome> PANDORA_BIOLUMINESCENT_FOREST = key("pandora_bioluminescent_forest");
    public static final ResourceKey<Biome> PANDORA_FLOATING_MOUNTAINS = key("pandora_floating_mountains");
    public static final ResourceKey<Biome> PANDORA_OCEAN_DEPTHS = key("pandora_ocean_depths");
    public static final ResourceKey<Biome> PANDORA_VOLCANIC_WASTELAND = key("pandora_volcanic_wasteland");
    public static final ResourceKey<Biome> PANDORA_SKY_ISLANDS = key("pandora_sky_islands");

    public static final ResourceKey<Biome> TORUS_RING_PLAINS = key("torus_ring_plains");
    public static final ResourceKey<Biome> HOLLOW_WORLD_CAVERN = key("hollow_world_cavern");
    public static final ResourceKey<Biome> EXOTICA_STRANGE_FIELDS = key("exotica_strange_fields");
    public static final ResourceKey<Biome> KEPLER_TEMPERATE_VALLEY = key("kepler_temperate_valley");

    private static ResourceKey<Biome> key(String path) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, path));
    }

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(NEUTRON_STAR_CRUSHED_BASALT_PLAINS, createNeutronStarBiome());
        context.register(SHATTERED_DYSON_HABITAT_DOME, createShatteredDysonBiome());
        context.register(CRYSTALIS_PRISM_RIDGES, createCrystalisBiome());
        context.register(INFERNO_PRIME_VOLCANIC_PLAINS, createInfernoPrimeBiome());
        context.register(AQUA_MUNDUS_ABYSSAL_TRENCH, createAquaMundusBiome());
        context.register(ARRAKIS_GREAT_DUNES, createArrakisGreatDunes());
        context.register(ARRAKIS_SPICE_MINES, createArrakisSpiceMines());
        context.register(ARRAKIS_POLAR_ICE_CAPS, createArrakisPolarIceCaps());
        context.register(ARRAKIS_SIETCH_STRONGHOLDS, createArrakisSietchStrongholds());
        context.register(ARRAKIS_STORMLANDS, createArrakisStormlands());
        context.register(PANDORA_BIOLUMINESCENT_FOREST, createPandoraBioluminescentForest());
        context.register(PANDORA_FLOATING_MOUNTAINS, createPandoraFloatingMountains());
        context.register(PANDORA_OCEAN_DEPTHS, createPandoraOceanDepths());
        context.register(PANDORA_VOLCANIC_WASTELAND, createPandoraVolcanicWasteland());
        context.register(PANDORA_SKY_ISLANDS, createPandoraSkyIslands());
        context.register(TORUS_RING_PLAINS, createTorusBiome());
        context.register(HOLLOW_WORLD_CAVERN, createHollowWorldBiome());
        context.register(EXOTICA_STRANGE_FIELDS, createExoticaBiome());
        context.register(KEPLER_TEMPERATE_VALLEY, createKeplerBiome());
    }

    public static void registerRegions() {
        if (CHEXConfig.enableTerraBlenderOverlay()) {
            Regions.register(new CHEXRegion(ResourceLocation.fromNamespaceAndPath(CHEX.MOD_ID, "chex_region"), 10));
            CHEX.LOGGER.info("CHEX TerraBlender overlay enabled.");
        } else {
            CHEX.LOGGER.info("CHEX TerraBlender overlay disabled by config.");
        }
    }

    private static Biome createNeutronStarBiome() {
        return buildBiome(0.0f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xFF0000)
                        .fogColor(0xFF0000)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createShatteredDysonBiome() {
        return buildBiome(0.3f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x1A1A1A)
                        .fogColor(0x0D0D0D)
                        .waterColor(0x394B70)
                        .waterFogColor(0x050B1E));
    }

    private static Biome createCrystalisBiome() {
        return buildBiome(0.4f, 0.4f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xAAF1FF)
                        .fogColor(0x88C8D4)
                        .waterColor(0x4681FF)
                        .waterFogColor(0x24408C)
                        .grassColorOverride(0xB6FFE0)
                        .foliageColorOverride(0x7FF7D4));
    }

    private static Biome createInfernoPrimeBiome() {
        return buildBiome(2.0f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x7A2E00)
                        .fogColor(0x402010)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createAquaMundusBiome() {
        return buildBiome(0.2f, 1.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x3A9FE8)
                        .fogColor(0x225F8A)
                        .waterColor(0x1E6E9D)
                        .waterFogColor(0x0C324D));
    }

    private static Biome createArrakisGreatDunes() {
        return buildBiome(2.0f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xFFDF80)
                        .fogColor(0xE6C550)
                        .waterColor(0x35A7FF)
                        .waterFogColor(0x0A3D66));
    }

    private static Biome createArrakisSpiceMines() {
        return buildBiome(1.4f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xD7A244)
                        .fogColor(0xA1651F)
                        .waterColor(0x2F4E72)
                        .waterFogColor(0x0E1A2B));
    }

    private static Biome createArrakisPolarIceCaps() {
        return buildBiome(-0.5f, 0.3f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xD4ECFF)
                        .fogColor(0xA9CCE8)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createArrakisSietchStrongholds() {
        return buildBiome(1.2f, 0.05f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xC1863D)
                        .fogColor(0x845C28)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createArrakisStormlands() {
        return buildBiome(1.5f, 0.4f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x7A5F73)
                        .fogColor(0x3A2636)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createPandoraBioluminescentForest() {
        return buildBiome(0.95f, 0.9f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x4E1895)
                        .fogColor(0x1B0D33)
                        .waterColor(0x3A76F0)
                        .waterFogColor(0x080A1F)
                        .grassColorOverride(0x5BE5A5)
                        .foliageColorOverride(0x48D8C0));
    }

    private static Biome createPandoraFloatingMountains() {
        return buildBiome(0.7f, 0.4f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xAEE4FF)
                        .fogColor(0x89BFE1)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .grassColorOverride(0x7ED1C2));
    }

    private static Biome createPandoraOceanDepths() {
        return buildBiome(0.3f, 1.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x224E78)
                        .fogColor(0x0D2440)
                        .waterColor(0x225A8F)
                        .waterFogColor(0x061A2C));
    }

    private static Biome createPandoraVolcanicWasteland() {
        return buildBiome(1.3f, 0.1f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x812B1D)
                        .fogColor(0x2C100B)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome createPandoraSkyIslands() {
        return buildBiome(0.7f, 0.2f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0xCFE9FF)
                        .fogColor(0xE6F4FF)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533)
                        .grassColorOverride(0xA9F0CE));
    }

    private static Biome createTorusBiome() {
        return buildBiome(0.9f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x88CCFF)
                        .fogColor(0x6699CC)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x041F33));
    }

    private static Biome createHollowWorldBiome() {
        return buildBiome(0.4f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x222222)
                        .fogColor(0x111111)
                        .waterColor(0x2F3F64)
                        .waterFogColor(0x0A0D14));
    }

    private static Biome createExoticaBiome() {
        return buildBiome(1.2f, 0.0f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x00FF99)
                        .fogColor(0x004433)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x041F33));
    }

    private static Biome createKeplerBiome() {
        return buildBiome(0.7f, 0.7f,
                new BiomeSpecialEffects.Builder()
                        .skyColor(0x77AA77)
                        .fogColor(0xAACCAA)
                        .waterColor(0x3F76E4)
                        .waterFogColor(0x050533));
    }

    private static Biome buildBiome(float temperature, float downfall, BiomeSpecialEffects.Builder effectsBuilder) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(downfall > 0.0f)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(effectsBuilder.build())
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
