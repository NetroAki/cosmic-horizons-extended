package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Enforces the ring band: clears blocks outside the band to air and builds black wall columns at
 * the band edge up to a fixed height. Works with the datapack-driven overworld generator by
 * post-processing chunks on load.
 */
public final class RingworldBandHooks {

  private static final int BAND_HALF_WIDTH = 48; // 6 chunks wide band
  private static final int WALL_HEIGHT = 160;
  private static final int MAX_COLUMNS_PER_TICK = 64; // conservative throttle

  private static final ResourceKey<Level> AURELIA_RINGWORLD =
      ResourceKey.create(Registries.DIMENSION, CHEX.id("aurelia_ringworld"));

  // Track processed chunks per dimension to avoid redundant work on reloads
  private static final Map<ResourceKey<Level>, Set<Long>> PROCESSED_BY_DIM =
      new ConcurrentHashMap<>();
  private static final Map<ResourceKey<Level>, java.util.ArrayDeque<long[]>> PENDING_COLUMNS =
      new ConcurrentHashMap<>();

  public static void register() {
    MinecraftForge.EVENT_BUS.register(new RingworldBandHooks());
  }

  @SubscribeEvent
  public void onChunkLoad(final ChunkEvent.Load event) {
    /* no-op; work is triggered on watch */
  }

  @net.minecraftforge.eventbus.api.SubscribeEvent
  public void onChunkWatch(final net.minecraftforge.event.level.ChunkWatchEvent.Watch event) {
    final net.minecraft.server.level.ServerLevel serverLevel = event.getLevel();
    if (!serverLevel.dimension().location().equals(AURELIA_RINGWORLD.location())) return;
    final net.minecraft.world.level.ChunkPos posChunk = event.getPos();

    final int minX = posChunk.getMinBlockX();
    final int minZ = posChunk.getMinBlockZ();
    final int maxZ = minZ + 15;
    final int minY = serverLevel.getMinBuildHeight();
    final int maxY = serverLevel.getMaxBuildHeight();
    final int maxAbsZ = Math.max(Math.abs(minZ), Math.abs(maxZ));

    // Skip if we already processed this chunk recently
    final long chunkKey = net.minecraft.world.level.ChunkPos.asLong(posChunk.x, posChunk.z);
    final Set<Long> processed =
        PROCESSED_BY_DIM.computeIfAbsent(
            serverLevel.dimension(), d -> ConcurrentHashMap.newKeySet());
    if (!processed.add(chunkKey)) {
      return;
    }

    int enqueued = 0;
    // Queue all outside-band columns in this watched chunk
    for (int lx = 0; lx < 16; lx++) {
      int worldX = minX + lx;
      for (int lz = 0; lz < 16; lz++) {
        int worldZ = minZ + lz;
        int absZ = Math.abs(worldZ);
        if (absZ > BAND_HALF_WIDTH) {
          PENDING_COLUMNS
              .computeIfAbsent(serverLevel.dimension(), d -> new java.util.ArrayDeque<>())
              .add(new long[] {worldX, worldZ, minY, maxY});
          enqueued++;
        }
      }
    }

    // Build wall columns in this watched chunk where the boundary crosses it
    if (Math.min(Math.abs(minZ), Math.abs(maxZ)) <= BAND_HALF_WIDTH && maxAbsZ >= BAND_HALF_WIDTH) {
      final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
      final BlockState wall = CHEXBlocks.AURELIA_WALL.get().defaultBlockState();
      int wallTop = Math.min(WALL_HEIGHT, maxY - 1);
      for (int lx = 0; lx < 16; lx++) {
        int worldX = minX + lx;
        for (int lz = 0; lz < 16; lz++) {
          int worldZ = minZ + lz;
          if (Math.abs(worldZ) == BAND_HALF_WIDTH) {
            for (int y = minY; y <= wallTop; y++) {
              bp.set(worldX, y, worldZ);
              // use level set to ensure client gets updates
              if (!serverLevel.getBlockState(bp).equals(wall)) {
                serverLevel.setBlock(bp, wall, 2);
              }
            }
          }
        }
      }
    }

    if (enqueued > 0) {
      com.netroaki.chex.CHEX.LOGGER.info(
          "[RingBand] Watched chunk {},{}: enqueued {} columns", posChunk.x, posChunk.z, enqueued);
    }
  }

