package com.netroaki.chex.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool.Projection;

public class StellarAvatarArenaStructure extends Structure {
  public static final Codec<StellarAvatarArenaStructure> CODEC =
      RecordCodecBuilder.<StellarAvatarArenaStructure>mapCodec(
              instance ->
                  instance
                      .group(
                          StellarAvatarArenaStructure.settingsCodec(instance),
                          StructureTemplatePool.CODEC
                              .fieldOf("start_pool")
                              .forGetter(structure -> structure.startPool),
                          ResourceLocation.CODEC
                              .optionalFieldOf("start_jigsaw_name")
                              .forGetter(structure -> structure.startJigsawName),
                          Codec.intRange(0, 30)
                              .fieldOf("size")
                              .forGetter(structure -> structure.size),
                          HeightProvider.CODEC
                              .fieldOf("start_height")
                              .forGetter(structure -> structure.startHeight),
                          Codec.BOOL
                              .fieldOf("use_expansion_hack")
                              .orElse(false)
                              .forGetter(structure -> structure.useExpansionHack),
                          Projection.CODEC
                              .optionalFieldOf("projection", Projection.RIGID)
                              .forGetter(structure -> structure.projection),
                          Codec.intRange(1, 128)
                              .fieldOf("max_distance_from_center")
                              .forGetter(structure -> structure.maxDistanceFromCenter))
                      .apply(instance, StellarAvatarArenaStructure::new))
          .codec();

  private final Holder<StructureTemplatePool> startPool;
  private final Optional<ResourceLocation> startJigsawName;
  private final int size;
  private final HeightProvider startHeight;
  private final boolean useExpansionHack;
  private final Projection projection;
  private final int maxDistanceFromCenter;

  public StellarAvatarArenaStructure(
      Structure.StructureSettings config,
      Holder<StructureTemplatePool> startPool,
      Optional<ResourceLocation> startJigsawName,
      int size,
      HeightProvider startHeight,
      boolean useExpansionHack,
      Projection projection,
      int maxDistanceFromCenter) {
    super(config);
    this.startPool = startPool;
    this.startJigsawName = startJigsawName;
    this.size = size;
    this.startHeight = startHeight;
    this.useExpansionHack = useExpansionHack;
    this.projection = projection;
    this.maxDistanceFromCenter = maxDistanceFromCenter;
  }

  @Override
  public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
    // Check if the location is valid for generation
    if (!isFeatureChunk(context)) {
      return Optional.empty();
    }

    // Get the height at the chunk's position
    int y =
        this.startHeight.sample(
            context.random(),
            new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

    // Get the chunk's position
    ChunkPos chunkPos = context.chunkPos();
    BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());

    // Return the structure's generation stub
    return Optional.of(
        new GenerationStub(
            blockPos,
            builder -> {
              generatePieces(builder, context);
            }));
  }

  private static boolean isFeatureChunk(Structure.GenerationContext context) {
    // Only generate in the Alpha Centauri dimension
    if (!context.chunkGenerator().getBiomeSource().possibleBiomes().stream()
        .anyMatch(
            biomeHolder ->
                biomeHolder.is(
                    ResourceKey.create(
                        Registries.BIOME, new ResourceLocation("chex", "alpha_centauri_a"))))) {
      return false;
    }

    // Check if the surface is solid (not over void or liquid)
    ChunkPos chunkPos = context.chunkPos();
    int x = chunkPos.getBlockX(8);
    int z = chunkPos.getBlockZ(8);

    // Get the top block at this position
    int y =
        context
            .chunkGenerator()
            .getFirstFreeHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState());

    // Check if the position is valid
    if (y < context.chunkGenerator().getSeaLevel()) {
      return false;
    }

    // Check if the surface is solid
    NoiseColumn column =
        context
            .chunkGenerator()
            .getBaseColumn(x, z, context.heightAccessor(), context.randomState());
    BlockState state = column.getBlock(y);
    return !state.isAir() && !state.getMaterial().isLiquid();
  }

  public static void generatePieces(
      StructurePiecesBuilder builder, Structure.GenerationContext context) {
    // This will be implemented in the next step
  }

  @Override
  public StructureType<?> type() {
    return CHEStructures.STELLAR_AVATAR_ARENA.get();
  }
}
