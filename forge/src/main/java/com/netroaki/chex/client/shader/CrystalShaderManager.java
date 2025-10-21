package com.netroaki.chex.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrystalShaderManager {
  private static ShaderInstance crystalShader;

  @SubscribeEvent
  public static void onRegisterShaders(RegisterShadersEvent event) {
    try {
      ResourceProvider resourceProvider = event.getResourceProvider();
      event.registerShader(
          new ShaderInstance(
              resourceProvider,
              new ResourceLocation("chex", "crystal_refraction"),
              DefaultVertexFormat.NEW_ENTITY),
          shader -> crystalShader = shader);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load crystal shader", e);
    }
  }

  public static ShaderInstance getCrystalShader() {
    return crystalShader;
  }

  public static boolean isShaderActive() {
    return crystalShader != null;
  }
}
