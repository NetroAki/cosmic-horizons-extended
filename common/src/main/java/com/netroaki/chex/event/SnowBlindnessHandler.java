package com.netroaki.chex.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SnowBlindnessHandler {
  // TODO: Fix when BiomeTags.create is available
  // private static final TagKey<Biome> COLD_BIOMES =
  // BiomeTags.create("forge:is_cold");
  private static final TagKey<Biome> COLD_BIOMES = null; // Placeholder
  private static final int CHECK_INTERVAL = 20; // once per second

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) return;
    Player player = event.player;
    Level level = player.level();
    if (level.isClientSide) return;
    if (level.getGameTime() % CHECK_INTERVAL != 0) return;

    // Only in cold biomes
    if (COLD_BIOMES == null || !level.getBiome(player.blockPosition()).is(COLD_BIOMES)) return;

    // TODO: Add biome precipitation check when API is available
    boolean blizzard = level.isRaining();
    if (!blizzard) return;

    // Apply short-duration snow blindness using vanilla blindness effect; refreshes while
    // conditions hold
    int amplifier = level.canSeeSky(player.blockPosition()) ? 1 : 0;
    player.addEffect(
        new MobEffectInstance(
            MobEffects.BLINDNESS,
            60, // 3 seconds
            amplifier,
            false,
            true));
  }
}
