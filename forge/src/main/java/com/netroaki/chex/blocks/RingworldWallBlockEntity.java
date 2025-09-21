package com.netroaki.chex.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Block entity for ringworld wall blocks */
public class RingworldWallBlockEntity extends BlockEntity {

  private boolean isOuterWall = false;
  private int wallHeight = 64;

  public RingworldWallBlockEntity(BlockPos pos, BlockState state) {
    super(com.netroaki.chex.registry.CHEXBlockEntities.RINGWORLD_WALL.get(), pos, state);
  }

  public boolean isOuterWall() {
    return isOuterWall;
  }

  public void setOuterWall(boolean outerWall) {
    this.isOuterWall = outerWall;
    setChanged();
  }

  public int getWallHeight() {
    return wallHeight;
  }

  public void setWallHeight(int height) {
    this.wallHeight = height;
    setChanged();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean("is_outer_wall", isOuterWall);
    tag.putInt("wall_height", wallHeight);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    isOuterWall = tag.getBoolean("is_outer_wall");
    wallHeight = tag.getInt("wall_height");
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    saveAdditional(tag);
    return tag;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
