package com.netroaki.chex.client;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.client.renderer.RingworldWallRenderer;
import com.netroaki.chex.client.renderer.entity.PlaceholderCubeRenderer;
import com.netroaki.chex.registry.CHEXBlockEntities;
import com.netroaki.chex.registry.entities.CHEXEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class CHEXClientSetup {

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(
        CHEXBlockEntities.RINGWORLD_WALL.get(), RingworldWallRenderer::new);

    // Temporary cube renderers for every CHEX entity so spawning them does not crash.
    registerPlaceholder(event, CHEXEntities.FLOATING_CRYSTAL.get(), "floating_crystal", 0.6F);
    registerPlaceholder(event, CHEXEntities.GLOWBEAST.get(), "glowbeast", 0.8F);
    registerPlaceholder(event, CHEXEntities.SPOREFLIES.get(), "sporeflies", 0.6F);
    registerPlaceholder(event, CHEXEntities.SPOREFLY.get(), "sporefly", 0.7F);
    registerPlaceholder(event, CHEXEntities.SPORELING.get(), "sporeling", 0.5F);
    registerPlaceholder(event, CHEXEntities.ELITE_SPORELING.get(), "elite_sporeling", 0.5F);
    registerPlaceholder(event, CHEXEntities.SPORE_CLOUD.get(), "spore_cloud", 0.4F);
    registerPlaceholder(
        event, CHEXEntities.SPORE_CLOUD_PROJECTILE.get(), "spore_cloud_projectile", 0.35F);
    registerPlaceholder(event, CHEXEntities.SKY_GRAZER.get(), "sky_grazer", 1.0F);
    registerPlaceholder(event, CHEXEntities.CLIFF_HUNTER.get(), "cliff_hunter", 1.2F);
    registerPlaceholder(event, CHEXEntities.SPORE_TYRANT.get(), "spore_tyrant", 1.8F);
    registerPlaceholder(
        event, CHEXEntities.CLIFF_HUNTER_ALPHA.get(), "cliff_hunter_alpha", 1.5F);
    registerPlaceholder(event, CHEXEntities.DEEP_SEA_SIREN.get(), "deep_sea_siren", 1.5F);
    registerPlaceholder(event, CHEXEntities.MOLTEN_BEHEMOTH.get(), "molten_behemoth", 1.8F);
    registerPlaceholder(event, CHEXEntities.STORM_ROC.get(), "storm_roc", 1.6F);
    registerPlaceholder(event, CHEXEntities.SKY_SOVEREIGN.get(), "sky_sovereign", 1.7F);
    registerPlaceholder(event, CHEXEntities.WORLDHEART_AVATAR.get(), "worldheart_avatar", 1.7F);
  }

  private static void registerPlaceholder(
      EntityRenderersEvent.RegisterRenderers event,
      EntityType<? extends Entity> type,
      String textureName,
      float baseScale) {
    event.registerEntityRenderer(
        type,
        context ->
            new PlaceholderCubeRenderer<>(context, entityTexture(textureName), baseScale));
  }

  private static ResourceLocation entityTexture(String name) {
    return new ResourceLocation(CHEX.MOD_ID, "textures/entity/" + name + ".png");
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(
        PlaceholderCubeRenderer.LAYER_LOCATION, PlaceholderCubeRenderer::createLayer);
  }

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {});
  }

  @SubscribeEvent
  public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
    event.register(
        new ResourceLocation(CHEX.MOD_ID, "pandora"), PandoraSkyEffects.INSTANCE);
    event.register(
        new ResourceLocation(CHEX.MOD_ID, "arrakis"), ArrakisSkyEffects.INSTANCE);
  }

  @SubscribeEvent
  public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
    // Placeholder: particle factories will be re-enabled with their respective systems.
  }
}
