package com.netroaki.chex.world.hollow.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HollowWorldPortalFrameBlock extends HorizontalDirectionalBlock {
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
  private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

  public HollowWorldPortalFrameBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, false));
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return Shapes.block();
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    ItemStack itemstack = player.getItemInHand(hand);

    if (!level.isClientSide && itemstack.is(Items.FLINT_AND_STEEL) && !state.getValue(ACTIVATED)) {
      level.setBlock(pos, state.setValue(ACTIVATED, true), 3);
      level.playSound(
          null,
          pos,
          SoundEvents.FLINTANDSTEEL_USE,
          SoundSource.BLOCKS,
          1.0F,
          level.getRandom().nextFloat() * 0.4F + 0.8F);

      if (!player.isCreative()) {
        itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
      }

      // Check if portal can be formed
      HollowWorldPortalShape.findEmptyPortalShape(level, pos)
          .ifPresent(
              shape -> {
                shape.createPortal();
              });

      return InteractionResult.SUCCESS;
    }

    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, ACTIVATED);
  }
}
