package com.netroaki.chex.terrablender;

import com.mojang.datafixers.util.Pair;
import com.netroaki.chex.CHEX;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;

public final class CHEXTerraBlender {
  private static final ResourceLocation PLACEHOLDER =
      new ResourceLocation(CHEX.MOD_ID, "placeholder_region");

  private CHEXTerraBlender() {}

  public static void register() {
    Regions.register(
        new Region(PLACEHOLDER, RegionType.OVERWORLD, 0) {
          @Override
          public void addBiomes(
              Registry<Biome> registry,
              Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer) {
            // Placeholder region - actual biome layering will be provided during worldgen task.
          }
        });
  }
}
