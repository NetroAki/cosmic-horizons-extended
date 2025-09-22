package com.netroaki.chex.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import com.netroaki.chex.CHEX;

import java.util.List;

public class ColossusArenaPiece extends TemplateStructurePiece {
    private static final ResourceLocation ARENA_TEMPLATE = new ResourceLocation(CHEX.MOD_ID, "verdant_colossus_arena");
    private final Rotation rotation;

    public ColossusArenaPiece(StructureTemplateManager templateManager, ResourceLocation template, BlockPos pos, Rotation rotation) {
        super(StructureRegistry.COLOSSUS_ARENA_PIECE.get(), 0, templateManager, template, template.toString(), 
              makeSettings(rotation), pos.offset(-8, -1, -8));
        this.rotation = rotation;
    }

    public ColossusArenaPiece(StructureTemplateManager templateManager, CompoundTag tag) {
        super(StructureRegistry.COLOSSUS_ARENA_PIECE.get(), tag, templateManager, 
              resourceLocation -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
        this.rotation = Rotation.valueOf(tag.getString("Rot"));
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings()
            .setRotation(rotation)
            .setMirror(Mirror.NONE)
            .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Rot", this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
        // Handle special data markers in the structure
        if ("boss_spawn".equals(function)) {
            // We'll spawn the boss when the structure is generated
            level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            
            // Schedule boss spawn for 1 tick later to ensure structure is fully loaded
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.getServer().tell(() -> {
                    VerdantColossusEntity boss = EntityRegistry.VERDANT_COLOSSUS.get().create(serverLevel);
                    if (boss != null) {
                        boss.setPersistenceRequired();
                        boss.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
                        serverLevel.addFreshEntityWithPassengers(boss);
                    }
                });
            }
        }
    }
}
