package com.netroaki.chex.client.sound;

import com.netroaki.chex.registry.CHEXSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class PandoraSoundController {
    private static final Map<SoundEvent, AbstractSoundInstance> playingSounds = new HashMap<>();
    private static int tickCounter = 0;
    private static final int SOUND_UPDATE_INTERVAL = 20;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;
        
        // Only update sounds in Pandora dimension
        if (!player.level().dimension().location().getPath().equals("pandora")) {
            stopAllSounds();
            return;
        }
        
        // Update sounds periodically
        tickCounter++;
        if (tickCounter >= SOUND_UPDATE_INTERVAL) {
            tickCounter = 0;
            updateAmbientSounds(player);
        }
    }
    
    private static void updateAmbientSounds(Player player) {
        Level level = player.level();
        Biome biome = level.getBiome(player.blockPosition()).value();
        
        // Stop sounds that shouldn't be playing
        playingSounds.entrySet().removeIf(entry -> {
            if (shouldStopSound(entry.getKey(), biome)) {
                Minecraft.getInstance().getSoundManager().stop(entry.getValue());
                return true;
            }
            return false;
        });
        
        // Play new sounds if needed
        if (biome.is(CHEXBiomes.Tags.BIOLUMINESCENT_FOREST)) {
            playAmbientSound(CHEXSoundEvents.AMBIENT_FOREST_LOOP.get(), 0.5f, 1.0f, player);
        } else if (biome.is(CHEXBiomes.Tags.FLOATING_ISLANDS)) {
            playAmbientSound(CHEXSoundEvents.AMBIENT_WIND_HOWL.get(), 0.4f, 1.0f, player);
            playAmbientSound(CHEXSoundEvents.AMBIENT_CRYSTAL_HUM.get(), 0.3f, 1.0f, player);
        } else if (biome.is(CHEXBiomes.Tags.VOLCANIC_WASTES)) {
            playAmbientSound(CHEXSoundEvents.AMBIENT_VOLCANIC_RUMBLE.get(), 0.6f, 1.0f, player);
        }
    }
    
    private static boolean shouldStopSound(SoundEvent sound, Biome biome) {
        if (sound == CHEXSoundEvents.AMBIENT_FOREST_LOOP.get()) {
            return !biome.is(CHEXBiomes.Tags.BIOLUMINESCENT_FOREST);
        } else if (sound == CHEXSoundEvents.AMBIENT_WIND_HOWL.get() || 
                  sound == CHEXSoundEvents.AMBIENT_CRYSTAL_HUM.get()) {
            return !biome.is(CHEXBiomes.Tags.FLOATING_ISLANDS);
        } else if (sound == CHEXSoundEvents.AMBIENT_VOLCANIC_RUMBLE.get()) {
            return !biome.is(CHEXBiomes.Tags.VOLCANIC_WASTES);
        }
        return true;
    }
    
    private static void playAmbientSound(SoundEvent sound, float volume, float pitch, Player player) {
        if (!isSoundPlaying(sound)) {
            AmbientSoundInstance soundInstance = new AmbientSoundInstance(
                sound, 
                SoundSource.AMBIENT, 
                volume, 
                pitch, 
                player
            );
            Minecraft.getInstance().getSoundManager().play(soundInstance);
            playingSounds.put(sound, soundInstance);
        }
    }
    
    private static boolean isSoundPlaying(SoundEvent sound) {
        return playingSounds.containsKey(sound) && 
               Minecraft.getInstance().getSoundManager().isActive(playingSounds.get(sound));
    }
    
    public static void stopAllSounds() {
        playingSounds.values().forEach(sound -> 
            Minecraft.getInstance().getSoundManager().stop(sound)
        );
        playingSounds.clear();
    }
    
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        // Lower the volume of certain vanilla sounds in Pandora
        if (Minecraft.getInstance().player != null && 
            Minecraft.getInstance().player.level().dimension().location().getPath().equals("pandora") &&
            event.getSound() != null) {
            
            SoundInstance sound = event.getSound();
            if (sound.getSource() == SoundSource.AMBIENT || 
                sound.getSource() == SoundSource.WEATHER) {
                event.setSound(new ModifiedSoundInstance(sound, sound.getVolume() * 0.7f));
            }
        }
    }
    
    // Custom sound instance for ambient sounds
    private static class AmbientSoundInstance extends AbstractSoundInstance {
        private final Player player;
        
        public AmbientSoundInstance(SoundEvent sound, SoundSource source, float volume, float pitch, Player player) {
            super(sound, source, SoundInstance.createUnseededRandom());
            this.player = player;
            this.volume = volume;
            this.pitch = pitch;
            this.looping = true;
            this.delay = 0;
            this.relative = true;
        }
        
        @Override
        public void tick() {
            if (this.player.isRemoved()) {
                this.stop();
                return;
            }
            
            // Update position to follow player
            this.x = (float) this.player.getX();
            this.y = (float) this.player.getY();
            this.z = (float) this.player.getZ();
            
            // Fade in/out based on biome
            Biome biome = player.level().getBiome(player.blockPosition()).value();
            if (shouldStopSound(this.getSound(), biome)) {
                this.volume = Math.max(0, this.volume - 0.01f);
                if (this.volume <= 0.01f) {
                    this.stop();
                }
            } else {
                this.volume = Math.min(this.volume + 0.01f, 1.0f);
            }
        }
    }
    
    // Wrapper to modify sound properties
    private record ModifiedSoundInstance(SoundInstance wrapped, float volume) implements SoundInstance {
        @Override public SoundEvent getLocation() { return wrapped.getLocation(); }
        @Override public SoundSource getSource() { return wrapped.getSource(); }
        @Override public boolean isLooping() { return wrapped.isLooping(); }
        @Override public boolean isRelative() { return wrapped.isRelative(); }
        @Override public float getPitch() { return wrapped.getPitch(); }
        @Override public float getVolume() { return volume; }
        @Override public double getX() { return wrapped.getX(); }
        @Override public double getY() { return wrapped.getY(); }
        @Override public double getZ() { return wrapped.getZ(); }
        @Override public Attenuation getAttenuation() { return wrapped.getAttenuation(); }
    }
}
