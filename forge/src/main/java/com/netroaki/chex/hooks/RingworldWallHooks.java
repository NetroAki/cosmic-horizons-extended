package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Hooks for generating ringworld wall blocks around the perimeter */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RingworldWallHooks {

  private static final int RINGWORLD_RADIUS = 1000; // Radius of the ringworld
  private static final int WALL_THICKNESS = 3; // Thickness of the wall
  private static final int WALL_HEIGHT = 64; // Height of the wall

  @SubscribeEvent
  public static void onChunkLoad(ChunkEvent.Load event) {
    if (!(event.getLevel() instanceof ServerLevel level)) return;
    if (!level
        .dimension()
        .location()
        .toString()
        .equals("cosmic_horizons_extended:aurelia_ringworld")) return;

    LevelChunk chunk = (LevelChunk) event.getChunk();
    generateWallBlocks(level, chunk);
  }

  private static void generateWallBlocks(ServerLevel level, LevelChunk chunk) {
    BlockPos chunkCenter = chunk.getPos().getMiddleBlockPosition(0);
    int chunkX = chunkCenter.getX();
    int chunkZ = chunkCenter.getZ();

    // Calculate distance from origin
    double distance = Math.sqrt(chunkX * chunkX + chunkZ * chunkZ);

    // Check if this chunk is near the ringworld edge
    if (isNearRingworldEdge(distance)) {
      generateWallInChunk(level, chunk, distance);
    }
  }

  private static boolean isNearRingworldEdge(double distance) {
    // Generate wall if we're within wall thickness of the ringworld edge
    return distance >= (RINGWORLD_RADIUS - WALL_THICKNESS)
        && distance <= (RINGWORLD_RADIUS + WALL_THICKNESS);
  }

  private static void generateWallInChunk(ServerLevel level, LevelChunk chunk, double distance) {
    BlockPos chunkPos = chunk.getPos().getWorldPosition();

    // Iterate through all blocks in the chunk
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        BlockPos worldPos = chunkPos.offset(x, 0, z);
        double blockDistance =
            Math.sqrt(worldPos.getX() * worldPos.getX() + worldPos.getZ() * worldPos.getZ());

        // Check if this block should be a wall
        if (shouldBeWall(blockDistance)) {
          generateWallColumn(level, worldPos, blockDistance);
        }
      }
    }
  }

  private static boolean shouldBeWall(double distance) {
    // Create wall if we're at the ringworld edge
    return distance >= RINGWORLD_RADIUS - 1 && distance <= RINGWORLD_RADIUS + 1;
  }

  private static void generateWallColumn(ServerLevel level, BlockPos pos, double distance) {
    boolean isOuterWall = distance > RINGWORLD_RADIUS;

    // Generate wall from bedrock to surface
    for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
      BlockPos wallPos = pos.atY(y);
      BlockState currentState = level.getBlockState(wallPos);

      // Only replace air or replaceable blocks
      if (currentState.isAir() || currentState.canBeReplaced()) {
        BlockState wallState =
            CHEXBlocks.RINGWORLD_WALL
                .get()
                .defaultBlockState()
                .setValue(com.netroaki.chex.blocks.RingworldWallBlock.IS_OUTER_WALL, isOuterWall);

        level.setBlock(wallPos, wallState, 3);

        // Set block entity data
        if (level.getBlockEntity(wallPos)
            instanceof com.netroaki.chex.blocks.RingworldWallBlockEntity wallEntity) {
          wallEntity.setOuterWall(isOuterWall);
          wallEntity.setWallHeight(WALL_HEIGHT);
        }
      }
    }
  }
}
