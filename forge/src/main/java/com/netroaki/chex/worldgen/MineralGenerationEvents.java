package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import net.minecraftforge.event.level.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Injects dynamically generated mineral placed features into biome generation.
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID)
public final class MineralGenerationEvents {

    private MineralGenerationEvents() {
    }

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (!MineralGenerationRegistry.isInitialized()) {
            return;
        }
        ResourceLocation biomeId = event.getName();
        if (biomeId == null) {
            return;
        }
        List<Holder<PlacedFeature>> features =
                MineralGenerationRegistry.gatherFeatures(biomeId, event.getTags());
        if (features.isEmpty()) {
            return;
        }
        features.forEach(feature -> event.getGeneration()
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature));
    }
}


