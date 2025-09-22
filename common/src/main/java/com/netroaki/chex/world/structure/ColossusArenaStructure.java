package com.netroaki.chex.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class ColossusArenaStructure extends Structure {
    public static final Codec<ColossusArenaStructure> CODEC = RecordCodecBuilder.create(
        builder -> builder.group(
            settingsCodec(builder),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
            Codec.INT.fieldOf("size").forGetter(structure -> structure.size)
        ).apply(builder, ColossusArenaStructure::new)
    );

    private final HeightProvider startHeight;
    private final int size;

    public ColossusArenaStructure(StructureSettings settings, HeightProvider startHeight, int size) {
        super(settings);
        this.startHeight = startHeight;
        this.size = size;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int height = this.startHeight.sample(
            context.random(),
            new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor())
        );
        
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        
        return Optional.of(new GenerationStub(
            blockPos,
            builder -> generatePieces(builder, context, blockPos)
        ));
    }
    
    private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos pos) {
        // Center the structure on the chunk
        int x = pos.getX() + 8;
        int z = pos.getZ() + 8;
        
        // Find the highest block at this position
        int y = context.chunkGenerator().getFirstOccupiedHeight(
            x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()
        );
        
        BlockPos centerPos = new BlockPos(x, y, z);
        
        // Add the main arena piece
        builder.addPiece(new ColossusArenaPiece(
            context.structureTemplateManager(),
            new ResourceLocation("chex:verdant_colossus_arena"),
            centerPos,
            Rotation.getRandom(context.random())
        ));
    }

    @Override
    public StructureType<?> type() {
        return StructureRegistry.COLOSSUS_ARENA.get();
    }
}
