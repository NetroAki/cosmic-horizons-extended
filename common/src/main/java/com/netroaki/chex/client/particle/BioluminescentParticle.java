package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BioluminescentParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final float baseSize;
  private final float baseAlpha;
  private final float[] colorHsv = new float[3];

  protected BioluminescentParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double xd,
      double yd,
      double zd,
      float size,
      float r,
      float g,
      float b,
      float alpha,
      int lifetime,
      SpriteSet sprites) {
    super(level, x, y, z, xd, yd, zd);
    this.sprites = sprites;
    this.baseSize = size;
    this.baseAlpha = alpha;
    this.lifetime = lifetime;

    // Set initial color
    setColor(r, g, b);
    this.alpha = alpha;

    // Randomize initial rotation and angular velocity
    this.roll = (float) (Math.random() * Math.PI * 2.0);
    this.oRoll = this.roll;
    this.roll += (float) (Math.random() * 0.1 - 0.05);

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
    } else {
      // Update position based on velocity
      this.xd += this.random.nextGaussian() * 0.002;
      this.yd += this.random.nextGaussian() * 0.002;
      this.zd += this.random.nextGaussian() * 0.002;

      // Apply drag
      this.xd *= 0.95;
      this.yd *= 0.95;
      this.zd *= 0.95;

      // Update position
      this.x += this.xd;
      this.y += this.yd;
      this.z += this.zd;

      // Update rotation
      this.roll += 0.05f;

      // Update size and alpha with pulsing effect
      float progress = (float) this.age / this.lifetime;
      float pulse = 0.5f + 0.5f * Mth.sin(progress * (float) Math.PI * 2.0f);
      this.quadSize = this.baseSize * (0.8f + 0.2f * pulse);
      this.alpha = this.baseAlpha * (1.0f - progress * progress);

      // Update sprite
      setSpriteFromAge(this.sprites);
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Factory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Factory(SpriteSet sprites) {
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
      // Random color in blue-green spectrum
      float r = 0.2f + level.random.nextFloat() * 0.2f;
      float g = 0.6f + level.random.nextFloat() * 0.4f;
      float b = 0.8f + level.random.nextFloat() * 0.2f;

      return new BioluminescentParticle(
          level,
          x,
          y,
          z,
          xd,
          yd,
          zd,
          0.2f + level.random.nextFloat() * 0.3f, // Size
          r,
          g,
          b, // Color
          0.8f, // Alpha
          40 + level.random.nextInt(60), // Lifetime
          this.sprites);
    }
  }
}
