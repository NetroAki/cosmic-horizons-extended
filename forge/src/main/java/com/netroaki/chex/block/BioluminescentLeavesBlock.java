package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BioluminescentLeavesBlock extends LeavesBlock {
  private static final int LIGHT_LEVEL = 12;

  public BioluminescentLeavesBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition
            .any()
            .setValue(DISTANCE, 7)
            .setValue(PERSISTENT, false)
            .setValue(WATERLOGGED, false));
  }

  @Override
  public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
    return LIGHT_LEVEL;
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (level.isClientSide && random.nextInt(10) == 0) {
      // Add glowing particle effects
      double x = (double) pos.getX() + random.nextDouble();
      double y = (double) pos.getY() + random.nextDouble();
      double z = (double) pos.getZ() + random.nextDouble();

      // Spawn glowing particles
      level.addParticle(
          net.minecraft.core.particles.ParticleTypes.END_ROD, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  @Override
  public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
    return 1.0F;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Custom decay behavior for floating leaves
    if (!state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7) {
      dropResources(state, level, pos);
      level.removeBlock(pos, false);
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (this.decaying(state)) {
      dropResources(state, level, pos);
      level.removeBlock(pos, false);
    }
  }

  protected boolean decaying(BlockState state) {
    return !state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 7;
  }
}
