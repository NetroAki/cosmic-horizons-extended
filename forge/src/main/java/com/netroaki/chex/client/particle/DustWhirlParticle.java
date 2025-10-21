package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DustWhirlParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final double centerX;
  private final double centerZ;
  private float angle;
  private float radius;
  private final float maxRadius;
  private final float rotationSpeed;
  private final float ySpeed;

  protected DustWhirlParticle(
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
    this.centerX = x;
    this.centerZ = z;
    this.quadSize = 0.2f + random.nextFloat() * 0.3f;
    this.lifetime = 100 + random.nextInt(40);
    this.maxRadius = 2.0f + random.nextFloat() * 3.0f;
    this.radius = random.nextFloat() * 0.5f;
    this.angle = (float) (random.nextFloat() * Math.PI * 2);
    this.rotationSpeed = (random.nextFloat() - 0.5f) * 0.02f;
    this.ySpeed = 0.01f + random.nextFloat() * 0.02f;

    // Dust color - slightly lighter than sand
    float colorVariation = 0.8f + random.nextFloat() * 0.2f;
    this.rCol = colorVariation * 1.0f;
    this.gCol = colorVariation * 0.9f;
    this.bCol = colorVariation * 0.7f;
    this.alpha = 0.3f;

    this.setSpriteFromAge(sprites);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.setSpriteFromAge(this.sprites);

      // Gradually increase radius
      if (this.radius < this.maxRadius) {
        this.radius += 0.02f;
      }

      // Update angle for circular motion
      this.angle += this.rotationSpeed;

      // Calculate new position
      this.x = this.centerX + Math.cos(this.angle) * this.radius;
      this.z = this.centerZ + Math.sin(this.angle) * this.radius;
      this.y += this.ySpeed;

      // Slight vertical oscillation
      this.y += Math.sin(this.age * 0.2f) * 0.01f;

      // Fade in and out
      if (this.age < 10) {
        this.alpha = this.age / 10.0f * 0.3f;
      } else if (this.age > this.lifetime - 10) {
        this.alpha = (this.lifetime - this.age) / 10.0f * 0.3f;
      } else {
        this.alpha = 0.3f;
      }

      // Random size variation
      this.quadSize = 0.2f + (float) Math.sin(this.age * 0.2f) * 0.1f + random.nextFloat() * 0.1f;
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Provider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Nullable
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
      return new DustWhirlParticle(level, x, y, z, xd, yd, zd, this.sprites);
    }
  }
}
