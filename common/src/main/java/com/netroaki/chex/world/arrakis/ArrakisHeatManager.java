package com.netroaki.chex.world.arrakis;

import com.netroaki.chex.suits.SuitTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArrakisHeatManager {
  private static final DamageSource HEAT_DAMAGE =
      new DamageSource("arrakis_heat").setScalesWithDifficulty();
  private static final int HEAT_CHECK_INTERVAL = 20; // Check every second
  private static final int THIRST_CHECK_INTERVAL = 100; // Check every 5 seconds

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
      return;
    }

    Player player = event.player;
    Level level = player.level();

    // Only apply in Arrakis dimension
    if (!level.dimension().location().getPath().equals("arrakis")) {
      return;
    }

    // Skip if player is in creative or spectator mode
    if (player.isCreative() || player.isSpectator()) {
      return;
    }

    // Check heat effects at regular intervals
    if (player.tickCount % HEAT_CHECK_INTERVAL == 0) {
      handleHeatEffects(player, level);
    }

    // Check thirst at longer intervals
    if (player.tickCount % THIRST_CHECK_INTERVAL == 0) {
      handleThirstMechanics(player, level);
    }
  }

  private static void handleHeatEffects(Player player, Level level) {
    BlockPos pos = player.blockPosition();
    Biome biome = level.getBiome(pos).value();
    float temperature = biome.getBaseTemperature();

    // Check if player is in direct sunlight
    boolean inSunlight = level.canSeeSky(pos) && level.isDay() && !level.isRaining();

    // Check if player is near heat sources (lava, fire, etc.)
    boolean nearHeatSource = isNearHeatSource(level, player);

    // Calculate heat level (0.0 - 1.0)
    float heatLevel = calculateHeatLevel(level, player, temperature, inSunlight, nearHeatSource);

    // Apply effects based on heat level
    if (heatLevel > 0.7f) {
      // Extreme heat - apply damage and slowness
      if (!isProtectedFromHeat(player)) {
        player.hurt(HEAT_DAMAGE, 1.0f);
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 1));

        // Play sizzle sound when taking heat damage
        if (player.tickCount % 40 == 0) {
          level.playSound(
              null,
              player.getX(),
              player.getY(),
              player.getZ(),
              SoundEvents.FIRE_EXTINGUISH,
              SoundSource.AMBIENT,
              0.5f,
              1.5f);
        }
      }
    } else if (heatLevel > 0.4f) {
      // High heat - apply mining fatigue
      if (!isProtectedFromHeat(player)) {
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0));
      }
    }
  }

  private static boolean isNearHeatSource(Level level, LivingEntity entity) {
    // Check blocks in a 3x3x3 area around the player
    AABB searchBox = entity.getBoundingBox().inflate(2.0);
    BlockPos.betweenClosedStream(searchBox)
        .anyMatch(
            pos -> {
              // Check for lava, fire, or other heat sources
              return level.getBlockState(pos).is(Blocks.LAVA)
                  || level.getBlockState(pos).is(Blocks.FIRE)
                  || level.getBlockState(pos).is(Blocks.CAMPFIRE)
                  || level.getBlockState(pos).is(Blocks.MAGMA_BLOCK);
            });
    return false;
  }

  private static float calculateHeatLevel(
      Level level, Player player, float baseTemp, boolean inSunlight, boolean nearHeatSource) {
    float heatLevel = baseTemp - 0.5f; // Normalize temperature (0.0-1.0)

    // Increase heat in direct sunlight
    if (inSunlight) {
      heatLevel += 0.3f;
    }

    // Increase heat near heat sources
    if (nearHeatSource) {
      heatLevel += 0.4f;
    }

    // Reduce heat when in water or rain
    if (player.isInWaterRainOrBubble() || level.isRainingAt(player.blockPosition())) {
      heatLevel -= 0.5f;
    }

    // Apply suit protection if wearing appropriate armor
    if (isProtectedFromHeat(player)) {
      heatLevel *= 0.3f; // Reduce heat effect with proper protection
    }

    return Math.max(0.0f, Math.min(1.0f, heatLevel));
  }

  private static boolean isProtectedFromHeat(Player player) {
    // Check if player is wearing appropriate protection (T3+ suit)
    return SuitTiers.getPlayerSuitTier(player).isAtLeast(SuitTiers.T3);
  }

  private static void handleThirstMechanics(Player player, Level level) {
    // Only apply thirst in hot biomes or when hot
    if (calculateHeatLevel(
            level,
            player,
            level.getBiome(player.blockPosition()).value().getBaseTemperature(),
            level.canSeeSky(player.blockPosition()) && level.isDay(),
            isNearHeatSource(level, player))
        > 0.4f) {

      // Increase thirst level (or decrease hydration)
      if (player instanceof ServerPlayer serverPlayer) {
        // TODO: Implement thirst system or use existing mechanics
        // For now, just play a sound when thirsty
        if (player.getRandom().nextFloat() < 0.3f) {
          level.playSound(
              null,
              player.getX(),
              player.getY(),
              player.getZ(),
              SoundEvents.HUSK_AMBIENT,
              SoundSource.AMBIENT,
              0.5f,
              1.2f);
        }
      }
    }
  }
}
