package com.netroaki.chex.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class SandParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final float rotationSpeed;

  protected SandParticle(
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
    this.lifetime = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
    this.rotationSpeed = ((float) Math.random() - 0.5F) * 0.1F;
    this.roll = (float) Math.random() * ((float) Math.PI * 2F);

    // Random sand color variation
    float colorVariation = 0.8f + random.nextFloat() * 0.2f;
    this.rCol = colorVariation * 0.9f;
    this.gCol = colorVariation * 0.8f;
    this.bCol = colorVariation * 0.6f;

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
      this.oRoll = this.roll;

      // Apply gravity and air resistance
      this.yd -= 0.04D * (double) this.gravity;
      this.yd *= 0.98D;
      this.xd *= 0.9D;
      this.zd *= 0.9D;

      // Random movement variation
      if (!this.onGround) {
        this.xd += (this.random.nextFloat() - 0.5f) * 0.05f;
        this.yd += (this.random.nextFloat() - 0.5f) * 0.05f;
        this.zd += (this.random.nextFloat() - 0.5f) * 0.05f;
      }

      // Apply rotation
      this.roll += (float) Math.PI * this.rotationSpeed * 2.0F;

      // Fade out at the end of lifetime
      if (this.age > this.lifetime / 2) {
        this.alpha =
            1.0F - ((float) this.age - (float) (this.lifetime / 2)) / (float) this.lifetime;
      }

      this.move(this.xd, this.yd, this.zd);
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
      return new SandParticle(level, x, y, z, xd, yd, zd, this.sprites);
    }
  }
}
