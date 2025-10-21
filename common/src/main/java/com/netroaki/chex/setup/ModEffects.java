package com.netroaki.chex.setup;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.effect.RadiationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
  public static final DeferredRegister<MobEffect> EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<MobEffect> RADIATION_EFFECT =
      EFFECTS.register("radiation", () -> new RadiationEffect());

  public static void register(IEventBus eventBus) {
    EFFECTS.register(eventBus);
  }
}
