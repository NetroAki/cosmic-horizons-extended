package com.netroaki.chex.world.eden.features.flora;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class CelestialBloomBlock extends EdenPlantBlock {
  private static final int GLOWSTONE_DUST_DROP_CHANCE = 3; // 1 in 3 chance

  public CelestialBloomBlock(Properties properties) {
    super(properties, 10); // Slower growth rate
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (state.getValue(AGE) >= 2) {
      // Add particle effects for mature plants
      if (random.nextInt(10) == 0) {
        double x = (double) pos.getX() + random.nextDouble();
        double y = (double) pos.getY() + 0.5D + random.nextDouble() * 0.5D;
        double z = (double) pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    super.playerWillDestroy(level, pos, state, player);

    if (!level.isClientSide && state.getValue(AGE) == 3) {
      // Play a twinkling sound when fully grown plant is broken
      level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 1.0F);

      // Drop Glowstone Dust when fully grown
      if (level.random.nextInt(GLOWSTONE_DUST_DROP_CHANCE) == 0) {
        popResource(level, pos, new ItemStack(Items.GLOWSTONE_DUST));
      }
    }
  }

  @Override
  public ItemStack getCloneItemStack(
      BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
    return new ItemStack(Items.GLOWSTONE_DUST);
  }
}
