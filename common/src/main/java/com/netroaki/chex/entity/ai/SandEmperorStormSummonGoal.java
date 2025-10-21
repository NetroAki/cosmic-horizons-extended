package com.netroaki.chex.entity.ai;

import com.netroaki.chex.entity.arrakis.SandEmperorEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class SandEmperorStormSummonGoal extends Goal {
  private final SandEmperorEntity emperor;
  private int stormCooldown;
  private static final int MIN_STORM_COOLDOWN = 400; // 20 seconds at 20 TPS
  private static final int MAX_STORM_COOLDOWN = 800; // 40 seconds at 20 TPS
  private static final int STORM_DURATION = 200; // 10 seconds at 20 TPS
  private int stormTicks;
  private boolean isStormActive;

  public SandEmperorStormSummonGoal(SandEmperorEntity emperor) {
    this.emperor = emperor;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    this.stormCooldown =
        emperor.getRandom().nextInt(MAX_STORM_COOLDOWN - MIN_STORM_COOLDOWN) + MIN_STORM_COOLDOWN;
  }

  @Override
  public boolean canUse() {
    if (this.stormCooldown > 0) {
      this.stormCooldown--;
      return false;
    }

    // Only use in phase 2 or later
    if (this.emperor.getPhase() < 2) {
      return false;
    }

    LivingEntity target = this.emperor.getTarget();
    if (target == null || !target.isAlive()) {
      return false;
    }

    // Only summon storm if we're not already in a storm and have a clear line of sight to the sky
    return !this.isStormActive
        && this.emperor.level().canSeeSky(this.emperor.blockPosition())
        && this.emperor.getRandom().nextFloat()
            < 0.05F; // 5% chance per tick when conditions are met
  }

  @Override
  public void start() {
    this.isStormActive = true;
    this.stormTicks = 0;

    // Play storm summoning sound and particles
    Level level = this.emperor.level();
    BlockPos pos = this.emperor.blockPosition();
    level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 5.0F, 0.5F);

    // TODO: Add storm summoning particles and visual effects

    // Start a storm if this is a server level
    if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
      // Store clear weather duration to restore later
      int clearWeatherTime = serverLevel.getLevelData().getClearWeatherTime();
      serverLevel.getLevelData().setClearWeatherTime(0);
      serverLevel.setWeatherParameters(0, STORM_DURATION, true, true);

      // Store the original clear weather time in the entity's persistent data
      this.emperor.getPersistentData().putInt("OriginalClearWeatherTime", clearWeatherTime);
    }
  }

  @Override
  public boolean canContinueToUse() {
    // Continue until the storm duration is over
    return this.isStormActive && this.stormTicks < STORM_DURATION;
  }

  @Override
  public void tick() {
    this.stormTicks++;

    // Every second, do storm effects
    if (this.stormTicks % 20 == 0) {
      this.doStormEffects();
    }
  }

  private void doStormEffects() {
    Level level = this.emperor.level();
    if (level.isClientSide) return;

    // Spawn lightning around the emperor
    for (int i = 0; i < 3; i++) {
      double angle = level.random.nextDouble() * Math.PI * 2.0D;
      double distance = 5.0D + level.random.nextDouble() * 15.0D;
      double x = this.emperor.getX() + Math.cos(angle) * distance;
      double z = this.emperor.getZ() + Math.sin(angle) * distance;

      // Find the top block at this position
      BlockPos pos = new BlockPos((int) x, 0, (int) z);
      int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos).getY();

      // Only spawn lightning if it's not too close to the emperor
      if (this.emperor.distanceToSqr(x, y, z) > 16.0D) {
        ((ServerLevel) level)
            .strikeLightning(
                new net.minecraft.world.entity.LightningBolt(
                        net.minecraft.world.entity.EntityType.LIGHTNING_BOLT, level)
                    .moveTo(x, y, z));
      }
    }

    // Apply effects to nearby players
    // TODO: Add sandstorm effects to nearby players
  }

  @Override
  public void stop() {
    this.isStormActive = false;
    this.stormCooldown =
        this.emperor.getRandom().nextInt(MAX_STORM_COOLDOWN - MIN_STORM_COOLDOWN)
            + MIN_STORM_COOLDOWN;

    // Restore original weather if this is a server level
    if (!this.emperor.level().isClientSide
        && this.emperor.level() instanceof ServerLevel serverLevel) {
      int originalClearWeatherTime =
          this.emperor.getPersistentData().getInt("OriginalClearWeatherTime");
      serverLevel.getLevelData().setClearWeatherTime(originalClearWeatherTime);
    }
  }
}
