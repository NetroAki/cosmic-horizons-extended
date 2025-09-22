package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CHEX.MOD_ID);

    // Ambient Sounds
    public static final RegistryObject<SoundEvent> AMBIENT_FOREST_LOOP = 
            registerSoundEvent("ambient.pandora.forest_loop");
    public static final RegistryObject<SoundEvent> AMBIENT_WIND_HOWL = 
            registerSoundEvent("ambient.pandora.wind_howl");
    public static final RegistryObject<SoundEvent> AMBIENT_CRYSTAL_HUM = 
            registerSoundEvent("ambient.pandora.crystal_hum");
    public static final RegistryObject<SoundEvent> AMBIENT_VOLCANIC_RUMBLE = 
            registerSoundEvent("ambient.pandora.volcanic_rumble");
    
    // Hazard Sounds
    public static final RegistryObject<SoundEvent> HAZARD_LEVITATION = 
            registerSoundEvent("hazard.pandora.levitation");
    public static final RegistryObject<SoundEvent> HAZARD_SPORE_CLOUD = 
            registerSoundEvent("hazard.pandora.spore_cloud");
    public static final RegistryObject<SoundEvent> HAZARD_HEAT_WAVE = 
            registerSoundEvent("hazard.pandora.heat_wave");
    
    // Weather Sounds
    public static final RegistryObject<SoundEvent> WEATHER_ACID_RAIN = 
            registerSoundEvent("weather.pandora.acid_rain");
    public static final RegistryObject<SoundEvent> WEATHER_ELECTRIC_STORM = 
            registerSoundEvent("weather.pandora.electric_storm");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> 
            SoundEvent.createVariableRangeEvent(new ResourceLocation(CHEX.MOD_ID, name))
        );
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