  @SubscribeEvent
  public void onLevelTick(net.minecraftforge.event.TickEvent.LevelTickEvent e) {
    if (e.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
    if (!(e.level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
    final var queue = PENDING_COLUMNS.get(serverLevel.dimension());
    if (queue == null || queue.isEmpty()) return;
    final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    int processed = 0;
    while (processed < MAX_COLUMNS_PER_TICK && !queue.isEmpty()) {
      long[] data = queue.pollFirst();
      if (data == null) break;
      int worldX = (int) data[0];
      int worldZ = (int) data[1];
      int minY = (int) data[2];
      int maxY = (int) data[3];
      int chunkX = worldX >> 4;
      int chunkZ = worldZ >> 4;
      net.minecraft.world.level.chunk.LevelChunk chunk = serverLevel.getChunk(chunkX, chunkZ);
      for (int y = maxY - 1; y >= minY; y--) {
        pos.set(worldX, y, worldZ);
        var st = chunk.getBlockState(pos);
        if (!st.isAir()) {
          for (int yy = y; yy >= minY; yy--) {
            pos.set(worldX, yy, worldZ);
            chunk.setBlockState(
                pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), false);
          }
          break;
        }
      }
      processed++;
    }
    if (processed > 0) {
      com.netroaki.chex.CHEX.LOGGER.info(
          "[RingBand] Culled {} columns this tick in {}",
          processed,
          serverLevel.dimension().location());
    }
  }

  // Fallback: ensure nearby chunks around players are enqueued if not already
  @SubscribeEvent
  public void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent e) {
    if (e.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
    if (!(e.player instanceof net.minecraft.server.level.ServerPlayer sp)) return;
    final net.minecraft.server.level.ServerLevel serverLevel = sp.serverLevel();
    if (!serverLevel.dimension().location().equals(AURELIA_RINGWORLD.location())) return;
    final int pcx = sp.chunkPosition().x;
    final int pcz = sp.chunkPosition().z;
    final int radius = 2; // small radius per tick
    final int minY = serverLevel.getMinBuildHeight();
    final int maxY = serverLevel.getMaxBuildHeight();
    final Set<Long> processed =
        PROCESSED_BY_DIM.computeIfAbsent(
            serverLevel.dimension(), d -> ConcurrentHashMap.newKeySet());
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        int cx = pcx + dx;
        int cz = pcz + dz;
        long key = net.minecraft.world.level.ChunkPos.asLong(cx, cz);
        if (!processed.add(key)) continue;
        int minX = cx * 16;
        int minZ = cz * 16;
        for (int lx = 0; lx < 16; lx++) {
          int worldX = minX + lx;
          for (int lz = 0; lz < 16; lz++) {
            int worldZ = minZ + lz;
            int absZ = Math.abs(worldZ);
            if (absZ > BAND_HALF_WIDTH) {
              PENDING_COLUMNS
                  .computeIfAbsent(serverLevel.dimension(), d -> new java.util.ArrayDeque<>())
                  .add(new long[] {worldX, worldZ, minY, maxY});
            }
          }
        }
        // build walls if boundary crosses this chunk
        int maxAbsZ = Math.max(Math.abs(minZ), Math.abs(minZ + 15));
        if (Math.min(Math.abs(minZ), Math.abs(minZ + 15)) <= BAND_HALF_WIDTH
            && maxAbsZ >= BAND_HALF_WIDTH) {
          final BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
          final BlockState wall = CHEXBlocks.AURELIA_WALL.get().defaultBlockState();
          int wallTop = Math.min(WALL_HEIGHT, maxY - 1);
          for (int lx2 = 0; lx2 < 16; lx2++) {
            int worldX2 = minX + lx2;
            for (int lz2 = 0; lz2 < 16; lz2++) {
              int worldZ2 = minZ + lz2;
              if (Math.abs(worldZ2) == BAND_HALF_WIDTH) {
                for (int y = minY; y <= wallTop; y++) {
                  bp.set(worldX2, y, worldZ2);
                  if (!serverLevel.getBlockState(bp).equals(wall)) {
                    serverLevel.setBlock(bp, wall, 2);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
