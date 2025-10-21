package com.netroaki.chex.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class IceShardProjectile extends ThrowableItemProjectile {
  private float damage = 4.0f;

  public IceShardProjectile(EntityType<? extends IceShardProjectile> type, Level level) {
    super(type, level);
  }

  public IceShardProjectile(Level level, LivingEntity shooter) {
    super(ModEntities.ICE_SHARD.get(), shooter, level);
  }

  public IceShardProjectile(Level level, double x, double y, double z) {
    super(ModEntities.ICE_SHARD.get(), x, y, z, level);
  }

  public void setDamage(float damage) {
    this.damage = damage;
  }

  @Override
  protected Item getDefaultItem() {
    return Items.ICE;
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    super.onHitEntity(result);
    if (!this.level().isClientSide) {
      Entity entity = result.getEntity();
      Entity owner = this.getOwner();

      boolean hurt =
          entity.hurt(
              this.damageSources()
                  .mobProjectile(this, owner instanceof LivingEntity ? (LivingEntity) owner : null),
              this.damage);

      if (hurt && entity instanceof LivingEntity livingEntity) {
        // Apply slowness effect
        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));

        // Small knockback
        double d0 = this.getX() - this.xo;
        double d1 = this.getZ() - this.zo;
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.1D);
        entity.push(d0 / d2 * 0.6D, 0.1D, d1 / d2 * 0.6D);
      }
    }
  }

  @Override
  protected void onHit(HitResult result) {
    super.onHit(result);
    if (!this.level().isClientSide) {
      this.level().broadcastEntityEvent(this, (byte) 3);
      this.discard();
    }
  }

  @Override
  public void tick() {
    super.tick();

    // Add particle trail
    if (this.level().isClientSide) {
      for (int i = 0; i < 2; ++i) {
        this.level()
            .addParticle(
                ParticleTypes.SNOWFLAKE,
                this.getX() + (this.random.nextDouble() - 0.5D) * 0.1D,
                this.getY() + (this.random.nextDouble() - 0.5D) * 0.1D,
                this.getZ() + (this.random.nextDouble() - 0.5D) * 0.1D,
                (this.random.nextDouble() - 0.5D) * 0.1D,
                (this.random.nextDouble() - 0.5D) * 0.1D,
                (this.random.nextDouble() - 0.5D) * 0.1D);
      }
    }

    // Remove if stuck in blocks
    if (this.tickCount > 100) {
      this.discard();
    }
  }
}
