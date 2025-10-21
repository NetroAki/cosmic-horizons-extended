package com.netroaki.chex.world.library;

import com.netroaki.chex.CHEX;
import java.util.function.Function;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LibraryDimension {
  public static final ResourceKey<Level> LIBRARY_DIMENSION =
      ResourceKey.create(
          Registries.DIMENSION, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));
  public static final ResourceKey<LevelStem> LIBRARY_STEM =
      ResourceKey.create(
          Registries.LEVEL_STEM, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));
  public static final ResourceKey<DimensionType> LIBRARY_DIM_TYPE =
      ResourceKey.create(
          Registries.DIMENSION_TYPE, new ResourceLocation(CHEX.MOD_ID, "infinite_library"));

  @SubscribeEvent
  public static void onWorldLoad(LevelEvent.Load event) {
    if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel) {
      // Initialize dimension-specific data when the world loads
    }
  }

  public static void register() {
    // Registration is handled by Forge's registry system
  }

  public static ITeleporter getTeleporter(ServerLevel level, Entity entity) {
    return new ITeleporter() {
      @Override
      public Entity placeEntity(
          Entity entity,
          ServerLevel currentWorld,
          ServerLevel destWorld,
          float yaw,
          Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false);

        // Find a safe spawn location
        BlockPos spawnPos = findSafeSpawnLocation(destWorld);
        entity.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);

        // Apply dimension-specific effects
        if (entity instanceof ServerPlayer player) {
          player.addEffect(
              new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }

        return entity;
      }
    };
  }

  private static BlockPos findSafeSpawnLocation(ServerLevel level) {
    // Start at world spawn
    BlockPos.MutableBlockPos pos = level.getSharedSpawnPos().mutable();

    // Find the top block at this position
    pos.setY(
        level.getChunk(pos).getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) + 1);

    // Ensure we're not spawning in a wall
    while (!level.getBlockState(pos).isAir() && pos.getY() < level.getMaxBuildHeight()) {
      pos.move(Direction.UP);
    }

    return pos.immutable();
  }

  public static class ChunkGenerator extends NoiseBasedChunkGenerator {
    private static final Codec<LibraryDimension.ChunkGenerator> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        RegistryOps.retrieveRegistry(Registries.NOISE).forGetter(generator -> null),
                        Codec.LONG.fieldOf("seed").forGetter(generator -> generator.seed))
                    .apply(instance, instance.stable(LibraryDimension.ChunkGenerator::new)));

    private final long seed;

    public ChunkGenerator(Registry<NormalNoise.NoiseParameters> noiseParams, long seed) {
      super(
          noiseParams,
          BuiltinRegistries.STRUCTURE_SET,
          BuiltinRegistries.BIOME_SOURCE,
          seed,
          () -> NoiseGeneratorSettings.overworld(true));
      this.seed = seed;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
      return CODEC;
    }

    @Override
    public void buildSurface(
        WorldGenRegion region,
        StructureManager structureManager,
        RandomState randomState,
        ChunkAccess chunk) {
      // Custom surface generation for the library dimension
      super.buildSurface(region, structureManager, randomState, chunk);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
        Executor executor,
        Blender blender,
        RandomState randomState,
        StructureManager structureManager,
        ChunkAccess chunk) {
      // Custom chunk generation for the library dimension
      return super.fillFromNoise(executor, blender, randomState, structureManager, chunk);
    }
  }
}
