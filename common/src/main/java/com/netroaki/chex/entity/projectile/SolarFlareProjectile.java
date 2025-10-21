package com.netroaki.chex.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SolarFlareProjectile extends AbstractHurtingProjectile {
  private float damage = 8.0F;
  private int life = 0;
  private final int maxLife = 100;

  public SolarFlareProjectile(EntityType<? extends SolarFlareProjectile> type, Level level) {
    super(type, level);
  }

  public SolarFlareProjectile(
      Level level,
      LivingEntity owner,
      double x,
      double y,
      double z,
      double dx,
      double dy,
      double dz) {
    super(ModEntityTypes.SOLAR_FLARE.get(), owner, dx, dy, dz, level);
    this.setPos(x, y, z);
    this.setNoGravity(true);
  }

  @Override
  public void tick() {
    super.tick();
    life++;

    // Visual effects
    if (this.level().isClientSide) {
      for (int i = 0; i < 2; ++i) {
        this.level()
            .addParticle(
                ParticleTypes.FLAME,
                this.getX() + (this.random.nextDouble() - 0.5) * 0.5,
                this.getY() + (this.random.nextDouble() - 0.5) * 0.5,
                this.getZ() + (this.random.nextDouble() - 0.5) * 0.5,
                0,
                0,
                0);
      }
    }

    // Remove if it goes too far or lives too long
    if (this.tickCount > 100 || this.distanceToSqr(this.getX(), this.getY(), this.getZ()) > 1000) {
      this.discard();
    }
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    super.onHitEntity(result);
    if (!this.level().isClientSide) {
      Entity entity = result.getEntity();
      Entity owner = this.getOwner();

      if (owner == null) {
        entity.hurt(this.damageSources().magic(), this.damage);
      } else {
        if (entity.hurt(this.damageSources().indirectMagic(this, owner), this.damage)) {
          if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setSecondsOnFire(5);
          }
        }
      }

      this.discard();
    }
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);
    if (!this.level().isClientSide) {
      // Create explosion on impact
      this.level()
          .explode(
              this.getOwner(),
              this.getX(),
              this.getY(),
              this.getZ(),
              1.5F,
              Level.ExplosionInteraction.MOB);

      this.discard();
    }
  }

  @Override
  protected float getInertia() {
    return 1.0F;
  }

  @Override
  public boolean isOnFire() {
    return false;
  }

  public void setDamage(float damage) {
    this.damage = damage;
  }
}
