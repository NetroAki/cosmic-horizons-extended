package com.netroaki.chex.world.hazard;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public abstract class HazardZone {
  protected final BlockPos center;
  protected final float radius;
  protected final float damagePerSecond;
  protected final int checkInterval;
  protected int tickCounter = 0;

  public HazardZone(BlockPos center, float radius, float damagePerSecond, int checkInterval) {
    this.center = center.immutable();
    this.radius = radius;
    this.damagePerSecond = damagePerSecond;
    this.checkInterval = Math.max(1, checkInterval);
  }

  public void tick(Level level) {
    if (++tickCounter >= checkInterval) {
      tickCounter = 0;

      // Get all entities in the hazard zone
      AABB boundingBox =
          new AABB(
              center.getX() - radius,
              center.getY() - radius,
              center.getZ() - radius,
              center.getX() + radius,
              center.getY() + radius,
              center.getZ() + radius);

      for (Entity entity : level.getEntities(null, boundingBox)) {
        if (isAffected(entity) && isInHazardZone(entity.position())) {
          affectEntity(entity, damagePerSecond * checkInterval / 20.0f);
        }
      }

      // Additional per-tick updates
      updateHazard(level);
    }
  }

  protected abstract boolean isAffected(Entity entity);

  protected abstract void affectEntity(Entity entity, float damageAmount);

  protected void updateHazard(Level level) {
    // Override in subclasses for custom behavior
  }

  public boolean isInHazardZone(net.minecraft.world.phys.Vec3 position) {
    double dx = position.x - (center.getX() + 0.5);
    double dy = position.y - (center.getY() + 0.5);
    double dz = position.z - (center.getZ() + 0.5);
    return dx * dx + dy * dy * 0.5 + dz * dz <= radius * radius;
  }

  public BlockPos getCenter() {
    return center;
  }

  public float getRadius() {
    return radius;
  }

  public boolean isActive() {
    return true; // Override in subclasses for conditional activation
  }
}
