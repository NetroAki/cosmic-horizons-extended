package com.netroaki.chex.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.netroaki.chex.registry.ArrakisStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class FremenVillageStructure extends Structure {
    public static final Codec<FremenVillageStructure> CODEC = RecordCodecBuilder.<FremenVillageStructure>mapCodec(
        instance -> instance.group(
            settingsCodec(instance),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight)
        ).apply(instance, FremenVillageStructure::new)
    ).codec();
    
    private final HeightProvider startHeight;
    
    public FremenVillageStructure(Structure.StructureSettings config, HeightProvider startHeight) {
        super(config);
        this.startHeight = startHeight;
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int y = this.startHeight.sample(
            context.random(),
            new WorldGenerationContext(
                context.chunkGenerator(),
                context.heightAccessor()
            )
        );
        
        int chunkX = context.chunkPos().getMiddleBlockX();
        int chunkZ = context.chunkPos().getMiddleBlockZ();
        
        return Optional.of(new GenerationStub(
            new BlockPos(chunkX, y, chunkZ),
            builder -> generatePieces(builder, context, y)
        ));
    }
    
    private static void generatePieces(StructurePiecesBuilder builder, GenerationContext context, int y) {
        BlockPos centerPos = new BlockPos(
            context.chunkPos().getMiddleBlockX(),
            y,
            context.chunkPos().getMiddleBlockZ()
        );
        
        // Add village pieces here
        FremenVillagePieces.addPieces(
            context.structureTemplateManager(),
            centerPos,
            builder,
            context.random()
        );
    }
    
    @Override
    public StructureType<?> type() {
        return ArrakisStructures.FREMEN_VILLAGE.get();
    }
    
    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long seed, ChunkPos chunkPos, Biome biome, ChunkPos pos, BiomeGenerationSettings biomeGenSettings, StructureFeatureManager structureFeatureManager) {
        // Only generate in desert biomes
        if (!biome.getBiomeCategory().equals(Biome.BiomeCategory.DESERT)) {
            return false;
        }
        
        // Check for a relatively flat area
        return isAreaSuitableForVillage(
            chunkGenerator,
            chunkPos,
            context.heightAccessor(),
            48,  // Check a 48x48 area
            4.0f // Max height variation
        );
    }
    
    private boolean isAreaSuitableForVillage(ChunkGenerator chunkGenerator, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, int size, float maxHeightVariation) {
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();
        int halfSize = size / 2;
        
        int[] heights = new int[9];
        int index = 0;
        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                int checkX = centerX + (x * halfSize);
                int checkZ = centerZ + (z * halfSize);
                heights[index++] = chunkGenerator.getBaseHeight(
                    checkX, checkZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor
                );
            }
        }
        
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        for (int height : heights) {
            if (height < min) min = height;
            if (height > max) max = height;
        }
        
        return (max - min) <= maxHeightVariation;
    }
}
