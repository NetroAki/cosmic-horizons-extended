package com.netroaki.chex.registry.sounds;

import com.netroaki.chex.CHEX;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** Registry for CHEX custom sounds. Provides placeholders for hazard and ambient audio hooks. */
public class CHEXSounds {

  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CHEX.MOD_ID);

  // Hazard sounds
  public static final RegistryObject<SoundEvent> FROSTBITE_WARNING =
      SOUND_EVENTS.register(
          "frostbite_warning",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("frostbite_warning")));

  public static final RegistryObject<SoundEvent> SNOW_BLINDNESS_WARNING =
      SOUND_EVENTS.register(
          "snow_blindness_warning",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("snow_blindness_warning")));

  public static final RegistryObject<SoundEvent> RADIATION_WARNING =
      SOUND_EVENTS.register(
          "radiation_warning",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("radiation_warning")));

  public static final RegistryObject<SoundEvent> PRESSURE_WARNING =
      SOUND_EVENTS.register(
          "pressure_warning",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("pressure_warning")));

  public static final RegistryObject<SoundEvent> HEAT_WARNING =
      SOUND_EVENTS.register(
          "heat_warning", () -> SoundEvent.createVariableRangeEvent(CHEX.id("heat_warning")));

  // Ambient sounds
  public static final RegistryObject<SoundEvent> PANDORA_AMBIENT =
      SOUND_EVENTS.register(
          "pandora_ambient", () -> SoundEvent.createVariableRangeEvent(CHEX.id("pandora_ambient")));

  public static final RegistryObject<SoundEvent> ARRAKIS_SANDSTORM =
      SOUND_EVENTS.register(
          "arrakis_sandstorm",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("arrakis_sandstorm")));

  public static final RegistryObject<SoundEvent> CRYSTALIS_CRYSTAL =
      SOUND_EVENTS.register(
          "crystalis_crystal",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("crystalis_crystal")));

  // Entity sounds
  public static final RegistryObject<SoundEvent> SPOREFLIES_AMBIENT =
      SOUND_EVENTS.register(
          "sporeflies_ambient",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("sporeflies_ambient")));

  public static final RegistryObject<SoundEvent> SPORE_TYRANT_ROAR =
      SOUND_EVENTS.register(
          "spore_tyrant_roar",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("spore_tyrant_roar")));

  public static final RegistryObject<SoundEvent> WORLDHEART_AVATAR_HUM =
      SOUND_EVENTS.register(
          "worldheart_avatar_hum",
          () -> SoundEvent.createVariableRangeEvent(CHEX.id("worldheart_avatar_hum")));
}
