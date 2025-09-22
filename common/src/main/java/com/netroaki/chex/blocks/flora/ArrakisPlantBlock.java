package com.netroaki.chex.blocks.flora;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public abstract class ArrakisPlantBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private final int maxAge;
    
    public ArrakisPlantBlock(Properties properties, int maxAge) {
        super(properties);
        this.maxAge = maxAge;
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(this.getAgeProperty(), 0));
    }
    
    protected abstract boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos);
    
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }
    
    public int getMaxAge() {
        return maxAge;
    }
    
    protected int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }
    
    public boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }
    
    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        if (!level.isAreaLoaded(pos, 1)) return;
        
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, pos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, 
                    random.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                    ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }
    
    protected float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        return 1.0F; // Can be overridden by specific plants
    }
    
    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Math.min(age, this.getMaxAge()));
    }
    
    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }
    
    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
