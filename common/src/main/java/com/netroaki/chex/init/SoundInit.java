package com.netroaki.chex.init;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CosmicHorizonsExpanded.MOD_ID);

  // Sand Emperor Sounds
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_AMBIENT =
      registerSoundEvent("entity.sand_emperor.ambient");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_HURT =
      registerSoundEvent("entity.sand_emperor.hurt");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_DEATH =
      registerSoundEvent("entity.sand_emperor.death");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_BURROW =
      registerSoundEvent("entity.sand_emperor.burrow");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_EMERGE =
      registerSoundEvent("entity.sand_emperor.emerge");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_ROAR =
      registerSoundEvent("entity.sand_emperor.roar");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_PHASE_CHANGE =
      registerSoundEvent("entity.sand_emperor.phase_change");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_SAND_BLAST =
      registerSoundEvent("entity.sand_emperor.sand_blast");
  public static final RegistryObject<SoundEvent> SAND_EMPEROR_TAIL_WHIP =
      registerSoundEvent("entity.sand_emperor.tail_whip");

  // Sound Event Registration Helper
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
