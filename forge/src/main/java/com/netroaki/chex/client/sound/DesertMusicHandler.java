package com.netroaki.chex.client.sound;

import com.netroaki.chex.sound.CHEXSoundEvents;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class DesertMusicHandler {
  private static final Random RANDOM = new Random();
  private static int musicDelay = 0;
  private static boolean isPlayingMusic = false;
  private static SoundInstance currentMusic = null;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;
    ClientLevel level = mc.level;

    if (player == null || level == null) {
      stopMusic();
      return;
    }

    // Only play in desert biomes
    if (!isInDesertBiome(level, player.blockPosition())) {
      stopMusic();
      return;
    }

    // Handle music timing
    if (musicDelay > 0) {
      musicDelay--;
      return;
    }

    // Don't play if there's already music playing
    if (isPlayingMusic
        || mc.getMusicManager().isPlayingMusic()
        || mc.getSoundManager()
            .isActive(
                sound ->
                    sound.getSource() == SoundSource.MUSIC
                        || sound.getSource() == SoundSource.RECORDS)) {
      return;
    }

    // Check for nearby hostile mobs
    boolean isInDanger = isPlayerInDanger(player, level);

    // Play appropriate music
    playMusic(mc, isInDanger);
  }

  private static boolean isInDesertBiome(ClientLevel level, net.minecraft.core.BlockPos pos) {
    Biome biome = level.getBiome(pos).value();
    return biome.is(Biomes.DESERT)
        || biome.getRegistryName() != null
            && biome.getRegistryName().getNamespace().equals("chex")
            && biome.getRegistryName().getPath().contains("desert");
  }

  private static boolean isPlayerInDanger(LocalPlayer player, ClientLevel level) {
    if (player.getHealth() < player.getMaxHealth() * 0.3) {
      return true;
    }

    // Check for nearby hostile mobs
    AABB searchArea = player.getBoundingBox().inflate(16.0, 8.0, 16.0);
    List<Monster> nearbyHostiles =
        level.getEntitiesOfClass(
            Monster.class,
            searchArea,
            entity -> entity.getTarget() == player || entity.getLastHurtMob() == player);

    return !nearbyHostiles.isEmpty();
  }

  private static void playMusic(Minecraft mc, boolean isInDanger) {
    SoundEvent music;

    if (isInDanger) {
      music = CHEXSoundEvents.MUSIC_DESERT_DANGER.get();
    } else if (mc.level.isDay()) {
      music = CHEXSoundEvents.MUSIC_DESERT_DAY.get();
    } else {
      music = CHEXSoundEvents.MUSIC_DESERT_NIGHT.get();
    }

    // Play the music
    currentMusic = SimpleSoundInstance.forMusic(music);
    mc.getSoundManager().play(currentMusic);
    isPlayingMusic = true;

    // Set delay until next music (5-10 minutes)
    musicDelay = 6000 + RANDOM.nextInt(6000);
  }

  private static void stopMusic() {
    if (isPlayingMusic && currentMusic != null) {
      Minecraft.getInstance().getSoundManager().stop(currentMusic);
      currentMusic = null;
      isPlayingMusic = false;
    }
  }
}
