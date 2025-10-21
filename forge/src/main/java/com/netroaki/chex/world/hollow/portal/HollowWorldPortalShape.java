package com.netroaki.chex.world.hollow.portal;

import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class HollowWorldPortalShape {
  private static final int MIN_WIDTH = 2;
  private static final int MAX_WIDTH = 21;
  private static final int MIN_HEIGHT = 3;
  private static final int MAX_HEIGHT = 21;

  private final LevelAccessor level;
  private final Direction.Axis axis;
  private final Direction rightDir;
  private int numPortalBlocks;
  @Nullable private BlockPos bottomLeft;
  private int height;
  private int width;

  public static Optional<HollowWorldPortalShape> findEmptyPortalShape(
      LevelAccessor level, BlockPos pos) {
    return findPortalShape(level, pos, (shape) -> shape.isValid() && shape.numPortalBlocks == 0);
  }

  public static Optional<HollowWorldPortalShape> findPortalShape(
      LevelAccessor level, BlockPos pos, Predicate<HollowWorldPortalShape> validator) {
    Optional<HollowWorldPortalShape> optional =
        Optional.of(new HollowWorldPortalShape(level, pos, Direction.Axis.X)).filter(validator);
    if (optional.isPresent()) {
      return optional;
    } else {
      return Optional.of(new HollowWorldPortalShape(level, pos, Direction.Axis.Z))
          .filter(validator);
    }
  }

  public HollowWorldPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
    this.level = level;
    this.axis = axis;
    this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
    this.bottomLeft = this.calculateBottomLeft(pos);

    if (this.bottomLeft == null) {
      this.bottomLeft = pos;
      this.width = 1;
      this.height = 1;
    } else {
      this.width = this.calculateWidth();
      if (this.width > 0) {
        this.height = this.calculateHeight();
      }
    }
  }

  @Nullable
  private BlockPos calculateBottomLeft(BlockPos pos) {
    for (int i = Math.max(0, pos.getY() - 21);
        pos.getY() > i && isEmpty(level.getBlockState(pos.below()));
        pos = pos.below()) {}

    Direction direction = this.rightDir.getOpposite();
    int j = this.getDistanceUntilEdge(pos, direction) - 1;
    return j < 0 ? null : pos.relative(direction, j);
  }

  private int calculateWidth() {
    int i = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
    return i >= 2 && i <= 21 ? i : 0;
  }

  private int getDistanceUntilEdge(BlockPos pos, Direction direction) {
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    for (int i = 0; i <= 21; ++i) {
      mutablePos.set(pos).move(direction, i);
      BlockState blockstate = this.level.getBlockState(mutablePos);
      if (!isEmpty(blockstate)) {
        if (isFrame(blockstate)) {
          return i;
        }
        break;
      }

      BlockState blockstate1 = this.level.getBlockState(mutablePos.move(Direction.DOWN));
      if (!isFrame(blockstate1)) {
        break;
      }
    }

    return 0;
  }

  private int calculateHeight() {
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
    int i = this.getDistanceUntilTop(mutablePos);
    return i >= 3 && i <= 21 && this.hasTopFrame(mutablePos, i) ? i : 0;
  }

  private boolean hasTopFrame(BlockPos.MutableBlockPos pos, int height) {
    for (int i = 0; i < this.width; ++i) {
      BlockPos.MutableBlockPos mutablePos =
          pos.set(this.bottomLeft).move(Direction.UP, height).move(this.rightDir, i);

      if (!isFrame(this.level.getBlockState(mutablePos))) {
        return false;
      }
    }
    return true;
  }

  private int getDistanceUntilTop(BlockPos.MutableBlockPos pos) {
    for (int i = 0; i < 21; ++i) {
      pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
      if (!isFrame(this.level.getBlockState(pos))) {
        return i;
      }

      pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
      if (!isFrame(this.level.getBlockState(pos))) {
        return i;
      }

      for (int j = 0; j < this.width; ++j) {
        pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
        BlockState blockstate = this.level.getBlockState(pos);
        if (!isEmpty(blockstate)) {
          return i;
        }

        if (blockstate.is(Blocks.PORTAL)) {
          ++this.numPortalBlocks;
        }
      }
    }

    return 21;
  }

  private static boolean isFrame(BlockState state) {
    return state.is(BlockTags.FIRE)
        || state.is(Blocks.OBSIDIAN)
        || state.is(Blocks.CRYING_OBSIDIAN);
  }

  public static boolean isEmpty(BlockState state) {
    return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.PORTAL);
  }

  public boolean isValid() {
    return this.bottomLeft != null
        && this.width >= 2
        && this.width <= 21
        && this.height >= 3
        && this.height <= 21;
  }

  public void createPortal() {
    BlockState portalState =
        HollowWorldPortalBlock.portal()
            .defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_AXIS, this.axis);

    BlockPos.betweenClosed(
            this.bottomLeft,
            this.bottomLeft
                .offset(Direction.UP, this.height - 1)
                .offset(this.rightDir, this.width - 1))
        .forEach(
            pos -> {
              this.level.setBlock(pos, portalState, 18);
            });
  }

  public boolean isComplete() {
    return this.isValid() && this.numPortalBlocks == this.width * this.height;
  }
}
