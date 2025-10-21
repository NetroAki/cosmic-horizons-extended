package com.netroaki.chex.worldgen.features;

import com.mojang.serialization.Codec;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PandoraFungalTowerFeature extends Feature<NoneFeatureConfiguration> {

  public PandoraFungalTowerFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    WorldGenLevel world = context.level();
    BlockPos pos = context.origin();
    RandomSource random = context.random();

    if (!world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
      return false;
    }

    int height = 5 + random.nextInt(8); // 5-12 blocks tall
    int capRadius = 2 + random.nextInt(3); // 2-4 block radius

    // Generate stem
    for (int y = 0; y < height; y++) {
      world.setBlock(pos.above(y), CHEXBlocks.PANDORITE_MOSSY.get().defaultBlockState(), 2);
    }

    // Generate cap
    BlockPos capCenter = pos.above(height);
    for (int x = -capRadius; x <= capRadius; x++) {
      for (int z = -capRadius; z <= capRadius; z++) {
        double distance = Math.sqrt(x * x + z * z);
        if (distance <= capRadius + 0.5) {
          BlockPos capPos = capCenter.offset(x, 0, z);
          if (world.isEmptyBlock(capPos)) {
            world.setBlock(capPos, CHEXBlocks.BIOLUME_MOSS.get().defaultBlockState(), 2);
          }
        }
      }
    }

    return true;
  }
}
