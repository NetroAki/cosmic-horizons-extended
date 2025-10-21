package com.netroaki.chex.client.music;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.sound.CHEXSoundEvents;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, value = Dist.CLIENT)
public class MusicManager {
  private static final Random RANDOM = new Random();
  private static final int MIN_DELAY = 6000; // 5 minutes
  private static final int MAX_DELAY = 12000; // 10 minutes

  private static int musicDelay = 0;
  private static SoundInstance currentMusic = null;
  private static MusicType currentMusicType = null;

  // Biome to music type mapping
  private static final Map<ResourceKey<Biome>, MusicType> BIOME_MUSIC = new HashMap<>();

  // Music types for different biomes and situations
  public enum MusicType {
    DESERT_DAY(CHEXSoundEvents.MUSIC_DESERT_DAY.get(), 100, 300),
    DESERT_NIGHT(CHEXSoundEvents.MUSIC_DESERT_NIGHT.get(), 100, 300),
    DESERT_DANGER(CHEXSoundEvents.MUSIC_DESERT_DANGER.get(), 20, 60),
    SANDSTORM(CHEXSoundEvents.WEATHER_SANDSTORM_LOOP.get(), 600, 1200);

    public final SoundEvent soundEvent;
    public final int minDelay;
    public final int maxDelay;

    MusicType(SoundEvent soundEvent, int minDelay, int maxDelay) {
      this.soundEvent = soundEvent;
      this.minDelay = minDelay;
      this.maxDelay = maxDelay;
    }
  }

  static {
    // Register biome music types
    BIOME_MUSIC.put(Biomes.DESERT, MusicType.DESERT_DAY);
    // Add more biome mappings as needed
  }

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null || mc.level == null) {
      stopMusic();
      return;
    }

    // Only handle music in-game
    if (mc.screen != null || mc.getConnection() == null) {
      return;
    }

    // Handle music timing
    if (musicDelay > 0) {
      musicDelay--;
      return;
    }

    // Don't interrupt other music
    if (mc.getSoundManager()
        .isActive(
            sound ->
                sound.getSource() == SoundSource.MUSIC
                    || sound.getSource() == SoundSource.RECORDS)) {
      return;
    }

    // Determine the appropriate music type
    MusicType newMusicType = determineMusicType(player, mc.level);

    // Play the music if it's different from current or if no music is playing
    if (newMusicType != currentMusicType
        || currentMusic == null
        || !mc.getSoundManager().isActive(currentMusic)) {
      playMusic(mc, newMusicType);
    }
  }

  private static MusicType determineMusicType(Player player, Level level) {
    // Check for danger first (highest priority)
    if (isPlayerInDanger(player, level)) {
      return MusicType.DESERT_DANGER;
    }

    // Check for sandstorm (second priority)
    if (isInSandstorm(player, level)) {
      return MusicType.SANDSTORM;
    }

    // Default to time-based music
    return level.isDay() ? MusicType.DESERT_DAY : MusicType.DESERT_NIGHT;
  }

  private static boolean isPlayerInDanger(Player player, Level level) {
    if (player.getHealth() < player.getMaxHealth() * 0.3) {
      return true;
    }

    // Check for nearby hostile mobs
    return !player
        .level()
        .getEntitiesOfClass(
            net.minecraft.world.entity.monster.Monster.class,
            player.getBoundingBox().inflate(16.0),
            e -> e.getTarget() == player || e.getLastHurtMob() == player)
        .isEmpty();
  }

  private static boolean isInSandstorm(Player player, Level level) {
    // TODO: Implement sandstorm detection
    return false;
  }

  private static void playMusic(Minecraft mc, MusicType musicType) {
    stopMusic();

    currentMusicType = musicType;
    currentMusic = SimpleSoundInstance.forMusic(musicType.soundEvent);

    // Set volume and pitch based on music type
    float volume = 1.0f;
    float pitch = 1.0f;

    // Apply some randomization to make it feel more natural
    pitch += (RANDOM.nextFloat() - 0.5f) * 0.1f;

    mc.getSoundManager()
        .play(
            new SimpleSoundInstance(
                musicType.soundEvent,
                SoundSource.MUSIC,
                volume,
                pitch,
                SoundInstance.createUnseededRandom(),
                false,
                0,
                SoundInstance.Attenuation.NONE,
                0.0,
                0.0,
                0.0,
                true));

    // Set delay until next music (converted from seconds to ticks)
    musicDelay = RANDOM.nextInt(musicType.maxDelay - musicType.minDelay) + musicType.minDelay;
  }

  public static void stopMusic() {
    if (currentMusic != null) {
      Minecraft.getInstance().getSoundManager().stop(currentMusic);
      currentMusic = null;
      currentMusicType = null;
    }
  }

  public static boolean isMusicPlaying() {
    return currentMusic != null && Minecraft.getInstance().getSoundManager().isActive(currentMusic);
  }
}
