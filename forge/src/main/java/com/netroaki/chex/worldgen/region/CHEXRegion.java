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

                // TODO: fold in the remaining CHEX worlds once their biome sets are production-ready.

                // Arrakis - distribute sub-biomes across hot/arid slices without touching vanilla temperate bands
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.span(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_0, Erosion.EROSION_2))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_GREAT_DUNES));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.NEUTRAL))
                                .continentalness(Continentalness.span(Continentalness.INLAND, Continentalness.FAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_1, Erosion.EROSION_3))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.LOW_SLICE_NORMAL_ASCENDING, Weirdness.LOW_SLICE_NORMAL_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_SPICE_MINES));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.COOL))
                                .humidity(Humidity.span(Humidity.DRY, Humidity.NEUTRAL))
                                .continentalness(Continentalness.span(Continentalness.COAST, Continentalness.NEAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_0, Erosion.EROSION_2))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.HIGH_SLICE_NORMAL_ASCENDING, Weirdness.HIGH_SLICE_NORMAL_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_POLAR_ICE_CAPS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.span(Continentalness.INLAND, Continentalness.FAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_3, Erosion.EROSION_4))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_SIETCH_STRONGHOLDS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.span(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_4, Erosion.EROSION_5))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.MID_SLICE_VARIANT_ASCENDING, Weirdness.MID_SLICE_VARIANT_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.ARRAKIS_STORMLANDS));

                // Pandora - tie each sub-biome to distinct humidity/height slices for vertical variety
                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.WARM))
                                .humidity(Humidity.span(Humidity.NEUTRAL, Humidity.WET))
                                .continentalness(Continentalness.span(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_1, Erosion.EROSION_3))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_BIOLUMINESCENT_FOREST));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.WARM))
                                .humidity(Humidity.span(Humidity.NEUTRAL, Humidity.WET))
                                .continentalness(Continentalness.span(Continentalness.COAST, Continentalness.NEAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_2, Erosion.EROSION_3))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.HIGH_SLICE_NORMAL_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_FLOATING_MOUNTAINS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.COOL, Temperature.NEUTRAL))
                                .humidity(Humidity.span(Humidity.WET, Humidity.HUMID))
                                .continentalness(Continentalness.span(Continentalness.COAST, Continentalness.NEAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_0, Erosion.EROSION_1))
                                .depth(Depth.FULL_RANGE)
                                .weirdness(Weirdness.span(Weirdness.DEEP_OCEAN, Weirdness.MID_SLICE_VARIANT_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_OCEAN_DEPTHS));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.WARM, Temperature.HOT))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.span(Continentalness.NEAR_INLAND, Continentalness.MID_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_1, Erosion.EROSION_2))
                                .depth(Depth.SURFACE)
                                .weirdness(Weirdness.span(Weirdness.LOW_SLICE_VARIANT_ASCENDING, Weirdness.LOW_SLICE_VARIANT_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_VOLCANIC_WASTELAND));

                new ParameterPointListBuilder()
                                .temperature(Temperature.span(Temperature.COOL, Temperature.NEUTRAL))
                                .humidity(Humidity.span(Humidity.ARID, Humidity.DRY))
                                .continentalness(Continentalness.span(Continentalness.HALF_INLAND, Continentalness.FAR_INLAND))
                                .erosion(Erosion.span(Erosion.EROSION_0, Erosion.EROSION_1))
                                .depth(Depth.HIGH_OFFSET, Depth.PEAK)
                                .weirdness(Weirdness.span(Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING))
                                .build().forEach(point -> builder.add(point, CHEXBiomes.PANDORA_SKY_ISLANDS));

                // Add our points to the mapper
                builder.build().forEach(mapper::accept);

                // Register simple surface rules to give each active biome a unique top block
                SurfaceRules.RuleSource arrakisDunesTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_GREAT_DUNES),
                                                SurfaceRules.state(CHEXBlocks.ARRAKIS_SAND.get().defaultBlockState())));

                SurfaceRules.RuleSource arrakisSpiceTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_SPICE_MINES),
                                                SurfaceRules.state(CHEXBlocks.ARRAKITE_SANDSTONE.get().defaultBlockState())));

                SurfaceRules.RuleSource arrakisPolarTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_POLAR_ICE_CAPS),
                                                SurfaceRules.state(CHEXBlocks.ARRAKIS_SALT.get().defaultBlockState())));

                SurfaceRules.RuleSource arrakisSietchTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_SIETCH_STRONGHOLDS),
                                                SurfaceRules.state(CHEXBlocks.ARRAKIS_ROCK.get().defaultBlockState())));

                SurfaceRules.RuleSource arrakisStormTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.ARRAKIS_STORMLANDS),
                                                SurfaceRules.state(CHEXBlocks.INFERNO_ASH.get().defaultBlockState())));

                SurfaceRules.RuleSource pandoraForestTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_BIOLUMINESCENT_FOREST),
                                                SurfaceRules.state(CHEXBlocks.PANDORA_GRASS.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraFloatingTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_FLOATING_MOUNTAINS),
                                                SurfaceRules.state(CHEXBlocks.PANDORA_BLOOM.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraOceanTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_OCEAN_DEPTHS),
                                                SurfaceRules.state(CHEXBlocks.AQUA_MUNDUS_STONE.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraVolcanicTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_VOLCANIC_WASTELAND),
                                                SurfaceRules.state(CHEXBlocks.INFERNO_STONE.get().defaultBlockState())));
                SurfaceRules.RuleSource pandoraSkyTop = SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.isBiome(CHEXBiomes.PANDORA_SKY_ISLANDS),
                                                SurfaceRules.state(CHEXBlocks.PANDORA_GRASS.get().defaultBlockState())));
                SurfaceRuleManager.addSurfaceRules(RuleCategory.OVERWORLD, CHEX.MOD_ID,
                                SurfaceRules.sequence(arrakisDunesTop, arrakisSpiceTop, arrakisPolarTop,
                                                arrakisSietchTop, arrakisStormTop, pandoraForestTop,
                                                pandoraFloatingTop, pandoraOceanTop, pandoraVolcanicTop,
                                                pandoraSkyTop));
        }
}








