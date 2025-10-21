package com.netroaki.chex.client.sound;

import com.netroaki.chex.CosmicHorizonsExpanded;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AquaMundusSounds {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CosmicHorizonsExpanded.MOD_ID);

  // Ambient Sounds
  public static final RegistryObject<SoundEvent> AMBIENT_UNDERWATER_LOOP =
      registerSoundEvent("ambient.aqua_mundus.underwater_loop");
  public static final RegistryObject<SoundEvent> AMBIENT_CAVERNS_LOOP =
      registerSoundEvent("ambient.aqua_mundus.caverns_loop");

  // Entity Sounds
  public static final RegistryObject<SoundEvent> LUMINFISH_AMBIENT =
      registerSoundEvent("entity.luminfish.ambient");
  public static final RegistryObject<SoundEvent> LUMINFISH_HURT =
      registerSoundEvent("entity.luminfish.hurt");

  public static final RegistryObject<SoundEvent> HYDROTHERMAL_DRONE_AMBIENT =
      registerSoundEvent("entity.hydrothermal_drone.ambient");
  public static final RegistryObject<SoundEvent> HYDROTHERMAL_DRONE_HURT =
      registerSoundEvent("entity.hydrothermal_drone.hurt");

  // Boss Sounds
  public static final RegistryObject<SoundEvent> OCEAN_SOVEREIGN_AMBIENT =
      registerSoundEvent("entity.ocean_sovereign.ambient");
  public static final RegistryObject<SoundEvent> OCEAN_SOVEREIGN_HURT =
      registerSoundEvent("entity.ocean_sovereign.hurt");
  public static final RegistryObject<SoundEvent> OCEAN_SOVEREIGN_SONIC_BOOM =
      registerSoundEvent("entity.ocean_sovereign.sonic_boom");
  public static final RegistryObject<SoundEvent> OCEAN_SOVEREIGN_WHIRLPOOL =
      registerSoundEvent("entity.ocean_sovereign.whirlpool");

  // Environment Sounds
  public static final RegistryObject<SoundEvent> BUBBLE_COLUMN =
      registerSoundEvent("block.bubble_column.ambient");
  public static final RegistryObject<SoundEvent> WHIRLPOOL_LOOP =
      registerSoundEvent("block.whirlpool.loop");

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
