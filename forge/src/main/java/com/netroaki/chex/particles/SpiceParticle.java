package com.netroaki.chex.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpiceParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private float targetScale = 0.15F;
  private float rotationSpeed;

  protected SpiceParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double dx,
      double dy,
      double dz,
      SpriteSet spriteSet) {
    super(level, x, y, z, dx, dy, dz);
    this.sprites = spriteSet;
    this.xd = dx * 0.1D;
    this.yd = dy * 0.1D + 0.02D;
    this.zd = dz * 0.1D;
    this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.8F);
    this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    this.hasPhysics = true;
    this.setSpriteFromAge(spriteSet);
    this.rotationSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -5.0D : 5.0D);
    this.roll = this.random.nextFloat() * ((float) Math.PI * 2F);
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
      this.roll += this.rotationSpeed;

      // Slight horizontal movement
      this.xd += (this.random.nextFloat() - 0.5F) * 0.002F;
      this.zd += (this.random.nextFloat() - 0.5F) * 0.002F;

      // Apply movement
      this.move(this.xd, this.yd, this.zd);

      // Fade out as particle ages
      if (this.age > this.lifetime / 2) {
        this.alpha =
            1.0F - ((float) this.age - (float) (this.lifetime / 2)) / (float) this.lifetime;
      }

      // Slight color variation over time
      float f = (float) this.age / (float) this.lifetime;
      this.rCol = Mth.lerp(f, 0.8F, 1.0F); // Orange to yellow
      this.gCol = Mth.lerp(f, 0.4F, 0.8F);
      this.bCol = Mth.lerp(f, 0.0F, 0.2F);
    }
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

    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double dx,
        double dy,
        double dz) {
      SpiceParticle particle = new SpiceParticle(level, x, y, z, dx, dy, dz, this.sprites);
      particle.pickSprite(this.sprites);
      return particle;
    }
  }
}
