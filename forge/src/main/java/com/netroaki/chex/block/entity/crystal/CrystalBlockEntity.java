package com.netroaki.chex.block.entity.crystal;

import com.netroaki.chex.block.crystal.CrystalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalBlockEntity extends BlockEntity {
  private int growthTicks = 0;
  private int growthStage = 0;
  private static final int TICKS_PER_GROWTH_STAGE = 24000; // 20 minutes per stage

  public CrystalBlockEntity(BlockPos pos, BlockState state) {
    // super(CHEXBlockEntities.CRYSTAL_BLOCK.get(), pos, state); // TODO: Fix when
    // CHEXBlockEntities is implemented
    super(null, pos, state); // Placeholder
  }

  public void tick() {
    if (this.level == null || this.level.isClientSide) {
      return;
    }

    // Only tick every 10 seconds to reduce server load
    if (this.level.getGameTime() % 200 != 0) {
      return;
    }

    BlockState state = this.getBlockState();
    if (!(state.getBlock() instanceof CrystalBlock)) {
      return;
    }

    // Increment growth timer
    this.growthTicks += 200; // Add 10 seconds worth of ticks

    // Check if ready to grow to next stage
    if (this.growthTicks >= TICKS_PER_GROWTH_STAGE) {
      this.growthTicks = 0;
      this.growthStage++;

      // Update block state if needed
      CrystalBlock.CrystalVariant currentVariant = state.getValue(CrystalBlock.VARIANT);
      if (currentVariant.ordinal() < CrystalBlock.CrystalVariant.values().length - 1) {
        BlockState newState =
            state.setValue(
                CrystalBlock.VARIANT,
                CrystalBlock.CrystalVariant.values()[currentVariant.ordinal() + 1]);
        this.level.setBlockAndUpdate(this.worldPosition, newState);
      }

      this.setChanged();
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("GrowthTicks", this.growthTicks);
    tag.putInt("GrowthStage", this.growthStage);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.growthTicks = tag.getInt("GrowthTicks");
    this.growthStage = tag.getInt("GrowthStage");
  }

  public int getGrowthStage() {
    return this.growthStage;
  }

  public float getGrowthProgress() {
    return (float) this.growthTicks / TICKS_PER_GROWTH_STAGE;
  }
}
