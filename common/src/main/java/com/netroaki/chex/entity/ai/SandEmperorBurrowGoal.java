package com.netroaki.chex.entity.ai;

import com.netroaki.chex.entity.arrakis.SandEmperorEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class SandEmperorBurrowGoal extends Goal {
  private final SandEmperorEntity emperor;
  private int burrowCooldown;
  private static final int MIN_BURROW_COOLDOWN = 200; // 10 seconds at 20 TPS
  private static final int MAX_BURROW_COOLDOWN = 400; // 20 seconds at 20 TPS

  public SandEmperorBurrowGoal(SandEmperorEntity emperor) {
    this.emperor = emperor;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    this.burrowCooldown =
        emperor.getRandom().nextInt(MAX_BURROW_COOLDOWN - MIN_BURROW_COOLDOWN)
            + MIN_BURROW_COOLDOWN;
  }

  @Override
  public boolean canUse() {
    if (this.burrowCooldown > 0) {
      this.burrowCooldown--;
      return false;
    }

    LivingEntity target = this.emperor.getTarget();
    if (target == null || !target.isAlive()) {
      return false;
    }

    // Only burrow if the target is within range and we're not already burrowed
    return !this.emperor.isBurrowed()
        && this.emperor.distanceToSqr(target) < 100.0D
        && // 10 blocks
        this.emperor.getRandom().nextFloat() < 0.1F; // 10% chance per tick when conditions are met
  }

  @Override
  public void start() {
    // Start burrowing
    this.emperor.setBurrowed(true);
    this.emperor.setBurrowTicks(0);

    // Play burrow sound and particles
    Level level = this.emperor.level();
    BlockPos pos = this.emperor.blockPosition();
    level.playSound(null, pos, SoundEvents.WARDEN_DIG, SoundSource.HOSTILE, 1.0F, 1.0F);

    // TODO: Add burrow particles
  }

  @Override
  public boolean canContinueToUse() {
    // Continue until the burrow is complete
    return this.emperor.isBurrowed()
        && this.emperor.getBurrowTicks() < SandEmperorEntity.MAX_BURROW_TICKS;
  }

  @Override
  public void tick() {
    // The actual burrow behavior is handled in the entity's tick method
    // This just manages the goal state
  }

  @Override
  public void stop() {
    // When done burrowing, reset cooldown
    this.burrowCooldown =
        this.emperor.getRandom().nextInt(MAX_BURROW_COOLDOWN - MIN_BURROW_COOLDOWN)
            + MIN_BURROW_COOLDOWN;

    // If we're still burrowed, emerge at a new location
    if (this.emperor.isBurrowed()) {
      this.emergeAtNewLocation();
    }
  }

  private void emergeAtNewLocation() {
    LivingEntity target = this.emperor.getTarget();
    if (target == null) {
      this.emperor.setBurrowed(false);
      return;
    }

    // Calculate a position near the target
    double angle = this.emperor.getRandom().nextDouble() * Math.PI * 2.0D;
    double distance = 3.0D + this.emperor.getRandom().nextDouble() * 4.0D;
    double x = target.getX() + Math.cos(angle) * distance;
    double z = target.getZ() + Math.sin(angle) * distance;

    // Find a valid Y position
    Level level = this.emperor.level();
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, target.getY(), z);

    // Move up if we're in a solid block
    while (pos.getY() < level.getMaxBuildHeight()
        && !level.getBlockState(pos).getMaterial().blocksMotion()) {
      pos.move(0, 1, 0);
    }

    // If we hit the top of the world, just use the target's Y
    if (pos.getY() >= level.getMaxBuildHeight()) {
      pos.setY(target.getY());
    }

    // Teleport and emerge
    this.emperor.teleportTo(x, pos.getY(), z);
    this.emperor.setBurrowed(false);

    // Play emerge sound and particles
    level.playSound(null, pos, SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE, 1.0F, 1.0F);
    // TODO: Add emerge particles and area damage
  }
}
