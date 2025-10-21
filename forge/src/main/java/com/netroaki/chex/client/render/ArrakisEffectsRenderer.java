package com.netroaki.chex.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.netroaki.chex.CHEX;
import java.util.Random;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
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
public class ArrakisEffectsRenderer {
  private static final ResourceLocation HEAT_HAZE_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/environment/heat_haze.png");
  private static final Random RANDOM = new Random();
  private static float heatHazeIntensity = 0.0f;
  private static float sandstormIntensity = 0.0f;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null || mc.level == null) return;

    // Update effect intensities based on biome and conditions
    updateEffectIntensities(player, mc.level);

    // Spawn sand particles during sandstorms
    if (sandstormIntensity > 0.1f && mc.level.getGameTime() % 2 == 0) {
      spawnSandParticles(mc, player);
    }
  }

  @SubscribeEvent
  public static void onRenderLevelLast(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;

    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null || mc.level == null) return;

    // Render heat haze
    if (heatHazeIntensity > 0.01f) {
      renderHeatHaze(event.getPoseStack(), mc, event.getCamera(), event.getPartialTick());
    }

    // Render sandstorm effects
    if (sandstormIntensity > 0.1f) {
      renderSandstorm(event.getPoseStack(), mc, event.getCamera(), event.getPartialTick());
    }
  }

  private static void updateEffectIntensities(Player player, Level level) {
    // Check if player is in a desert biome
    boolean isInDesert = isInDesertBiome(level, player.blockPosition());

    // Update heat haze intensity (smooth transition)
    float targetHeatHaze = isInDesert ? 1.0f : 0.0f;
    heatHazeIntensity = Mth.lerp(0.05f, heatHazeIntensity, targetHeatHaze);

    // Check for sandstorm conditions (simplified - could be tied to weather)
    boolean isSandstorm =
        isInDesert && level.isRaining() && level.canSeeSky(player.blockPosition().above());
    float targetSandstorm = isSandstorm ? 1.0f : 0.0f;
    sandstormIntensity = Mth.lerp(0.02f, sandstormIntensity, targetSandstorm);
  }

  private static boolean isInDesertBiome(Level level, BlockPos pos) {
    Biome biome = level.getBiome(pos).value();
    return biome.is(Biomes.DESERT)
        || (biome.getRegistryName() != null
            && biome.getRegistryName().getNamespace().equals(CHEX.MOD_ID)
            && biome.getRegistryName().getPath().contains("desert"));
  }

  private static void spawnSandParticles(Minecraft mc, Player player) {
    ClientLevel level = mc.level;
    if (level == null) return;

    int particles = 5 + RANDOM.nextInt(5);
    for (int i = 0; i < particles; i++) {
      double x = player.getX() + (RANDOM.nextDouble() - 0.5) * 20.0;
      double z = player.getZ() + (RANDOM.nextDouble() - 0.5) * 20.0;
      double y = player.getY() + 2.0 + RANDOM.nextDouble() * 5.0;

      // Check if position is above ground
      BlockPos pos = new BlockPos(x, y, z);
      if (level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos).getY() > y) {
        continue;
      }

      // Create sand particle
      double dx = (RANDOM.nextDouble() - 0.5) * 0.5;
      double dy = RANDOM.nextDouble() * 0.1 - 0.2;
      double dz = (RANDOM.nextDouble() - 0.5) * 0.5;

      level.addParticle(CHEXParticleTypes.SAND_PARTICLE.get(), x, y, z, dx, dy, dz);
    }
  }

  private static void renderHeatHaze(
      PoseStack poseStack, Minecraft mc, Camera camera, float partialTicks) {
    // Calculate distance-based intensity
    float distance = (float) camera.getPosition().distanceTo(camera.getEntity().position());
    float distanceFactor = Mth.clamp(distance / 16.0f, 0.0f, 1.0f);

    // Skip if too close to the ground
    if (distance < 2.0f) return;

    // Set up rendering
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucentCull(HEAT_HAZE_TEXTURE));

    // Calculate view angles
    float time = (mc.level.getGameTime() + partialTicks) * 0.01f;
    float yaw = (float) Math.toRadians(camera.getYRot());

    // Draw multiple layers for better effect
    for (int i = 0; i < 3; i++) {
      poseStack.pushPose();

      // Position at player's feet
      Vec3 pos = camera.getPosition().subtract(0, 1.0, 0);
      poseStack.translate(-pos.x, -pos.y, -pos.z);

      // Apply rotation and scaling
      float scale = 16.0f + i * 4.0f;
      float offset = i * 0.3f;

      poseStack.mulPose(Axis.YP.rotation(yaw));
      poseStack.translate(0, 0, -offset);
      poseStack.scale(scale, scale, scale);

      // Animate the heat haze
      float uOffset = time * (0.1f + i * 0.05f);
      float vOffset = time * (0.07f - i * 0.03f);

      // Draw quad
      Matrix4f matrix = poseStack.last().pose();
      float alpha = 0.15f * heatHazeIntensity * distanceFactor * (1.0f - i * 0.2f);

      builder
          .vertex(matrix, -1, 0, -1)
          .color(1, 1, 1, alpha)
          .uv(0 + uOffset, 0 + vOffset)
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(LightTexture.FULL_BRIGHT)
          .normal(0, 1, 0)
          .endVertex();
      builder
          .vertex(matrix, 1, 0, -1)
          .color(1, 1, 1, alpha)
          .uv(1 + uOffset, 0 + vOffset)
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(LightTexture.FULL_BRIGHT)
          .normal(0, 1, 0)
          .endVertex();
      builder
          .vertex(matrix, 1, 0, 1)
          .color(1, 1, 1, alpha)
          .uv(1 + uOffset, 1 + vOffset)
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(LightTexture.FULL_BRIGHT)
          .normal(0, 1, 0)
          .endVertex();
      builder
          .vertex(matrix, -1, 0, 1)
          .color(1, 1, 1, alpha)
          .uv(0 + uOffset, 1 + vOffset)
          .overlayCoords(OverlayTexture.NO_OVERLAY)
          .uv2(LightTexture.FULL_BRIGHT)
          .normal(0, 1, 0)
          .endVertex();

      poseStack.popPose();
    }

    // Draw the buffer
    buffer.endBatch();
  }

  private static void renderSandstorm(
      PoseStack poseStack, Minecraft mc, Camera camera, float partialTicks) {
    // Skip if intensity is too low
    if (sandstormIntensity < 0.1f) return;

    // Set up rendering
    MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
    VertexConsumer builder = buffer.getBuffer(RenderType.particle());

    // Get camera position and orientation
    Vec3 cameraPos = camera.getPosition();
    float time = (mc.level.getGameTime() + partialTicks) * 0.05f;

    // Render multiple layers of sand particles
    int layers = 3;
    int particlesPerLayer = 100;

    for (int l = 0; l < layers; l++) {
      float layerHeight = 5.0f + l * 3.0f;
      float layerIntensity = sandstormIntensity * (1.0f - l * 0.3f);

      if (layerIntensity < 0.1f) continue;

      for (int i = 0; i < particlesPerLayer; i++) {
        // Generate random position in a cylinder around the player
        float radius = 20.0f + RANDOM.nextFloat() * 30.0f;
        float angle = RANDOM.nextFloat() * (float) (2 * Math.PI);
        float x = (float) (Math.cos(angle) * radius);
        float z = (float) (Math.sin(angle) * radius);
        float y = RANDOM.nextFloat() * 10.0f + layerHeight;

        // Animate the particles
        float speed = 0.1f + RANDOM.nextFloat() * 0.2f;
        x += Math.sin(time * 0.5f + i * 0.1f) * 2.0f;
        z += Math.cos(time * 0.3f + i * 0.15f) * 2.0f;

        // Calculate world position
        double px = cameraPos.x + x;
        double py = cameraPos.y - 5.0 + y;
        double pz = cameraPos.z + z;

        // Skip if below ground
        BlockPos pos = new BlockPos(px, py, pz);
        if (mc.level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos).getY() > py) {
          continue;
        }

        // Calculate particle color (sandy yellow/brown)
        float r = 0.9f - RANDOM.nextFloat() * 0.2f;
        float g = 0.8f - RANDOM.nextFloat() * 0.3f;
        float b = 0.4f - RANDOM.nextFloat() * 0.1f;
        float a = 0.7f * layerIntensity;

        // Draw particle
        Matrix4f matrix = poseStack.last().pose();
        float size = 0.1f + RANDOM.nextFloat() * 0.2f;

        builder
            .vertex(matrix, (float) (px - size), (float) (py - size), (float) pz)
            .color(r, g, b, a)
            .uv(0, 0)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, (float) (px + size), (float) (py - size), (float) pz)
            .color(r, g, b, a)
            .uv(1, 0)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, (float) (px + size), (float) (py + size), (float) pz)
            .color(r, g, b, a)
            .uv(1, 1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(0, 1, 0)
            .endVertex();
        builder
            .vertex(matrix, (float) (px - size), (float) (py + size), (float) pz)
            .color(r, g, b, a)
            .uv(0, 1)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(0, 1, 0)
            .endVertex();
      }
    }

    // Draw the buffer
    buffer.endBatch();
  }

  // Particle type for sand particles
  public static class SandParticle extends TextureSheetParticle {
    protected SandParticle(
        ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
      super(level, x, y, z, dx, dy, dz);
      this.lifetime = 40 + random.nextInt(20);
      this.quadSize = 0.1f + random.nextFloat() * 0.2f;
      this.rCol = 0.9f - random.nextFloat() * 0.2f;
      this.gCol = 0.8f - random.nextFloat() * 0.3f;
      this.bCol = 0.4f - random.nextFloat() * 0.1f;
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
        this.yd -= 0.04D * this.gravity;
        this.move(this.xd, this.yd, this.zd);

        // Air resistance
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        // Random wind
        if (this.onGround) {
          this.xd *= 0.7D;
          this.zd *= 0.7D;
        } else {
          // Add some wind effect
          this.xd += (random.nextFloat() - 0.5f) * 0.01f;
          this.zd += (random.nextFloat() - 0.5f) * 0.01f;
        }
      }
    }

    @Override
    public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double dx,
        double dy,
        double dz) {
      return new SandParticle(level, x, y, z, dx, dy, dz);
    }
  }
}
