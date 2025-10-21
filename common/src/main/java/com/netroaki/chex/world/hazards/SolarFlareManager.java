package com.netroaki.chex.world.hazards;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CosmicHorizonsExpanded.MOD_ID)
public class SolarFlareManager {
  private static final Map<Integer, FlareState> DIMENSION_STATES = new HashMap<>();
  private static final Random RANDOM = new Random();
  private static final int WARNING_TIME = 200; // 10 seconds warning
  private static final int FLARE_DURATION = 400; // 20 seconds duration
  private static final int MIN_FLARE_INTERVAL = 12000; // 10 minutes minimum between flares
  private static final int MAX_FLARE_INTERVAL = 24000; // 20 minutes maximum between flares

  private static class FlareState {
    int timer;
    int warningTimer;
    boolean isFlaring;
    int nextFlare;

    FlareState() {
      resetTimer();
    }

    void resetTimer() {
      this.timer = 0;
      this.warningTimer = 0;
      this.isFlaring = false;
      this.nextFlare = RANDOM.nextInt(MAX_FLARE_INTERVAL - MIN_FLARE_INTERVAL) + MIN_FLARE_INTERVAL;
    }
  }

  @SubscribeEvent
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    if (!(event.level instanceof ServerLevel level)) return;
    if (!level.dimension().location().getPath().equals("alpha_centauri_a")) return;

    FlareState state =
        DIMENSION_STATES.computeIfAbsent(
            level.dimension().location().hashCode(), k -> new FlareState());
    state.timer++;

    if (!state.isFlaring) {
      if (state.timer >= state.nextFlare) {
        startFlareWarning(level);
        state.warningTimer = WARNING_TIME;
        state.isFlaring = true;
        state.timer = 0;
      }
    } else if (state.warningTimer > 0) {
      state.warningTimer--;
      if (state.warningTimer <= 0) {
        startFlare(level);
      }
    } else if (state.timer >= FLARE_DURATION) {
      endFlare(level);
      state.resetTimer();
    }
  }

  private static void startFlareWarning(ServerLevel level) {
    // Play warning sound to all players in the dimension
    for (ServerPlayer player : level.players()) {
      if (player.level().dimension().equals(level.dimension())) {
        player.displayClientMessage(
            Component.literal("WARNING: Solar flare detected! Seek shelter immediately!")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
            true);
        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.WITHER_SPAWN,
            SoundSource.AMBIENT,
            1.0f,
            0.5f);
      }
    }
  }

  private static void startFlare(ServerLevel level) {
    // Apply radiation effect to all players in the dimension
    for (ServerPlayer player : level.players()) {
      if (player.level().dimension().equals(level.dimension())) {
        player.displayClientMessage(
            Component.literal("SOLAR FLARE IN PROGRESS! Take cover!")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD, ChatFormatting.UNDERLINE),
            true);
        player.addEffect(
            new MobEffectInstance(
                ModEffects.RADIATION_EFFECT.get(),
                FLARE_DURATION,
                2, // Increased level during flare
                false,
                true,
                true));
      }
    }
  }

  private static void endFlare(ServerLevel level) {
    // Notify players the flare has ended
    for (ServerPlayer player : level.players()) {
      if (player.level().dimension().equals(level.dimension())) {
        player.displayClientMessage(
            Component.literal("Solar flare has subsided. It is now safe to continue.")
                .withStyle(ChatFormatting.GREEN),
            true);
      }
    }
  }

  public static boolean isFlareActive(Level level) {
    FlareState state = DIMENSION_STATES.get(level.dimension().location().hashCode());
    return state != null && state.isFlaring && state.warningTimer <= 0;
  }
}
