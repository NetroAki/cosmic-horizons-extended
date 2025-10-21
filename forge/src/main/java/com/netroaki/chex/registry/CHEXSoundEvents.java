package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXSoundEvents {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CHEX.MOD_ID);

  // Ambient Sounds
  public static final RegistryObject<SoundEvent> BIOLUME_HUM =
      registerSoundEvent("ambient.pandora.biolume_hum");
  public static final RegistryObject<SoundEvent> WIND_AMBIENT =
      registerSoundEvent("ambient.pandora.wind");

  // Hazard Sounds
  public static final RegistryObject<SoundEvent> UPDRAFT_WHOOSH =
      registerSoundEvent("hazard.pandora.updraft");
  public static final RegistryObject<SoundEvent> HEAT_CRACKLE =
      registerSoundEvent("hazard.pandora.heat_crackle");
  public static final RegistryObject<SoundEvent> SPORE_DRIFT =
      registerSoundEvent("hazard.pandora.spore_drift");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(
        name,
        () ->
            SoundEvent.createVariableRangeEvent(ResourceLocation.parse(CHEX.MOD_ID + ":" + name)));
  }
}
