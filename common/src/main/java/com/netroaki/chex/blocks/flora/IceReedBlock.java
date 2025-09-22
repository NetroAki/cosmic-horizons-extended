package com.netroaki.chex.blocks.flora;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class IceReedBlock extends ArrakisPlantBlock {
    public static final int MAX_AGE = 3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),
        Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D),
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D),
        Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D)
    };
    
    public IceReedBlock(Properties properties) {
        super(properties, MAX_AGE);
    }
    
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
    
    @Override
    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.isMaxAge(state) && level.getDayTime() % 20 == 0 && random.nextFloat() < 0.2f) {
            double d0 = (double)pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
            double d1 = (double)pos.getY() + 0.8D;
            double d2 = (double)pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.5D;
            
            level.addParticle(ParticleTypes.FALLING_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            
            if (random.nextInt(10) == 0) {
                level.playLocalSound(d0, d1, d2, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }
    }
    
    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        
        // Grow faster at night or during rain
        float growthModifier = 1.0f;
        if (level.isNight() || level.isRainingAt(pos.above())) {
            growthModifier = 2.0f;
        }
        
        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float growthSpeed = getGrowthSpeed(this, level, pos) * growthModifier;
            if (ForgeHooks.onCropsGrowPre(level, pos, state, 
                random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                level.setBlock(pos, this.getStateForAge(age + 1), 2);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return (belowState.is(Blocks.SAND) || belowState.is(Blocks.RED_SAND) || 
                belowState.is(Blocks.DIRT) || belowState.is(Blocks.GRASS_BLOCK) ||
                belowState.is(Blocks.CLAY) || belowState.is(Blocks.TERRACOTTA)) &&
               (level.getBlockState(pos.above()).isAir() || level.getBlockState(pos.above()).is(this));
    }
    
    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        int age = Math.min(this.getAge(state) + 1, this.getMaxAge());
        level.setBlock(pos, this.getStateForAge(age), 2);
    }
    
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.SUGAR_CANE); // Temporary until we have our own item
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND) || 
               state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.CLAY) || state.is(Blocks.TERRACOTTA);
    }
}
