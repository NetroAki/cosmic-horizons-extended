package com.netroaki.chex.integration.gtceu;

import com.netroaki.chex.CHEX;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Proper GTCEu integration using GTCEu's 3x3 chunk section system This integrates with GTCEu's
 * actual ore generation philosophy
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GTCEuWorldGenIntegration {

  /**
   * GTCEu uses a 3x3 chunk section system for ore generation Each section contains 1 or more ore
   * veins with 2-4 different ore types Veins are 10-20 blocks wide and contain 1,000-5,000 ore
   * blocks
   */
  private static final int SECTION_SIZE = 3; // 3x3 chunks = 1 section

  private static final int CHUNK_SIZE = 16;
  private static final int SECTION_CHUNK_SIZE = SECTION_SIZE * CHUNK_SIZE; // 48 blocks

  @SubscribeEvent
  public static void onChunkLoad(ChunkEvent.Load event) {
    if (!(event.getLevel() instanceof ServerLevel level)) return;

    // Only process CHEX dimensions
    if (!isCHEXDimension(level)) return;

    // Get chunk coordinates
    int chunkX = event.getChunk().getPos().x;
    int chunkZ = event.getChunk().getPos().z;

    // Check if this is the center chunk of a 3x3 section
    if (isSectionCenter(chunkX, chunkZ)) {
      generateGTCEuSection(level, chunkX, chunkZ);
    }
  }

  private static boolean isCHEXDimension(ServerLevel level) {
    ResourceLocation dimensionId = level.dimension().location();
    return dimensionId.getNamespace().equals("cosmic_horizons_extended")
        || dimensionId.getNamespace().equals("cosmos");
  }

  private static boolean isSectionCenter(int chunkX, int chunkZ) {
    // GTCEu sections are 3x3 chunks, so center chunks are every 3rd chunk
    return (chunkX % SECTION_SIZE == 0) && (chunkZ % SECTION_SIZE == 0);
  }

  private static void generateGTCEuSection(ServerLevel level, int centerChunkX, int centerChunkZ) {
    // Get biome for ore type selection
    BlockPos centerPos =
        new BlockPos(
            centerChunkX * CHUNK_SIZE + CHUNK_SIZE / 2,
            64,
            centerChunkZ * CHUNK_SIZE + CHUNK_SIZE / 2);

    Biome biome = level.getBiome(centerPos).value();
    ResourceLocation biomeId =
        level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome);

    if (biomeId == null) return;

    // Generate GTCEu-style ore section
    generateOreSection(level, centerChunkX, centerChunkZ, biomeId);
  }

  private static void generateOreSection(
      ServerLevel level, int centerChunkX, int centerChunkZ, ResourceLocation biomeId) {
    // Use section coordinates for deterministic generation
    Random sectionRandom = new Random(level.getSeed() + (long) centerChunkX * 31L + centerChunkZ);

    // GTCEu generates 1-3 ore veins per section
    int veinCount = 1 + sectionRandom.nextInt(3);

    for (int i = 0; i < veinCount; i++) {
      generateOreVein(level, centerChunkX, centerChunkZ, biomeId, sectionRandom, i);
    }

    CHEX.LOGGER.debug(
        "Generated {} ore veins in GTCEu section at ({}, {})",
        veinCount,
        centerChunkX,
        centerChunkZ);
  }

  private static void generateOreVein(
      ServerLevel level,
      int centerChunkX,
      int centerChunkZ,
      ResourceLocation biomeId,
      Random random,
      int veinIndex) {

    // Select ore types based on biome
    String[] oreTypes = getOreTypesForBiome(biomeId);
    if (oreTypes.length == 0) return;

    // GTCEu generates 2-4 different ore types per vein
    int oreTypeCount = 2 + random.nextInt(3);
    oreTypeCount = Math.min(oreTypeCount, oreTypes.length);

    // Generate vein center within the 3x3 section
    int veinCenterX = centerChunkX * CHUNK_SIZE + random.nextInt(SECTION_CHUNK_SIZE);
    int veinCenterZ = centerChunkZ * CHUNK_SIZE + random.nextInt(SECTION_CHUNK_SIZE);
    int veinCenterY = 16 + random.nextInt(48); // Y levels 16-64

    // GTCEu veins are 10-20 blocks wide
    int veinRadius = 5 + random.nextInt(11);

    // Generate 1,000-5,000 ore blocks per vein
    int oreBlockCount = 1000 + random.nextInt(4001);

    for (int i = 0; i < oreBlockCount; i++) {
      // Random position within vein radius
      int offsetX = random.nextInt(veinRadius * 2) - veinRadius;
      int offsetY = random.nextInt(veinRadius) - veinRadius / 2;
      int offsetZ = random.nextInt(veinRadius * 2) - veinRadius;

      BlockPos orePos =
          new BlockPos(veinCenterX + offsetX, veinCenterY + offsetY, veinCenterZ + offsetZ);

      if (level.isInWorldBounds(orePos) && canReplaceBlock(level.getBlockState(orePos))) {
        // Select random ore type for this block
        String oreType = oreTypes[random.nextInt(oreTypeCount)];
        placeOreBlock(level, orePos, oreType);
      }
    }
  }

  private static String[] getOreTypesForBiome(ResourceLocation biomeId) {
    String biomeName = biomeId.getPath();

    return switch (biomeName) {
      case "pandora_bioluminescent_forest" -> new String[] {
        "gtceu:cobalt_ore", "gtceu:nickel_ore", "gtceu:lithium_ore"
      };
      case "pandora_floating_mountains" -> new String[] {
        "gtceu:titanium_ore", "gtceu:vanadium_ore", "gtceu:aluminium_ore"
      };
      case "pandora_ocean_depths" -> new String[] {
        "gtceu:manganese_ore", "gtceu:rare_earth_ore", "gtceu:platinum_ore"
      };
      case "pandora_volcanic_wasteland" -> new String[] {
        "gtceu:tungsten_ore", "gtceu:chromium_ore", "gtceu:iridium_ore"
      };
      case "pandora_sky_islands" -> new String[] {
        "gtceu:aluminium_ore", "gtceu:copper_ore", "gtceu:silver_ore"
      };
      default -> new String[] {"gtceu:titanium_ore", "gtceu:vanadium_ore", "gtceu:lithium_ore"};
    };
  }

  private static void placeOreBlock(ServerLevel level, BlockPos pos, String oreType) {
    // Try to get the actual GTCEu ore block
    ResourceLocation oreLocation = ResourceLocation.parse(oreType);
    var oreBlock = level.registryAccess().registryOrThrow(Registries.BLOCK).get(oreLocation);

    if (oreBlock != null) {
      level.setBlock(pos, oreBlock.defaultBlockState(), 3);
    } else {
      // Fallback to pandorite if GTCEu ore not found
      var fallbackBlock =
          level
              .registryAccess()
              .registryOrThrow(Registries.BLOCK)
              .get(ResourceLocation.parse("cosmic_horizons_extended:pandorite_stone"));
      if (fallbackBlock != null) {
        level.setBlock(pos, fallbackBlock.defaultBlockState(), 3);
      }
    }
  }

  private static boolean canReplaceBlock(net.minecraft.world.level.block.state.BlockState state) {
    return state.is(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES)
        || state.is(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES)
        || state.getBlock().getDescriptionId().contains("pandorite");
  }
}
