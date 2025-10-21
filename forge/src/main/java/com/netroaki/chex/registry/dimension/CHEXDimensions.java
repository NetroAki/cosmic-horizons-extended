package com.netroaki.chex.registry.dimension;

import com.netroaki.chex.CHEX;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

public class CHEXDimensions {
  public static final DeferredRegister<LevelStem> LEVEL_STEMS =
      DeferredRegister.create(Registries.LEVEL_STEM, CHEX.MOD_ID);

  public static final DeferredRegister<DimensionType> DIMENSION_TYPES =
      DeferredRegister.create(Registries.DIMENSION_TYPE, CHEX.MOD_ID);

  // TODO: Fix when DimensionType constructor is available
  // Register the Ringworld dimension
  // public static final RegistryObject<DimensionType> RINGWORLD_DIMENSION_TYPE =
  // DIMENSION_TYPES.register(
  // "ringworld",
  // () -> new DimensionType(
  // OptionalLong.empty(), // fixedTime
  // true, // hasSkyLight
  // false, // hasCeiling
  // false, // ultrawarm
  // true, // natural
  // 1.0, // coordinateScale
  // false, // piglinSafe
  // true, // bedWorks
  // false, // respawnAnchorWorks
  // 256, // height
  // 256, // logicalHeight
  // 0, // minY
  // OptionalLong.empty(), // effects
  // OptionalLong.empty(), // fixedTime
  // new DimensionType.MonsterSettings(
  // false, // piglinSafe
  // false, // hasRaids
  // OptionalLong.empty(), // bedWorks
  // false // respawnAnchorWorks
  // )));

  // TODO: Fix when LevelStem constructor is available
  // public static final RegistryObject<LevelStem> RINGWORLD_LEVEL_STEM =
  // LEVEL_STEMS.register(
  // "ringworld",
  // () -> new LevelStem(
  // () -> RINGWORLD_DIMENSION_TYPE.get(),
  // null // TODO: Implement RingworldDimension.RingworldChunkGenerator
  // ));

  public static void register(IEventBus eventBus) {
    LEVEL_STEMS.register(eventBus);
    DIMENSION_TYPES.register(eventBus);
  }

  // Helper method to check if a level is the Ringworld
  // TODO: Implement when RingworldDimension is available
  // public static boolean isRingworld(Level level) {
  // return level.dimension().equals(RingworldDimension.RINGWORLD_LEVEL);
  // }

  // Get the Ringworld dimension key
  // TODO: Implement when RingworldDimension is available
  // public static ResourceKey<Level> getRingworldKey() {
  // return RingworldDimension.RINGWORLD_LEVEL;
  // }
}
