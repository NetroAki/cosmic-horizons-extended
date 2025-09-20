package com.netroaki.chex.hooks;

import com.netroaki.chex.CHEX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Drains all water (including flowing, sources, bubble columns) and clears
 * waterlogged properties inside the ring band whenever a chunk loads in the
 * Aurelia dimension. This prevents neighboring chunks from reintroducing water
 * columns after generation.
 */
public final class RingworldDrainWaterHooks {

    private static final int BAND_HALF_WIDTH = 180; // keep in sync with HaloRingworldGenerator

    private static final ResourceKey<Level> AURELIA_RINGWORLD = ResourceKey.create(
            Registries.DIMENSION, CHEX.id("aurelia_ringworld"));

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new RingworldDrainWaterHooks());
    }

    @SubscribeEvent
    public void onChunkLoad(final ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;
        if (!serverLevel.dimension().location().equals(AURELIA_RINGWORLD.location())) return;
        if (!(event.getChunk() instanceof LevelChunk chunk)) return;

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        final int minY = chunk.getMinBuildHeight();
        final int maxY = chunk.getMaxBuildHeight();
        final int minX = chunk.getPos().getMinBlockX();
        final int minZ = chunk.getPos().getMinBlockZ();

        for (int lx = 0; lx < 16; lx++) {
            int worldX = minX + lx;
            for (int lz = 0; lz < 16; lz++) {
                int worldZ = minZ + lz;
                if (Math.abs(worldZ) >= BAND_HALF_WIDTH) continue; // don't touch walls/outside

                for (int y = minY; y < maxY; y++) {
                    pos.set(worldX, y, worldZ);
                    BlockState st = chunk.getBlockState(pos);

                    // Remove any water fluid blocks (source/flowing/bubble columns via fluid state)
                    if (st.getFluidState().is(FluidTags.WATER)) {
                        chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
                        continue;
                    }

                    // Clear waterlogged if present
                    if (st.hasProperty(BlockStateProperties.WATERLOGGED) && st.getValue(BlockStateProperties.WATERLOGGED)) {
                        chunk.setBlockState(pos, st.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), false);
                        continue;
                    }

                    // Replace water plants/columns explicitly just in case
                    if (st.is(Blocks.BUBBLE_COLUMN) || st.is(Blocks.KELP) || st.is(Blocks.KELP_PLANT)
                            || st.is(Blocks.SEAGRASS) || st.is(Blocks.TALL_SEAGRASS)) {
                        chunk.setBlockState(pos, Blocks.AIR.defaultBlockState(), false);
                    }
                }
            }
        }
    }
}


