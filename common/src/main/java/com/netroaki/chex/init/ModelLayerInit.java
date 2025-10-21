package com.netroaki.chex.init;

import com.netroaki.chex.CHEX;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = CHEX.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT)
public class ModelLayerInit {
  public static final ModelLayerLocation SANDWORM_JUVENILE = register("sandworm_juvenile");

  // Add more model layers here as needed

  private static ModelLayerLocation register(String name) {
    return register(name, "main");
  }

  private static ModelLayerLocation register(String name, String layer) {
    return new ModelLayerLocation(new ResourceLocation(CHEX.MOD_ID, name), layer);
  }

  @SubscribeEvent
  public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    // TODO: Register layer definition when SandwormJuvenileModel is implemented
    // event.registerLayerDefinition(SANDWORM_JUVENILE,
    // SandwormJuvenileModel::createBodyLayer);
    // Register more layer definitions here
  }
}
