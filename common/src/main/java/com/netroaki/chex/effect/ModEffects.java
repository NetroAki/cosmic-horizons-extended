package com.netroaki.chex.effect;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
  public static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CosmicHorizonsExpanded.MOD_ID);

  public static final RegistryObject<MobEffect> PRESSURE_RESISTANCE =
      MOB_EFFECTS.register("pressure_resistance", PressureResistanceEffect::new);

  public static final RegistryObject<MobEffect> FROSTBITE =
      MOB_EFFECTS.register("frostbite", FrostbiteEffect::new);

  public static final RegistryObject<MobEffect> SNOW_BLINDNESS =
      MOB_EFFECTS.register("snow_blindness", SnowBlindnessEffect::new);

  public static void register(IEventBus eventBus) {
    MOB_EFFECTS.register(eventBus);
  }
}
