package com.netroaki.chex.client.sound;

import com.netroaki.chex.world.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class AquaMundusAmbientSoundHandler {
  private static final int AMBIENT_SOUND_DELAY = 600; // 30 seconds at 20 ticks per second
  private static int ambientSoundCooldown = 0;
  private static SoundInstance currentAmbientSound;

  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;

    if (player == null || mc.level == null) {
      stopCurrentSound();
      return;
    }

    // Only play in Aqua Mundus dimension
    if (!player.level.dimension().equals(ModDimensions.AQUA_MUNDUS_LEVEL_KEY)) {
      stopCurrentSound();
      return;
    }

    // Handle ambient sound timing
    if (ambientSoundCooldown > 0) {
      ambientSoundCooldown--;
    } else if (player.isUnderWater()) {
      playAmbientSound(player);
      ambientSoundCooldown = AMBIENT_SOUND_DELAY + player.getRandom().nextInt(600);
    }
  }

  private static void playAmbientSound(Player player) {
    stopCurrentSound();

    // Choose appropriate ambient sound based on biome/depth
    SoundEvent soundEvent;
    if (player.getY() < 30) {
      // Deep ocean/caverns
      soundEvent =
          player.getRandom().nextBoolean()
              ? AquaMundusSounds.AMBIENT_CAVERNS_LOOP.get()
              : AquaMundusSounds.AMBIENT_UNDERWATER_LOOP.get();
    } else {
      // Open ocean
      soundEvent = AquaMundusSounds.AMBIENT_UNDERWATER_LOOP.get();
    }

    // Play the sound
    currentAmbientSound =
        new SimpleSoundInstance(
            soundEvent.getLocation(),
            SoundSource.AMBIENT,
            0.5f + player.getRandom().nextFloat() * 0.5f, // Volume
            0.8f + player.getRandom().nextFloat() * 0.4f, // Pitch
            SoundInstance.createUnseededRandom(),
            false,
            0,
            SoundInstance.Attenuation.NONE,
            player.getX(),
            player.getY(),
            player.getZ(),
            false);

    Minecraft.getInstance().getSoundManager().play(currentAmbientSound);
  }

  private static void stopCurrentSound() {
    if (currentAmbientSound != null) {
      Minecraft.getInstance().getSoundManager().stop(currentAmbientSound);
      currentAmbientSound = null;
    }
  }

  @SubscribeEvent
  public static void onPlaySound(PlaySoundEvent event) {
    // Optional: Modify or filter sounds when underwater in Aqua Mundus
    if (event.getSound() != null && event.getSound().getSource() == SoundSource.AMBIENT) {
      // Example: Mute vanilla underwater ambience in our dimension
      if (event.getSound().getLocation().getNamespace().equals("minecraft")
          && event.getSound().getLocation().getPath().contains("underwater")) {
        event.setSound(null);
      }
    }
  }
}
