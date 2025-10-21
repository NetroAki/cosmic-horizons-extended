package com.netroaki.chex.client.particle;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WindParticle extends TextureSheetParticle {
  private static final Random RANDOM = new Random();
  private final double xStart;
  private final double yStart;
  private final double zStart;
  private final double xDir;
  private final double yDir;
  private final double zDir;
  private final float scaleFactor;

  protected WindParticle(
      ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
    super(level, x, y, z, 0.0D, 0.0D, 0.0D);
    this.xd = xd;
    this.yd = yd;
    this.zd = zd;
    this.xStart = x;
    this.yStart = y;
    this.zStart = z;
    this.xo = x + xd;
    this.yo = y + yd;
    this.zo = z + zd;
    this.x = this.xo;
    this.y = this.yo;
    this.z = this.zo;

    // Randomize initial position slightly
    this.x += (RANDOM.nextDouble() - 0.5) * 0.5;
    this.y += (RANDOM.nextDouble() - 0.5) * 0.5;
    this.z += (RANDOM.nextDouble() - 0.5) * 0.5;

    // Random size and lifetime
    this.scaleFactor = 0.1f * (RANDOM.nextFloat() * 0.5f + 0.5f) * 2.0f;
    this.quadSize = 0.2f * (RANDOM.nextFloat() * 0.5f + 0.5f) * 2.0f;
    this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
    this.lifetime = (int) (this.lifetime * 2.0D);

    // Random color variation (bluish-gray)
    float colorVar = 0.8f + RANDOM.nextFloat() * 0.2f;
    this.rCol = 0.7f * colorVar;
    this.gCol = 0.8f * colorVar;
    this.bCol = 0.9f * colorVar;

    // Initial velocity based on wind direction
    Vec3 windVec = new Vec3(xd, yd, zd).normalize().scale(0.05);
    this.xd = windVec.x;
    this.yd = windVec.y;
    this.zd = windVec.z;

    this.xDir = windVec.x * 0.1;
    this.yDir = windVec.y * 0.1;
    this.zDir = windVec.z * 0.1;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      // Update position with wind direction and some noise
      float f = (float) this.age / (float) this.lifetime;
      float f1 = -f + f * f * 2.0F;

      // Add some noise to the movement
      double noiseX = Mth.sin((float) (this.age * 0.1) * 3.1415927F * 0.5F) * 0.1;
      double noiseZ = Mth.cos((float) (this.age * 0.1) * 3.1415927F * 0.5F) * 0.1;

      // Update position with wind direction and noise
      this.x = this.xStart + this.xd * f1 + noiseX;
      this.y = this.yStart + this.yd * f1 + (RANDOM.nextDouble() - 0.5) * 0.1;
      this.z = this.zStart + this.zd * f1 + noiseZ;

      // Update velocity with wind direction and noise
      this.xd = this.xDir + noiseX * 0.1;
      this.zd = this.zDir + noiseZ * 0.1;

      // Fade out at the end of the particle's life
      if (f > 0.8f) {
        this.alpha = (1.0f - (f - 0.8f) * 5.0f) * 0.5f;
      } else {
        this.alpha = 0.5f;
      }

      // Update size based on lifetime
      this.quadSize = this.scaleFactor * (1.0f - f * 0.5f);

      // Apply gravity and air resistance
      this.yd -= 0.004D;
      this.yd = Math.max(this.yd, -0.1D);

      // Move the particle
      this.move(this.xd, this.yd, this.zd);

      // Slow down over time
      this.xd *= 0.98D;
      this.yd *= 0.98D;
      this.zd *= 0.98D;

      // If on ground, remove the particle
      if (this.onGround) {
        this.remove();
      }
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @OnlyIn(Dist.CLIENT)
  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
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
      WindParticle particle = new WindParticle(level, x, y, z, xd, yd, zd);
      particle.pickSprite(this.spriteSet);
      return particle;
    }
  }
}
