package com.netroaki.chex.client.sound;

import com.netroaki.chex.sound.CHEXSoundEvents;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class DesertAmbienceHandler {
  private static final Random RANDOM = new Random();
  private static int ambientSoundDelay = 0;
  private static boolean isPlayingAmbient = false;
  private static SoundInstance currentAmbientSound = null;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null) return;

    // Only play in desert biomes
    if (!isInDesertBiome(player.level(), player.blockPosition())) {
      stopAmbientSounds();
      return;
    }

    // Handle ambient sound timing
    if (ambientSoundDelay > 0) {
      ambientSoundDelay--;
    } else if (!isPlayingAmbient) {
      playRandomAmbientSound(mc, player);
    }

    // Stop ambient sounds if a music disc is playing
    if (mc.getSoundManager()
        .isActive(
            sound ->
                sound.getSource() == SoundSource.MUSIC
                    || sound.getSource() == SoundSource.RECORDS)) {
      stopAmbientSounds();
    }
  }

  private static boolean isInDesertBiome(Level level, net.minecraft.core.BlockPos pos) {
    Biome biome = level.getBiome(pos).value();
    return biome.is(Biomes.DESERT)
        || biome.getRegistryName() != null
            && biome.getRegistryName().getNamespace().equals("chex")
            && biome.getRegistryName().getPath().contains("desert");
  }

  private static void playRandomAmbientSound(Minecraft mc, Player player) {
    if (mc.getSoundManager().isActive(sound -> sound.getSource() == SoundSource.AMBIENT)) {
      return; // Don't play multiple ambient sounds
    }

    SoundEvent soundEvent = getRandomAmbientSound(player);
    if (soundEvent != null) {
      float volume = 0.5f + RANDOM.nextFloat() * 0.5f;
      float pitch = 0.8f + RANDOM.nextFloat() * 0.4f;

      currentAmbientSound = SimpleSoundInstance.forAmbientAddition(soundEvent);
      mc.getSoundManager().play(currentAmbientSound);

      // Set delay until next ambient sound (1-3 minutes)
      ambientSoundDelay = 1200 + RANDOM.nextInt(2400);
      isPlayingAmbient = true;
    }
  }

  private static SoundEvent getRandomAmbientSound(Player player) {
    boolean isNight = !player.level().isDay();

    if (isNight) {
      return RANDOM.nextFloat() < 0.7f
          ? CHEXSoundEvents.AMBIENT_DESERT_NIGHT.get()
          : CHEXSoundEvents.AMBIENT_DESERT_WIND.get();
    } else {
      return RANDOM.nextFloat() < 0.7f
          ? CHEXSoundEvents.AMBIENT_DESERT_DAY.get()
          : CHEXSoundEvents.AMBIENT_DESERT_WIND.get();
    }
  }

  private static void stopAmbientSounds() {
    if (currentAmbientSound != null) {
      Minecraft.getInstance().getSoundManager().stop(currentAmbientSound);
      currentAmbientSound = null;
    }
    isPlayingAmbient = false;
  }

  @SubscribeEvent
  public static void onPlaySound(PlaySoundEvent event) {
    // Lower the volume of vanilla desert sounds to avoid conflicts
    if (event.getSound() != null
        && (event.getSound().getLocation().getPath().contains("desert")
            || event.getSound().getLocation().getPath().contains("wind"))) {
      event.setSound(event.getSound().attenuationDistance(16));
    }
  }
}
