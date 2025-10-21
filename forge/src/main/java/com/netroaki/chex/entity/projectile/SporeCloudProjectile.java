package com.netroaki.chex.entity.projectile;

import com.netroaki.chex.entity.SporeCloudEntity;
import com.netroaki.chex.registry.entities.CHEXEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Simple projectile that spawns a {@link SporeCloudEntity} on impact. Damage and complex behaviour
 * will arrive with the combat polish pass; for now we just provide motion, particles, and spawn
 * timing so dependant code compiles and can be iterated on.
 */
public class SporeCloudProjectile extends AbstractProjectile {
  private static final int MAX_LIFETIME = 60;

  public SporeCloudProjectile(EntityType<? extends SporeCloudProjectile> type, Level level) {
    super(type, level);
  }

  public SporeCloudProjectile(Level level, LivingEntity shooter, Vec3 direction) {
    this(CHEXEntities.SPORE_CLOUD_PROJECTILE.get(), level);
    this.setOwner(shooter);
    this.setDeltaMovement(direction.normalize().scale(0.6F));
  }

  @Override
  protected void defineSynchedData() {}

  @Override
  protected void readAdditionalSaveData(CompoundTag tag) {}

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {}

  @Override
  public void tick() {
    super.tick();

    if (this.isRemoved()) {
      return;
    }

    Vec3 movement = this.getDeltaMovement();
    this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
    this.setDeltaMovement(movement.scale(0.95F));

    if (this.level().isClientSide) {
      this.level()
          .addParticle(
              ParticleTypes.SPORE_BLOSSOM_AIR,
              this.getX(),
              this.getY(),
              this.getZ(),
              0.0,
              0.0,
              0.0);
    }

    if (!this.level().isClientSide && this.tickCount > MAX_LIFETIME) {
      spawnCloud();
      this.discard();
    }
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);
    if (!this.level().isClientSide) {
      spawnCloud();
      this.discard();
    }
  }

  private void spawnCloud() {
    Level level = this.level();
    if (level.isClientSide) {
      return;
    }

    SporeCloudEntity cloud = CHEXEntities.SPORE_CLOUD.get().create(level);
    if (cloud == null) {
      return;
    }

    cloud.setPos(this.getX(), this.getY(), this.getZ());
    if (this.getOwner() instanceof LivingEntity owner) {
      cloud.setOwner(owner);
    }
    level.addFreshEntity(cloud);
  }
}
