package com.netroaki.chex.sound;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArrakisSounds {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CosmicHorizonsExpanded.MOD_ID);

  // Ambient wind sounds
  public static final RegistryObject<SoundEvent> WIND_LIGHT =
      registerSoundEvent("arrakis.wind.light");
  public static final RegistryObject<SoundEvent> WIND_MEDIUM =
      registerSoundEvent("arrakis.wind.medium");
  public static final RegistryObject<SoundEvent> WIND_STRONG =
      registerSoundEvent("arrakis.wind.strong");

  // Sandstorm sounds
  public static final RegistryObject<SoundEvent> SANDSTORM_START =
      registerSoundEvent("arrakis.sandstorm.start");
  public static final RegistryObject<SoundEvent> SANDSTORM_LOOP =
      registerSoundEvent("arrakis.sandstorm.loop");
  public static final RegistryObject<SoundEvent> SANDSTORM_END =
      registerSoundEvent("arrakis.sandstorm.end");

  // Environmental sounds
  public static final RegistryObject<SoundEvent> SAND_DRIFT =
      registerSoundEvent("arrakis.environment.sand_drift");
  public static final RegistryObject<SoundEvent> DUST_DEVIL =
      registerSoundEvent("arrakis.environment.dust_devil");

  // Distant sounds
  public static final RegistryObject<SoundEvent> DISTANT_WIND =
      registerSoundEvent("arrakis.distant.wind");
  public static final RegistryObject<SoundEvent> DISTANT_SANDSTORM =
      registerSoundEvent("arrakis.distant.sandstorm");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(
        name,
        () ->
            SoundEvent.createVariableRangeEvent(
                new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, name)));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
}
