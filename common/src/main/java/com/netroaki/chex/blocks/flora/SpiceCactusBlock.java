package com.netroaki.chex.blocks.flora;

import com.netroaki.chex.registry.CHEXItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SpiceCactusBlock extends ArrakisPlantBlock {
    public static final int MAX_AGE = 3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
        Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
        Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
        Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D),
        Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)
    };
    
    public SpiceCactusBlock(Properties properties) {
        super(properties, MAX_AGE);
    }
    
    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
    
    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        
        // Grow taller if not at max height
        if (!level.getBlockState(pos.above()).isAir()) {
            return;
        }
        
        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float growthSpeed = getGrowthSpeed(this, level, pos);
            if (ForgeHooks.onCropsGrowPre(level, pos, state, 
                random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                
                level.setBlock(pos.above(), this.getStateForAge(0), 2);
                level.setBlock(pos, this.getStateForAge(age + 1), 4);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }
    
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && 
               (belowState.is(Blocks.SAND) || belowState.is(Blocks.RED_SAND) || 
                belowState.is(this) && belowState.getValue(AGE) == this.getMaxAge());
    }
    
    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
            if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003000000026077032D || d1 >= 0.003000000026077032D) {
                    entity.hurt(level.damageSources().cactus(), 1.0F);
                }
            }
        }
    }
    
    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        int age = Math.min(this.getAge(state) + 1, this.getMaxAge());
        level.setBlock(pos, this.getStateForAge(age), 2);
        
        // If at max age, try to grow upward
        if (age == this.getMaxAge() && level.getBlockState(pos.above()).isAir()) {
            level.setBlock(pos.above(), this.getStateForAge(0), 2);
        }
    }
    
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(CHEXItems.SPICE_MELANGE.get());
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND);
    }
}
