package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class HeatHazeParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final float baseSize;
  private final float speed;
  private float offsetX;
  private float offsetZ;
  private final float maxOffset = 0.5f;

  protected HeatHazeParticle(
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
    this.baseSize = 0.5f + random.nextFloat() * 1.5f;
    this.quadSize = this.baseSize;
    this.lifetime = 40 + random.nextInt(40);
    this.speed = 0.01f + random.nextFloat() * 0.02f;

    // Heat haze is semi-transparent white
    this.rCol = 1.0f;
    this.gCol = 1.0f;
    this.bCol = 1.0f;
    this.alpha = 0.1f + random.nextFloat() * 0.1f;

    // Random initial offset
    this.offsetX = (random.nextFloat() - 0.5f) * 2.0f * maxOffset;
    this.offsetZ = (random.nextFloat() - 0.5f) * 2.0f * maxOffset;

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

      // Gentle floating motion
      this.y += speed;

      // Slight horizontal movement
      this.offsetX += (random.nextFloat() - 0.5f) * 0.02f;
      this.offsetZ += (random.nextFloat() - 0.5f) * 0.02f;

      // Keep offset within bounds
      this.offsetX = Mth.clamp(this.offsetX, -maxOffset, maxOffset);
      this.offsetZ = Mth.clamp(this.offsetZ, -maxOffset, maxOffset);

      // Apply offset to position
      this.x = this.xo + offsetX;
      this.z = this.zo + offsetZ;

      // Pulsing size
      float pulse = (float) Math.sin(this.age * 0.1f) * 0.1f + 1.0f;
      this.quadSize = this.baseSize * pulse;

      // Fade in and out
      if (this.age < 10) {
        this.alpha = this.age / 10.0f * 0.2f;
      } else if (this.age > this.lifetime - 10) {
        this.alpha = (this.lifetime - this.age) / 10.0f * 0.2f;
      } else {
        this.alpha = 0.1f + random.nextFloat() * 0.1f;
      }
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
      return new HeatHazeParticle(level, x, y, z, xd, yd, zd, this.sprites);
    }
  }
}
