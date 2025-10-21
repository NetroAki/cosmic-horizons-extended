package com.netroaki.chex.world.spawning;

import com.netroaki.chex.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = "cosmic_horizons_extended")
public class SandEmperorSpawnPlacement {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final int SPAWN_CHANCE = 100; // 1 in X chance per storm tick
  private static final int MIN_PLAYER_DISTANCE = 100; // Minimum distance from players to spawn
  private static final int MAX_PLAYER_DISTANCE = 200; // Maximum distance from players to spawn
  private static final int STORM_DURATION_THRESHOLD = 6000; // 5 minutes of storm time

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onWorldTick(TickEvent.LevelTickEvent event) {
    // Only run on server, during end phase, and in overworld dimension
    if (event.phase != TickEvent.Phase.END
        || event.level.isClientSide
        || !event.level.dimension().equals(Level.OVERWORLD)) {
      return;
    }

    ServerLevel level = (ServerLevel) event.level;

    // Only attempt spawn during thunderstorm
    if (!level.isThundering() || !level.getLevelData().isRaining()) {
      return;
    }

    // Check if enough time has passed in the storm
    if (level.getLevelData().getRainTime() < STORM_DURATION_THRESHOLD) {
      return;
    }

    // Check if any players are online
    if (level.players().isEmpty()) {
      return;
    }

    // Only attempt spawn occasionally to reduce lag
    if (level.random.nextInt(SPAWN_CHANCE) != 0) {
      return;
    }

    // Check if mob spawning is allowed
    if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
      return;
    }

    // Try to spawn the Sand Emperor
    attemptSpawn(level);
  }

  private static void attemptSpawn(ServerLevel level) {
    // Find a random player to spawn near
    if (level.players().isEmpty()) return;

    var player = level.players().get(level.random.nextInt(level.players().size()));
    if (player == null) return;

    // Calculate spawn position
    double angle = level.random.nextDouble() * Math.PI * 2.0D;
    double distance =
        MIN_PLAYER_DISTANCE
            + level.random.nextDouble() * (MAX_PLAYER_DISTANCE - MIN_PLAYER_DISTANCE);
    double x = player.getX() + Math.cos(angle) * distance;
    double z = player.getZ() + Math.sin(angle) * distance;

    // Find a suitable Y position
    BlockPos spawnPos = findSpawnPosition(level, new BlockPos(x, 0, z));
    if (spawnPos == null) return;

    // Check if the spawn position is valid
    if (!isValidSpawnPosition(level, spawnPos)) return;

    // Spawn the Sand Emperor
    var sandEmperor = EntityInit.SAND_EMPEROR.get().create(level);
    if (sandEmperor == null) return;

    sandEmperor.moveTo(
        spawnPos.getX() + 0.5,
        spawnPos.getY(),
        spawnPos.getZ() + 0.5,
        level.random.nextFloat() * 360.0F,
        0.0F);

    // Add spawn effects
    level.levelEvent(2004, spawnPos, 0);
    level.levelEvent(2001, spawnPos, 0);

    // Spawn the entity
    if (level.addFreshEntity(sandEmperor)) {
      LOGGER.info("Spawned Sand Emperor at {}", spawnPos);

      // Trigger a local storm centered on the spawn position
      level.setWeatherParameters(0, 12000, true, true);
      level.setThunderLevel(1.0F);
    }
  }

  private static BlockPos findSpawnPosition(LevelAccessor level, BlockPos center) {
    // Find a suitable Y position at the given x,z coordinates
    int y =
        level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, center.getX(), center.getZ());

    // Make sure the position is in a desert biome
    if (!level.getBiome(center).is(Biomes.DESERT)
        && !level.getBiome(center).is(Biomes.DESERT_HILLS)
        && !level.getBiome(center).is(Biomes.DESERT_LAKES)) {
      return null;
    }

    // Check if the block below is solid
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(center.getX(), y, center.getZ());
    while (pos.getY() > 0 && !level.getBlockState(pos.below()).getMaterial().isSolid()) {
      pos.move(0, -1, 0);
    }

    return pos.immutable();
  }

  private static boolean isValidSpawnPosition(LevelAccessor level, BlockPos pos) {
    // Check if the position is in a valid biome
    Biome biome = level.getBiome(pos).value();
    if (!biome.getBiomeCategory().equals(Biome.BiomeCategory.DESERT)) {
      return false;
    }

    // Check if there's enough space
    for (int y = 0; y < 4; y++) {
      if (!level.getBlockState(pos.above(y)).isAir()) {
        return false;
      }
    }

    return true;
  }

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
    // Prevent natural spawning of Sand Emperor
    if (event.getEntity().getType() == EntityInit.SAND_EMPEROR.get()) {
      if (event.getSpawnReason() != MobSpawnType.EVENT
          && event.getSpawnReason() != MobSpawnType.COMMAND
          && event.getSpawnReason() != MobSpawnType.SPAWN_EGG) {
        event.setCanceled(true);
      }
    }
  }
}
