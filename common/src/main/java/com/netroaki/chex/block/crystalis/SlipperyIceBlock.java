package com.netroaki.chex.block.crystalis;

// import com.netroaki.chex.api.suit.ISuitTier; // TODO: Implement suit API
// import com.netroaki.chex.api.suit.SuitTier; // TODO: Implement suit API
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlipperyIceBlock extends IceBlock {
  private static final float SLIP_FACTOR = 0.989f; // Very slippery

  public SlipperyIceBlock(Properties properties) {
    super(properties.friction(SLIP_FACTOR));
  }

  @Override
  public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
    if (entity instanceof LivingEntity living) {
      // Apply extra slipping effect
      Vec3 motion = entity.getDeltaMovement();
      if (Math.abs(motion.x) > 0.01 || Math.abs(motion.z) > 0.01) {
        // TODO: Suit tier mitigation reduces sliding when suit API is implemented
        double mult = 1.1;
        // if (living instanceof net.minecraft.world.entity.player.Player player) {
        // ISuitTier suitTier = SuitTier.getSuitTier(player);
        // int tier = suitTier != null ? suitTier.getTier() : 0;
        // if (tier >= 3) {
        // mult = 1.02; // advanced suits stabilize footing
        // } else if (tier >= 2) {
        // mult = 1.06;
        // }
        // }
        // Amplify horizontal movement for sliding effect (mitigated by suit)
        entity.setDeltaMovement(motion.x * mult, motion.y, motion.z * mult);

        // Add sliding particles
        if (!level.isClientSide && level.random.nextInt(5) == 0) {
          level.addParticle(
              ParticleTypes.SNOWFLAKE,
              entity.getX() + (level.random.nextDouble() - 0.5) * 0.5,
              entity.getY() + 0.1,
              entity.getZ() + (level.random.nextDouble() - 0.5) * 0.5,
              0,
              0,
              0);
        }
      }
    }
    super.stepOn(level, pos, state, entity);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    super.animateTick(state, level, pos, random);

    // Add occasional frost particles
    if (random.nextInt(20) == 0) {
      double d0 = (double) pos.getX() + random.nextDouble();
      double d1 = (double) pos.getY() + 1.0D;
      double d2 = (double) pos.getZ() + random.nextDouble();
      level.addParticle(ParticleTypes.SNOWFLAKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
  }
}
