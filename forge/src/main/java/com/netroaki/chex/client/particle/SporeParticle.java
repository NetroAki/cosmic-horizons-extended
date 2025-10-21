package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SporeParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final Vec3 target;
  private float rotSpeed;
  private float spinAcceleration;

  protected SporeParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double xd,
      double yd,
      double zd,
      SpriteSet sprites) {
    super(level, x, y, z, xd, yd, zd);
    this.sprites = sprites;
    this.quadSize *= 0.75f + random.nextFloat() * 0.5f;
    this.lifetime = (int) (20.0F / (random.nextFloat() * 0.8F + 0.2F));
    this.hasPhysics = true;
    this.gravity = 0.01f;
    this.friction = 0.9f;

    // Set random rotation
    this.roll = (float) (Math.PI * 2.0 * random.nextFloat());
    this.oRoll = this.roll;
    this.rotSpeed = (float) ((Math.random() - 0.5) * 0.1);
    this.spinAcceleration = (float) ((Math.random() - 0.5) * 0.001);

    // Set random target position for floating motion
    this.target =
        new Vec3(
            x + (random.nextDouble() - 0.5) * 2.0,
            y + random.nextDouble() * 2.0,
            z + (random.nextDouble() - 0.5) * 2.0);

    // Set random color variation
    float colorVariation = 0.1f + random.nextFloat() * 0.2f;
    this.rCol = 0.7f + colorVariation;
    this.gCol = 0.9f + colorVariation;
    this.bCol = 0.3f + colorVariation;

    // Set sprite
    setSpriteFromAge(sprites);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.oRoll = this.roll;

    if (this.age++ >= this.lifetime) {
      this.remove();
      return;
    }

    // Update rotation
    this.rotSpeed += this.spinAcceleration;
    this.roll += this.rotSpeed;

    // Calculate direction to target
    double dx = this.target.x - this.x;
    double dy = this.target.y - this.y;
    double dz = this.target.z - this.z;
    double distSq = dx * dx + dy * dy + dz * dz;

    // Move towards target with some randomness
    if (distSq < 0.1 || distSq > 100.0 || this.random.nextInt(100) == 0) {
      // Pick a new target
      this.target.set(
          this.x + (random.nextDouble() - 0.5) * 2.0,
          this.y + random.nextDouble() * 2.0,
          this.z + (random.nextDouble() - 0.5) * 2.0);
    } else {
      // Move towards target
      double speed = 0.02;
      this.xd += dx * 0.0025;
      this.yd += dy * 0.0025;
      this.zd += dz * 0.0025;

      // Add some random motion
      this.xd += (random.nextDouble() - 0.5) * 0.01;
      this.yd += (random.nextDouble() - 0.5) * 0.01;
      this.zd += (random.nextDouble() - 0.5) * 0.01;

      // Apply friction
      this.xd *= this.friction;
      this.yd *= this.friction;
      this.zd *= this.friction;
    }

    // Apply gravity
    this.yd -= this.gravity;

    // Move the particle
    this.move(this.xd, this.yd, this.zd);

    // Fade out at the end of life
    if (this.age >= this.lifetime - 10) {
      this.alpha = 1.0F - ((float) (this.age - (this.lifetime - 10)) / 10.0F);
    }

    // Update sprite
    if (!this.removed) {
      setSpriteFromAge(this.sprites);
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double xd,
        double yd,
        double zd) {
      return new SporeParticle(level, x, y, z, xd, yd, zd, this.sprites);
    }
  }
}
