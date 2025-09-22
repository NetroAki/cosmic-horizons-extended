package com.netroaki.chex.blocks.flora;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DesertShrubBlock extends ArrakisPlantBlock {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final int SPREAD_CHANCE = 10;
    
    public DesertShrubBlock(Properties properties) {
        super(properties, 1); // Single growth stage
    }
    
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        
        // Chance to spread to adjacent sand blocks
        if (random.nextInt(SPREAD_CHANCE) == 0) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockPos spreadPos = pos.relative(direction);
            
            if (level.getBlockState(spreadPos).isAir() && canSurvive(state, level, spreadPos)) {
                level.setBlock(spreadPos, this.defaultBlockState(), 2);
            }
        }
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        // Can grow on sand, sandstone, or other desert blocks
        return belowState.is(BlockTags.SAND) || 
               belowState.is(Blocks.SANDSTONE) || 
               belowState.is(Blocks.RED_SANDSTONE) ||
               belowState.is(Blocks.TERRACOTTA) ||
               belowState.is(BlockTags.DIRT);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.SAND) || 
               state.is(Blocks.SANDSTONE) || 
               state.is(Blocks.RED_SANDSTONE) ||
               state.is(Blocks.TERRACOTTA) ||
               state.is(BlockTags.DIRT);
    }
    
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.DEAD_BUSH); // Temporary until we have our own item
    }
    
    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        // When bonemealed, try to spread in all directions
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos spreadPos = pos.relative(direction);
            if (level.getBlockState(spreadPos).isAir() && canSurvive(state, level, spreadPos)) {
                level.setBlock(spreadPos, this.defaultBlockState(), 2);
            }
        }
    }
}
