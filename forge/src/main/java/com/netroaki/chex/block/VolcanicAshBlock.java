package com.netroaki.chex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

public class VolcanicAshBlock extends Block {
  private static final int TICK_DELAY = 20;

  public VolcanicAshBlock() {
    super(Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5f, 0.5f).friction(0.98f));
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    // Emit occasional ash particles
    if (random.nextInt(20) == 0) {
      double x = pos.getX() + random.nextDouble();
      double y = pos.getY() + 1.1D;
      double z = pos.getZ() + random.nextDouble();

      level.addParticle(ParticleTypes.ASH, x, y, z, 0.0D, 0.0D, 0.0D);
    }
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (!entity.isSteppingCarefully() && entity instanceof LivingEntity) {
      // Slow down entities walking on ash
      entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));

      // Small chance to emit ash particles when stepped on
      if (level.random.nextInt(10) == 0) {
        level.addParticle(
            ParticleTypes.ASH, entity.getX(), pos.getY() + 1.0D, entity.getZ(), 0.0D, 0.1D, 0.0D);
      }
    }
    super.stepOn(level, pos, state, entity);
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    // Check if there's a solid block below, if not, fall
    if (!canSurvive(state, level, pos)) {
      level.destroyBlock(pos, true);
    }
  }

  @Override
  public BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      LevelAccessor level,
      BlockPos currentPos,
      BlockPos neighborPos) {
    level.scheduleTick(currentPos, this, TICK_DELAY);
    return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
    level.scheduleTick(pos, this, TICK_DELAY);
  }

  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockPos belowPos = pos.below();
    return canSupportRigidBlock(level, belowPos) || canSupportCenter(level, belowPos, Direction.UP);
  }
}
