package com.netroaki.chex.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.netroaki.chex.CHEX;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, value = Dist.CLIENT)
public class SpiceBlowRenderer {
  private static final ResourceLocation SPICE_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/particle/spice_particle.png");
  private static final Random RANDOM = new Random();
  private static SpiceBlowEvent activeEvent = null;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    if (mc.level == null || mc.player == null) return;

    // Random chance to start a spice blow event
    if (activeEvent == null && RANDOM.nextInt(2000) == 0) {
      // Check if player is in a desert biome
      BlockPos playerPos = mc.player.blockPosition();
      if (isInDesertBiome(mc.level, playerPos)) {
        // Find a suitable location near the player
        double angle = RANDOM.nextDouble() * Math.PI * 2;
        double distance = 20.0 + RANDOM.nextDouble() * 50.0;
        double x = playerPos.getX() + Math.cos(angle) * distance;
        double z = playerPos.getZ() + Math.sin(angle) * distance;
        double y = mc.level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z);

        // Make sure it's not in water or other invalid locations
        BlockPos pos = new BlockPos((int) x, (int) y, (int) z);
        if (mc.level.getBlockState(pos.below()).isAir()
            || mc.level.getBlockState(pos).getMaterial().isLiquid()) {
          return;
        }

        // Start the spice blow event
        activeEvent = new SpiceBlowEvent(pos, 200 + RANDOM.nextInt(200)); // 10-20 seconds
      }
    }

    // Update active event
    if (activeEvent != null) {
      activeEvent.tick(mc);
      if (activeEvent.isComplete()) {
        activeEvent = null;
      }
    }
  }

  @SubscribeEvent
  public static void onRenderLevelLast(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;
    if (activeEvent == null) return;

    activeEvent.render(event.getPoseStack(), event.getCamera(), event.getPartialTick());
  }

  private static boolean isInDesertBiome(Level level, BlockPos pos) {
    Biome biome = level.getBiome(pos).value();
    return biome.is(Biomes.DESERT)
        || (biome.getRegistryName() != null
            && biome.getRegistryName().getNamespace().equals(CHEX.MOD_ID)
            && biome.getRegistryName().getPath().contains("desert"));
  }

  private static class SpiceBlowEvent {
    private final BlockPos position;
    private int duration;
    private int timer = 0;
    private final int maxParticles;
    private boolean hasSpawnedSpice = false;

    public SpiceBlowEvent(BlockPos position, int duration) {
      this.position = position;
      this.duration = duration;
      this.maxParticles = 100 + RANDOM.nextInt(100);
    }

    public void tick(Minecraft mc) {
      timer++;

      // Spawn particles during the main phase
      if (timer > 20 && timer < duration - 40) {
        if (mc.level != null) {
          // Spawn spice particles
          for (int i = 0; i < 3; i++) {
            double x = position.getX() + (RANDOM.nextDouble() - 0.5) * 3.0;
            double z = position.getZ() + (RANDOM.nextDouble() - 0.5) * 3.0;
            double y = position.getY() + 0.1;

            double dx = (RANDOM.nextDouble() - 0.5) * 0.2;
            double dy = RANDOM.nextDouble() * 0.1 + 0.1;
            double dz = (RANDOM.nextDouble() - 0.5) * 0.2;

            mc.level.addParticle(CHEXParticleTypes.SPICE_PARTICLE.get(), x, y, z, dx, dy, dz);
          }

          // Spawn dust cloud particles
          if (RANDOM.nextInt(3) == 0) {
            double x = position.getX() + (RANDOM.nextDouble() - 0.5) * 2.0;
            double z = position.getZ() + (RANDOM.nextDouble() - 0.5) * 2.0;
            double y = position.getY() + 0.1;

            double dx = (RANDOM.nextDouble() - 0.5) * 0.1;
            double dy = RANDOM.nextDouble() * 0.05;
            double dz = (RANDOM.nextDouble() - 0.5) * 0.1;

            mc.level.addParticle(CHEXParticleTypes.SAND_PARTICLE.get(), x, y, z, dx, dy, dz);
          }
        }
      }

      // Spawn spice block at the peak of the event
      if (!hasSpawnedSpice && timer > duration / 2) {
        hasSpawnedSpice = true;
        // TODO: Spawn spice block in the world
      }
    }

    public void render(PoseStack poseStack, Camera camera, float partialTicks) {
      if (timer >= duration) return;

      float progress = (timer + partialTicks) / (float) duration;
      float scale = Mth.sin(progress * (float) Math.PI) * 5.0f + 1.0f;

      // Set up rendering
      MultiBufferSource.BufferSource buffer =
          Minecraft.getInstance().renderBuffers().bufferSource();
      VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(SPICE_TEXTURE));

      // Calculate position
      double x = position.getX() + 0.5;
      double y = position.getY() + progress * 2.0;
      double z = position.getZ() + 0.5;

      // Calculate camera-relative position
      Vec3 cameraPos = camera.getPosition();
      float dx = (float) (x - cameraPos.x);
      float dy = (float) (y - cameraPos.y);
      float dz = (float) (z - cameraPos.z);

      // Build rotation matrix to face camera
      poseStack.pushPose();
      poseStack.translate(dx, dy, dz);
      poseStack.mulPose(camera.rotation());
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

      // Calculate animation
      float time = (timer + partialTicks) * 0.1f;
      float pulse = Mth.sin(time) * 0.1f + 1.0f;
      scale *= pulse;

      // Draw the spice cloud
      Matrix4f matrix = poseStack.last().pose();
      float alpha = Mth.clamp(progress * 3.0f, 0.0f, 1.0f) * 0.7f;

      // Draw multiple layers for better effect
      for (int i = 0; i < 3; i++) {
        float layerScale = scale * (1.0f - i * 0.1f);
        float layerAlpha = alpha * (1.0f - i * 0.2f);
        float uOffset = time * 0.1f * (i + 1);
        float vOffset = time * 0.07f * (i + 1);

        builder
            .vertex(matrix, -layerScale, -layerScale, 0)
            .color(1, 1, 1, layerAlpha)
            .uv(0 + uOffset, 1 + vOffset)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(0xF000F0)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, layerScale, -layerScale, 0)
            .color(1, 1, 1, layerAlpha)
            .uv(1 + uOffset, 1 + vOffset)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(0xF000F0)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, layerScale, layerScale, 0)
            .color(1, 1, 1, layerAlpha)
            .uv(1 + uOffset, 0 + vOffset)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(0xF000F0)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, -layerScale, layerScale, 0)
            .color(1, 1, 1, layerAlpha)
            .uv(0 + uOffset, 0 + vOffset)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(0xF000F0)
            .normal(0, 1, 0)
            .endVertex();
      }

      poseStack.popPose();
      buffer.endBatch();
    }

    public boolean isComplete() {
      return timer >= duration;
    }
  }

  // Particle type for spice particles
  public static class SpiceParticle extends TextureSheetParticle {
    private final float rotSpeed;

    protected SpiceParticle(
        ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
      super(level, x, y, z, dx, dy, dz);
      this.lifetime = 40 + random.nextInt(40);
      this.quadSize = 0.2f + random.nextFloat() * 0.3f;
      this.rotSpeed = (random.nextFloat() - 0.5f) * 0.1f;

      // Orange/brown spice color
      this.rCol = 0.9f - random.nextFloat() * 0.2f;
      this.gCol = 0.6f - random.nextFloat() * 0.2f;
      this.bCol = 0.2f - random.nextFloat() * 0.1f;

      // Random rotation
      this.roll = random.nextFloat() * (float) Math.PI * 2.0f;
    }

    @Override
    public void tick() {
      super.tick();
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;

      if (this.age++ >= this.lifetime) {
        this.remove();
      } else {
        // Update position with gravity and air resistance
        this.yd -= 0.02D * this.gravity;
        this.move(this.xd, this.yd, this.zd);

        // Air resistance
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        // Random movement
        if (this.onGround) {
          this.xd *= 0.7D;
          this.zd *= 0.7D;
        } else {
          // Add some random movement
          this.xd += (random.nextDouble() - 0.5) * 0.002;
          this.zd += (random.nextDouble() - 0.5) * 0.002;

          // Rotate the particle
          this.roll += rotSpeed;
        }

        // Fade out at the end
        if (this.age > this.lifetime - 10) {
          this.alpha = (this.lifetime - this.age) / 10.0f;
        }
      }
    }

    @Override
    public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    @Override
    public net.minecraft.client.particle.Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double dx,
        double dy,
        double dz) {
      return new SpiceParticle(level, x, y, z, dx, dy, dz);
    }
  }
}
