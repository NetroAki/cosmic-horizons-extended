package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AshParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private float rotSpeed;
  private float spinAcceleration;

  protected AshParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed,
      SpriteSet spriteSet) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.sprites = spriteSet;

    // Random size between 0.05 and 0.15
    this.scale(0.1f * (this.random.nextFloat() * 0.5f + 0.5f));

    // Random lifetime between 30 and 90 ticks (1.5 to 4.5 seconds)
    this.lifetime = 30 + this.random.nextInt(60);

    // Random rotation
    this.roll = (float) (Math.PI * 2 * this.random.nextFloat());
    this.oRoll = this.roll;

    // Random rotation speed
    this.rotSpeed = (float) (Math.PI / 16) * (this.random.nextFloat() - 0.5f);
    this.spinAcceleration = (float) (Math.PI / 64) * (this.random.nextFloat() - 0.5f);

    // Set the sprite
    this.setSpriteFromAge(spriteSet);

    // Random color variation (slightly off-white to light gray)
    float colorVariation = 0.8f + this.random.nextFloat() * 0.2f;
    this.rCol = colorVariation * 0.9f;
    this.gCol = colorVariation * 0.9f;
    this.bCol = colorVariation * 0.9f;

    // Slight transparency
    this.alpha = 0.8f;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.oRoll = this.roll;

    // Update position with wind effect
    this.xd += this.random.nextGaussian() * 0.002;
    this.zd += this.random.nextGaussian() * 0.002;
    this.yd -= 0.02; // Slow falling speed

    // Update rotation
    this.rotSpeed += this.spinAcceleration;
    this.roll += this.rotSpeed;

    // Fade out as particle ages
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.setSpriteFromAge(this.sprites);
      if (this.age > this.lifetime / 2) {
        this.alpha = 0.8f * (1.0f - (float) (this.age - this.lifetime / 2) / (this.lifetime / 2));
      }
    }

    // Apply movement
    this.move(this.xd, this.yd, this.zd);

    // Slow down over time
    this.xd *= 0.98;
    this.yd *= 0.98;
    this.zd *= 0.98;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Provider(SpriteSet spriteSet) {
      this.sprites = spriteSet;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed) {
      return new AshParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
    }
  }
}
