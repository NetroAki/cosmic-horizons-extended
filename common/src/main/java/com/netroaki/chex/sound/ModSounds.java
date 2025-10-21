package com.netroaki.chex.sound;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CosmicHorizonsExpanded.MOD_ID);

  // Stellar Avatar Sounds
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_AMBIENT =
      registerSoundEvent("stellar_avatar_ambient");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_HURT =
      registerSoundEvent("stellar_avatar_hurt");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_DEATH =
      registerSoundEvent("stellar_avatar_death");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_PHASE_CHANGE =
      registerSoundEvent("stellar_avatar_phase_change");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_ATTACK_FLARE =
      registerSoundEvent("stellar_avatar_attack_flare");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_ATTACK_CORONAL =
      registerSoundEvent("stellar_avatar_attack_coronal");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_ATTACK_NOVA =
      registerSoundEvent("stellar_avatar_attack_nova");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_SHIELD_ACTIVATE =
      registerSoundEvent("stellar_avatar_shield_activate");
  public static final RegistryObject<SoundEvent> STELLAR_AVATAR_SHIELD_BREAK =
      registerSoundEvent("stellar_avatar_shield_break");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    ResourceLocation id = new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, name);
    return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
}
