package com.netroaki.chex.client.sound;

import com.netroaki.chex.CosmicHorizonsExpanded;
import com.netroaki.chex.sound.ArrakisSounds;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID, value = Dist.CLIENT)
public class ArrakisAmbientSoundManager {
  private static final Random RANDOM = new Random();
  private static final int SOUND_UPDATE_INTERVAL = 100; // Update sound every 5 seconds (in ticks)

  private static int tickCounter = 0;
  private static float currentWindIntensity = 0.0f;
  private static float targetWindIntensity = 0.0f;
  private static float sandstormIntensity = 0.0f;
  private static float targetSandstormIntensity = 0.0f;

  // Active sound instances
  private static final Map<String, SoundInstance> activeSounds = new HashMap<>();

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;

    if (player == null || minecraft.level == null) {
      stopAllSounds();
      return;
    }

    // Only handle sounds in Arrakis dimension
    if (!player.level().dimension().location().getPath().equals("arrakis")) {
      stopAllSounds();
      return;
    }

    tickCounter++;

    // Update intensities at a lower frequency
    if (tickCounter % SOUND_UPDATE_INTERVAL == 0) {
      updateSoundIntensities(player, player.level());
    }

    // Smoothly transition between intensities
    currentWindIntensity = Mth.lerp(0.02f, currentWindIntensity, targetWindIntensity);
    sandstormIntensity = Mth.lerp(0.01f, sandstormIntensity, targetSandstormIntensity);

    // Update or play sounds based on current intensities
    updateWindSounds(player);
    updateSandstormSounds(player);

    // Clean up finished sounds
    activeSounds.values().removeIf(sound -> !minecraft.getSoundManager().isActive(sound));
  }

  private static void updateSoundIntensities(Player player, Level level) {
    // Base wind intensity based on height and weather
    float baseWind = 0.3f;

    // Increase wind at higher altitudes
    float heightFactor = (float) (player.getY() - level.getSeaLevel()) / 128.0f;
    baseWind += Mth.clamp(heightFactor * 0.2f, 0.0f, 0.4f);

    // Increase wind during rain (sandstorm)
    if (level.isRaining()) {
      baseWind += 0.4f;
    }

    // Add some randomness
    baseWind += (RANDOM.nextFloat() - 0.5f) * 0.1f;

    // Clamp and set target intensity
    targetWindIntensity = Mth.clamp(baseWind, 0.0f, 1.0f);

    // Sandstorm intensity (based on rain/weather)
    targetSandstormIntensity = level.isRaining() ? 1.0f : 0.0f;
  }

  private static void updateWindSounds(Player player) {
    Minecraft minecraft = Minecraft.getInstance();

    // Determine which wind sound to play based on intensity
    SoundEvent windSound = null;
    float volume = 0.0f;
    float pitch = 1.0f;

    if (currentWindIntensity > 0.6f) {
      windSound = ArrakisSounds.WIND_STRONG.get();
      volume = currentWindIntensity * 0.8f;
      pitch = 0.9f + (currentWindIntensity - 0.6f) * 0.5f;
    } else if (currentWindIntensity > 0.3f) {
      windSound = ArrakisSounds.WIND_MEDIUM.get();
      volume = (currentWindIntensity - 0.3f) * 2.5f;
      pitch = 0.95f + (currentWindIntensity - 0.3f) * 0.5f;
    } else if (currentWindIntensity > 0.1f) {
      windSound = ArrakisSounds.WIND_LIGHT.get();
      volume = currentWindIntensity * 3.0f;
      pitch = 1.0f;
    }

    // Play or update wind sound
    if (windSound != null) {
      playLoopingSound("wind", windSound, volume, pitch, player);
    } else {
      stopSound("wind");
    }
  }

  private static void updateSandstormSounds(Player player) {
    if (sandstormIntensity > 0.1f) {
      // Play sandstorm loop
      float volume = sandstormIntensity * 0.7f;
      float pitch = 0.9f + sandstormIntensity * 0.2f;
      playLoopingSound("sandstorm_loop", ArrakisSounds.SANDSTORM_LOOP.get(), volume, pitch, player);

      // Play occasional sand drift sounds
      if (RANDOM.nextFloat() < sandstormIntensity * 0.01f) {
        float driftVolume = 0.3f + RANDOM.nextFloat() * 0.5f * sandstormIntensity;
        float driftPitch = 0.8f + RANDOM.nextFloat() * 0.4f;
        player
            .level()
            .playLocalSound(
                player.getX(),
                player.getY(),
                player.getZ(),
                ArrakisSounds.SAND_DRIFT.get(),
                SoundSource.AMBIENT,
                driftVolume * 2.0f,
                driftPitch,
                false);
      }
    } else {
      stopSound("sandstorm_loop");
    }
  }

  private static void playLoopingSound(
      String id, SoundEvent sound, float volume, float pitch, Player player) {
    Minecraft minecraft = Minecraft.getInstance();

    if (!activeSounds.containsKey(id)
        || !minecraft.getSoundManager().isActive(activeSounds.get(id))) {
      LoopingSoundInstance soundInstance =
          new LoopingSoundInstance(sound, SoundSource.AMBIENT, volume, pitch, player);
      minecraft.getSoundManager().play(soundInstance);
      activeSounds.put(id, soundInstance);
    } else if (activeSounds.get(id) instanceof LoopingSoundInstance loopingSound) {
      loopingSound.update(volume, pitch);
    }
  }

  private static void stopSound(String id) {
    Minecraft minecraft = Minecraft.getInstance();
    SoundInstance sound = activeSounds.remove(id);
    if (sound != null) {
      minecraft.getSoundManager().stop(sound);
    }
  }

  private static void stopAllSounds() {
    Minecraft minecraft = Minecraft.getInstance();
    for (SoundInstance sound : activeSounds.values()) {
      minecraft.getSoundManager().stop(sound);
    }
    activeSounds.clear();
  }

  @SubscribeEvent
  public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
    stopAllSounds();
  }

  // Custom sound instance for looping sounds with volume control
  private static class LoopingSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private float targetVolume;
    private float targetPitch;

    public LoopingSoundInstance(
        SoundEvent sound, SoundSource source, float volume, float pitch, Player player) {
      super(sound, source, SoundInstance.createUnseededRandom());
      this.player = player;
      this.volume = 0.0f;
      this.pitch = pitch;
      this.looping = true;
      this.delay = 0;
      this.relative = false;
      this.targetVolume = volume;
      this.targetPitch = pitch;
      this.attenuation = SoundInstance.Attenuation.LINEAR;
    }

    public void update(float volume, float pitch) {
      this.targetVolume = volume;
      this.targetPitch = pitch;
    }

    @Override
    public void tick() {
      if (this.player.isRemoved()
          || !this.player.level().dimension().location().getPath().equals("arrakis")) {
        this.stop();
        return;
      }

      // Smoothly adjust volume and pitch
      this.volume = Mth.lerp(0.1f, this.volume, this.targetVolume);
      this.pitch = Mth.lerp(0.05f, this.pitch, this.targetPitch);

      // Update position to follow player
      this.x = (float) this.player.getX();
      this.y = (float) this.player.getY();
      this.z = (float) this.player.getZ();

      // Stop if volume is very low
      if (this.volume < 0.01f) {
        this.volume = 0.0f;
        this.stop();
      }
    }
  }
}
