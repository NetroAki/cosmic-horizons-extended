package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Mirrors chunk contents along X so that chunks beyond the circumference reuse the canonical chunk
 * data. This ensures edits at the seam persist across the loop without relying on player teleport
 * wrapping.
 */
public final class RingworldChunkMirrorHooks {

  private static final double RING_CIRCUMFERENCE =
      2000.0; // must match wrap hooks and gameplay expectations
  private static final int WRAP_CHUNKS = (int) (RING_CIRCUMFERENCE / 16.0); // 125
  private static final int BAND_HALF_WIDTH = 180; // keep in sync with HaloRingworldGenerator

  private static final ResourceKey<Level> AURELIA_RINGWORLD =
      ResourceKey.create(Registries.DIMENSION, CHEX.id("aurelia_ringworld"));

  public static void register() {
    MinecraftForge.EVENT_BUS.register(new RingworldChunkMirrorHooks());
  }

  @SubscribeEvent
  public void onChunkLoad(final ChunkEvent.Load event) {
    if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
    if (!serverLevel.dimension().location().equals(AURELIA_RINGWORLD.location())) return;
    if (!(event.getChunk() instanceof LevelChunk targetChunk)) return;

    final int targetChunkX = targetChunk.getPos().x;
    final int targetChunkZ = targetChunk.getPos().z;

    final int canonicalChunkX = Math.floorMod(targetChunkX, WRAP_CHUNKS);
    if (canonicalChunkX == targetChunkX) {
      return; // already canonical
    }

    // Force-load the canonical source chunk (this may generate it if not present)
    LevelChunk sourceChunk = serverLevel.getChunk(canonicalChunkX, targetChunkZ);

    BlockPos.MutableBlockPos srcPos = new BlockPos.MutableBlockPos();
    BlockPos.MutableBlockPos dstPos = new BlockPos.MutableBlockPos();

    final int minY = targetChunk.getMinBuildHeight();
    final int maxY = targetChunk.getMaxBuildHeight();
    final int dstMinX = targetChunk.getPos().getMinBlockX();
    final int dstMinZ = targetChunk.getPos().getMinBlockZ();
    final int srcMinX = sourceChunk.getPos().getMinBlockX();
    final int srcMinZ = sourceChunk.getPos().getMinBlockZ();

    for (int lx = 0; lx < 16; lx++) {
      int worldX = dstMinX + lx;
      int srcWorldX = srcMinX + lx;
      for (int lz = 0; lz < 16; lz++) {
        int worldZ = dstMinZ + lz;
        int srcWorldZ = srcMinZ + lz;
        boolean insideBand = Math.abs(worldZ) <= BAND_HALF_WIDTH;
        for (int y = minY; y < maxY; y++) {
          dstPos.set(worldX, y, worldZ);
          if (insideBand) {
            srcPos.set(srcWorldX, y, srcWorldZ);
            targetChunk.setBlockState(dstPos, sourceChunk.getBlockState(srcPos), false);
          } else {
            targetChunk.setBlockState(dstPos, Blocks.AIR.defaultBlockState(), false);
          }
        }
      }
    }
  }
}
