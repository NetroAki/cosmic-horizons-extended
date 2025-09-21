package com.netroaki.chex.registry.fluids;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CHEXFlowingFluid extends ForgeFlowingFluid {

  public CHEXFlowingFluid(Properties properties) {
    super(properties);
  }

  @Override
  protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
    super.createFluidStateDefinition(builder);
    builder.add(LEVEL);
  }

  @Override
  public int getAmount(FluidState state) {
    return state.getValue(LEVEL);
  }

  @Override
  public boolean isSource(FluidState state) {
    return false;
  }

  public static class Source extends CHEXFlowingFluid {
    public Source(ForgeFlowingFluid.Properties properties) {
      super(properties);
    }

    @Override
    public int getAmount(FluidState state) {
      return 8;
    }

    @Override
    public boolean isSource(FluidState state) {
      return true;
    }
  }

  public static class Flowing extends CHEXFlowingFluid {
    public Flowing(ForgeFlowingFluid.Properties properties) {
      super(properties);
    }
  }
}
