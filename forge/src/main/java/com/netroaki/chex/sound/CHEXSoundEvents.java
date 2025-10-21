package com.netroaki.chex.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXSoundEvents {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "chex");

  // Desert Ambience
  public static final RegistryObject<SoundEvent> AMBIENT_DESERT_WIND =
      registerSoundEvent("ambient.desert.wind");
  public static final RegistryObject<SoundEvent> AMBIENT_DESERT_NIGHT =
      registerSoundEvent("ambient.desert.night");
  public static final RegistryObject<SoundEvent> AMBIENT_DESERT_DAY =
      registerSoundEvent("ambient.desert.day");

  // Weather Sounds
  public static final RegistryObject<SoundEvent> WEATHER_SANDSTORM =
      registerSoundEvent("weather.sandstorm");
  public static final RegistryObject<SoundEvent> WEATHER_SANDSTORM_LOOP =
      registerSoundEvent("weather.sandstorm.loop");

  // Creature Sounds
  public static final RegistryObject<SoundEvent> ENTITY_SANDWORM_AMBIENT =
      registerSoundEvent("entity.sandworm.ambient");
  public static final RegistryObject<SoundEvent> ENTITY_SANDWORM_HURT =
      registerSoundEvent("entity.sandworm.hurt");
  public static final RegistryObject<SoundEvent> ENTITY_SANDWORM_DEATH =
      registerSoundEvent("entity.sandworm.death");

  // Environmental Sounds
  public static final RegistryObject<SoundEvent> BLOCK_SAND_SHIFT =
      registerSoundEvent("block.sand.shift");
  public static final RegistryObject<SoundEvent> BLOCK_SPICE_CRYSTAL_AMBIENT =
      registerSoundEvent("block.spice_crystal.ambient");

  // Music
  public static final RegistryObject<SoundEvent> MUSIC_DESERT_DAY =
      registerSoundEvent("music.desert.day");
  public static final RegistryObject<SoundEvent> MUSIC_DESERT_NIGHT =
      registerSoundEvent("music.desert.night");
  public static final RegistryObject<SoundEvent> MUSIC_DESERT_DANGER =
      registerSoundEvent("music.desert.danger");

  // Pandora Ambience
  public static final RegistryObject<SoundEvent> AMBIENT_PANDORA_FOREST =
      registerSoundEvent("ambient.pandora.forest");
  public static final RegistryObject<SoundEvent> AMBIENT_PANDORA_CAVES =
      registerSoundEvent("ambient.pandora.caves");
  public static final RegistryObject<SoundEvent> AMBIENT_PANDORA_WIND =
      registerSoundEvent("ambient.pandora.wind");
  public static final RegistryObject<SoundEvent> AMBIENT_PANDORA_BIOLUME =
      registerSoundEvent("ambient.pandora.biolume");

  // Pandora Hazards
  public static final RegistryObject<SoundEvent> HAZARD_SPORE_CLOUD =
      registerSoundEvent("hazard.spore_cloud");
  public static final RegistryObject<SoundEvent> HAZARD_LEVITATION_FIELD =
      registerSoundEvent("hazard.levitation_field");
  public static final RegistryObject<SoundEvent> HAZARD_HEAT_HAZE =
      registerSoundEvent("hazard.heat_haze");

  // Pandora Creature Sounds
  public static final RegistryObject<SoundEvent> ENTITY_SPORE_TYRANT_AMBIENT =
      registerSoundEvent("entity.spore_tyrant.ambient");
  public static final RegistryObject<SoundEvent> ENTITY_SPORE_TYRANT_HURT =
      registerSoundEvent("entity.spore_tyrant.hurt");
  public static final RegistryObject<SoundEvent> ENTITY_SPORE_TYRANT_DEATH =
      registerSoundEvent("entity.spore_tyrant.death");
  public static final RegistryObject<SoundEvent> ENTITY_SPORE_TYRANT_SPORE_BURST =
      registerSoundEvent("entity.spore_tyrant.spore_burst");

  private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
    return SOUND_EVENTS.register(
        name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("chex", name)));
  }

  public static void register(IEventBus eventBus) {
    SOUND_EVENTS.register(eventBus);
  }
}
