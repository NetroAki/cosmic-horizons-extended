package com.netroaki.chex.world.eden.features.flora;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EdenPlantBlock extends BushBlock implements BonemealableBlock {
  public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
  protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
  private final int growthRarity;

  public EdenPlantBlock(Properties properties, int growthRarity) {
    super(properties);
    this.growthRarity = growthRarity;
    this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
    return state.is(/* Your soil block here */ );
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return state.getValue(AGE) < 3;
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    int age = state.getValue(AGE);
    if (age < 3
        && level.getRawBrightness(pos.above(), 0) >= 9
        && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(
            level, pos, state, random.nextInt(growthRarity) == 0)) {
      level.setBlock(pos, state.setValue(AGE, age + 1), 2);
      net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
    }
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return state.getValue(AGE) < 3;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    int newAge = Math.min(3, state.getValue(AGE) + 1);
    level.setBlock(pos, state.setValue(AGE, newAge), 2);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }
}
