package com.netroaki.chex.worldgen.region;

import com.mojang.datafixers.util.Pair;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.biomes.CHEXBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import net.minecraft.world.level.levelgen.SurfaceRules;
import com.netroaki.chex.registry.blocks.CHEXBlocks;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class CHEXRegion extends Region {

        public CHEXRegion(ResourceLocation name, int weight) {
                super(name, RegionType.OVERWORLD, weight);
        }

        @Override
        public void addBiomes(Registry<Biome> registry,
                        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
                // Preserve original CH biomes by default: only add mappings/surface rules when
                // enabled
                if (!com.netroaki.chex.config.CHEXConfig.enableTerraBlenderOverlay()) {
                        return; // no-op overlay; registration exists for future enablement
                }
                VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

                // Neutron Star - Extreme cold, no humidity
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.FROZEN, Temperature.COOL))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build()
                                .forEach(point -> builder.add(point, CHEXBiomes.NEUTRON_STAR_CRUSHED_BASALT_PLAINS));

                // Shattered Dyson Swarm - Moderate temperature, no humidity
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.COOL, Temperature.NEUTRAL))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.HIGH_SLICE_NORMAL_ASCENDING,
                                                Weirdness.HIGH_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.SHATTERED_DYSON_HABITAT_DOME));

                // Crystalis - Cold, moderate humidity
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.FROZEN, Temperature.COOL))
                                .humidity(Humidity.span(Humidity.DRY, Humidity.NEUTRAL))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.CRYSTALIS_PRISM_RIDGES));

                // Inferno Prime - Very hot, no humidity
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.HOT, Temperature.WARM))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.INFERNO_PRIME_VOLCANIC_PLAINS));

                // Aqua Mundus - Moderate temperature, high humidity
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.COOL, Temperature.NEUTRAL))
                                .humidity(Humidity.span(Humidity.HUMID, Humidity.WET))
                                .continentalness(Continentalness.COAST, Continentalness.NEAR_INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.AQUA_MUNDUS_ABYSSAL_TRENCH));

                // Arrakis - distribute sub-biomes
                new ParameterPointListBuilder()
                                .temperature(Temperature.HOT)
                                .humidity(Humidity.ARID)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_3)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_GREAT_DUNES));

                new ParameterPointListBuilder()
                                .temperature(Temperature.WARM)
                                .humidity(Humidity.DRY)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_2)
                                .depth(Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_SPICE_MINES));

                new ParameterPointListBuilder()
                                .temperature(Temperature.COOL)
                                .humidity(Humidity.DRY)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_1)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_POLAR_ICE_CAPS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.WARM)
                                .humidity(Humidity.DRY)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_1)
                                .depth(Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_SIETCH_STRONGHOLDS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.HOT)
                                .humidity(Humidity.ARID)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_4)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_VARIANT_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_STORMLANDS));

                // Pandora - map multiple climate bands to its sub-biomes
                new ParameterPointListBuilder()
                                .temperature(Temperature.NEUTRAL)
                                .humidity(Humidity.HUMID)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_1)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_BIOLUMINESCENT_FOREST));

                new ParameterPointListBuilder()
                                .temperature(Temperature.WARM)
                                .humidity(Humidity.NEUTRAL)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_2)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.HIGH_SLICE_NORMAL_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_FLOATING_MOUNTAINS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.COOL)
                                .humidity(Humidity.WET)
                                .continentalness(Continentalness.COAST)
                                .erosion(Erosion.EROSION_0)
                                .depth(Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_OCEAN_DEPTHS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.HOT)
                                .humidity(Humidity.ARID)
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_3)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_VARIANT_ASCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_VOLCANIC_WASTELAND));

                new ParameterPointListBuilder()
                                .temperature(Temperature.NEUTRAL)
                                .humidity(Humidity.NEUTRAL)
                                .continentalness(Continentalness.NEAR_INLAND)
                                .erosion(Erosion.EROSION_1)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.HIGH_SLICE_VARIANT_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_SKY_ISLANDS));

                // Torus ring plains - warm
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.NEUTRAL, Humidity.HUMID))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_2, Erosion.EROSION_3)
                                .depth(Depth.SURFACE, Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.TORUS_RING_PLAINS));

                // Hollow world cavern - cold/dry
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.COOL, Temperature.FROZEN))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_0, Erosion.EROSION_1)
                                .depth(Depth.FLOOR)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.HOLLOW_WORLD_CAVERN));

                // Exotica strange fields - hot/dry, weird
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.HOT, Temperature.WARM))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_4, Erosion.EROSION_5)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.HIGH_SLICE_VARIANT_ASCENDING,
                                                Weirdness.HIGH_SLICE_VARIANT_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.EXOTICA_STRANGE_FIELDS));

                // Kepler temperate valley - temperate/wet
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.WARM))
                                .humidity(Humidity.span(Humidity.NEUTRAL, Humidity.WET))
                                .continentalness(Continentalness.INLAND)
                                .erosion(Erosion.EROSION_1, Erosion.EROSION_2)
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING)
                                .build().forEach(point -> builder.add(point, CHEXBiomes.KEPLER_TEMPERATE_VALLEY));

                // Add our points to the mapper
                builder.build().forEach(mapper::accept);

                // Register simple surface rules to give each biome a unique top block
                SurfaceRules.RuleSource arrakisTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_GREAT_DUNES),
                                                SurfaceRules.state(CHEXBlocks.ARRAKIS_SAND.get().defaultBlockState())));

                SurfaceRules.RuleSource infernoTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.INFERNO_PRIME_VOLCANIC_PLAINS),
                                                SurfaceRules.state(
                                                                CHEXBlocks.INFERNO_STONE.get().defaultBlockState())));

                SurfaceRules.RuleSource crystalisTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.CRYSTALIS_PRISM_RIDGES),
                                                SurfaceRules.state(CHEXBlocks.CRYSTALIS_CRYSTAL.get()
                                                                .defaultBlockState())));

                SurfaceRules.RuleSource aquaTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.AQUA_MUNDUS_ABYSSAL_TRENCH),
                                                SurfaceRules.state(CHEXBlocks.AQUA_MUNDUS_STONE.get()
                                                                .defaultBlockState())));

                SurfaceRules.RuleSource pandoraForestTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_BIOLUMINESCENT_FOREST),
                                                SurfaceRules.state(
                                                                CHEXBlocks.PANDORA_GRASS.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraFloatingTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_FLOATING_MOUNTAINS),
                                                SurfaceRules.state(
                                                                CHEXBlocks.PANDORA_BLOOM.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraOceanTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_OCEAN_DEPTHS),
                                                SurfaceRules.state(CHEXBlocks.AQUA_MUNDUS_STONE.get()
                                                                .defaultBlockState())));
                SurfaceRules.RuleSource pandoraVolcanicTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_VOLCANIC_WASTELAND),
                                                SurfaceRules.state(
                                                                CHEXBlocks.INFERNO_STONE.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraSkyTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_SKY_ISLANDS),
                                                SurfaceRules.state(
                                                                CHEXBlocks.PANDORA_GRASS.get().defaultBlockState())));

                SurfaceRules.RuleSource neutronTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.NEUTRON_STAR_CRUSHED_BASALT_PLAINS),
                                                SurfaceRules.state(CHEXBlocks.NEUTRON_STAR_BASALT.get()
                                                                .defaultBlockState())));

                SurfaceRules.RuleSource torusTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.TORUS_RING_PLAINS),
                                                SurfaceRules.state(CHEXBlocks.ARRAKIS_ROCK.get().defaultBlockState())));

                SurfaceRules.RuleSource hollowTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.HOLLOW_WORLD_CAVERN),
                                                SurfaceRules.state(CHEXBlocks.NEUTRON_STAR_PLATE.get()
                                                                .defaultBlockState())));

                SurfaceRules.RuleSource exoticaTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.EXOTICA_STRANGE_FIELDS),
                                                SurfaceRules.state(CHEXBlocks.INFERNO_ASH.get().defaultBlockState())));

                SurfaceRules.RuleSource keplerTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.KEPLER_TEMPERATE_VALLEY),
                                                SurfaceRules.state(
                                                                CHEXBlocks.PANDORA_BLOOM.get().defaultBlockState())));

                SurfaceRuleManager.addSurfaceRules(RuleCategory.OVERWORLD, CHEX.MOD_ID,
                                SurfaceRules.sequence(arrakisTop, infernoTop, crystalisTop, aquaTop,
                                                pandoraForestTop, pandoraFloatingTop, pandoraOceanTop,
                                                pandoraVolcanicTop,
                                                pandoraSkyTop,
                                                neutronTop, torusTop, hollowTop, exoticaTop, keplerTop));
        }
}
