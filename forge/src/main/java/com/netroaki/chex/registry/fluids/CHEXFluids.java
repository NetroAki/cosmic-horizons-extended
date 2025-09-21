package com.netroaki.chex.registry.fluids;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.registry.items.CHEXItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEXFluids {

  public static final DeferredRegister<Fluid> FLUIDS =
      DeferredRegister.create(ForgeRegistries.FLUIDS, CHEX.MOD_ID);

  public static final DeferredRegister<FluidType> FLUID_TYPES =
      DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CHEX.MOD_ID);

  // Minimal functional fluids so JEI and buckets/tooltips don't NPE

  public static final RegistryObject<FluidType> KEROSENE_FLUID_TYPE =
      FLUID_TYPES.register(
          "kerosene",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(800)
                      .viscosity(2000)
                      .temperature(300)
                      .lightLevel(0)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties KEROSENE_PROPS =
      new ForgeFlowingFluid.Properties(
              KEROSENE_FLUID_TYPE,
              () -> CHEXFluids.KEROSENE.get(),
              () -> CHEXFluids.KEROSENE_FLOWING.get())
          .bucket(() -> CHEXItems.KEROSENE_BUCKET.get())
          .block(() -> CHEXBlocks.KEROSENE_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> KEROSENE =
      FLUIDS.register("kerosene", () -> new CHEXFlowingFluid.Source(KEROSENE_PROPS));

  public static final RegistryObject<FlowingFluid> KEROSENE_FLOWING =
      FLUIDS.register("kerosene_flowing", () -> new CHEXFlowingFluid.Flowing(KEROSENE_PROPS));

  public static final RegistryObject<FluidType> RP1_FLUID_TYPE =
      FLUID_TYPES.register(
          "rp1",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(820)
                      .viscosity(1800)
                      .temperature(280)
                      .lightLevel(0)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties RP1_PROPS =
      new ForgeFlowingFluid.Properties(
              RP1_FLUID_TYPE, () -> CHEXFluids.RP1.get(), () -> CHEXFluids.RP1_FLOWING.get())
          .bucket(() -> CHEXItems.RP1_BUCKET.get())
          .block(() -> CHEXBlocks.RP1_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> RP1 =
      FLUIDS.register("rp1", () -> new CHEXFlowingFluid.Source(RP1_PROPS));

  public static final RegistryObject<FlowingFluid> RP1_FLOWING =
      FLUIDS.register("rp1_flowing", () -> new CHEXFlowingFluid.Flowing(RP1_PROPS));

  public static final RegistryObject<FluidType> LOX_FLUID_TYPE =
      FLUID_TYPES.register(
          "lox",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(1140)
                      .viscosity(1000)
                      .temperature(90)
                      .lightLevel(0)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(true)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties LOX_PROPS =
      new ForgeFlowingFluid.Properties(
              LOX_FLUID_TYPE, () -> CHEXFluids.LOX.get(), () -> CHEXFluids.LOX_FLOWING.get())
          .bucket(() -> CHEXItems.LOX_BUCKET.get())
          .block(() -> CHEXBlocks.LOX_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> LOX =
      FLUIDS.register("lox", () -> new CHEXFlowingFluid.Source(LOX_PROPS));

  public static final RegistryObject<FlowingFluid> LOX_FLOWING =
      FLUIDS.register("lox_flowing", () -> new CHEXFlowingFluid.Flowing(LOX_PROPS));

  public static final RegistryObject<FluidType> LH2_FLUID_TYPE =
      FLUID_TYPES.register(
          "lh2",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(70)
                      .viscosity(200)
                      .temperature(20)
                      .lightLevel(0)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties LH2_PROPS =
      new ForgeFlowingFluid.Properties(
              LH2_FLUID_TYPE, () -> CHEXFluids.LH2.get(), () -> CHEXFluids.LH2_FLOWING.get())
          .bucket(() -> CHEXItems.LH2_BUCKET.get())
          .block(() -> CHEXBlocks.LH2_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> LH2 =
      FLUIDS.register("lh2", () -> new CHEXFlowingFluid.Source(LH2_PROPS));

  public static final RegistryObject<FlowingFluid> LH2_FLOWING =
      FLUIDS.register("lh2_flowing", () -> new CHEXFlowingFluid.Flowing(LH2_PROPS));

  // Additional fallback fluids for higher tiers

  public static final RegistryObject<FluidType> DT_MIX_FLUID_TYPE =
      FLUID_TYPES.register(
          "dt_mix",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(150)
                      .viscosity(100)
                      .temperature(15)
                      .lightLevel(2)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties DT_MIX_PROPS =
      new ForgeFlowingFluid.Properties(
              DT_MIX_FLUID_TYPE,
              () -> CHEXFluids.DT_MIX.get(),
              () -> CHEXFluids.DT_MIX_FLOWING.get())
          .bucket(() -> CHEXItems.DT_MIX_BUCKET.get())
          .block(() -> CHEXBlocks.DT_MIX_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> DT_MIX =
      FLUIDS.register("dt_mix", () -> new CHEXFlowingFluid.Source(DT_MIX_PROPS));

  public static final RegistryObject<FlowingFluid> DT_MIX_FLOWING =
      FLUIDS.register("dt_mix_flowing", () -> new CHEXFlowingFluid.Flowing(DT_MIX_PROPS));

  public static final RegistryObject<FluidType> HE3_BLEND_FLUID_TYPE =
      FLUID_TYPES.register(
          "he3_blend",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(120)
                      .viscosity(80)
                      .temperature(5)
                      .lightLevel(3)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties HE3_BLEND_PROPS =
      new ForgeFlowingFluid.Properties(
              HE3_BLEND_FLUID_TYPE,
              () -> CHEXFluids.HE3_BLEND.get(),
              () -> CHEXFluids.HE3_BLEND_FLOWING.get())
          .bucket(() -> CHEXItems.HE3_BLEND_BUCKET.get())
          .block(() -> CHEXBlocks.HE3_BLEND_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> HE3_BLEND =
      FLUIDS.register("he3_blend", () -> new CHEXFlowingFluid.Source(HE3_BLEND_PROPS));

  public static final RegistryObject<FlowingFluid> HE3_BLEND_FLOWING =
      FLUIDS.register("he3_blend_flowing", () -> new CHEXFlowingFluid.Flowing(HE3_BLEND_PROPS));

  public static final RegistryObject<FluidType> EXOTIC_MIX_FLUID_TYPE =
      FLUID_TYPES.register(
          "exotic_mix",
          () ->
              new CHEXFluidType(
                  FluidType.Properties.create()
                      .density(2000)
                      .viscosity(5000)
                      .temperature(1000)
                      .lightLevel(15)
                      .canSwim(false)
                      .canDrown(false)
                      .canExtinguish(false)
                      .canConvertToSource(false)
                      .supportsBoating(false)));

  private static final ForgeFlowingFluid.Properties EXOTIC_MIX_PROPS =
      new ForgeFlowingFluid.Properties(
              EXOTIC_MIX_FLUID_TYPE,
              () -> CHEXFluids.EXOTIC_MIX.get(),
              () -> CHEXFluids.EXOTIC_MIX_FLOWING.get())
          .bucket(() -> CHEXItems.EXOTIC_MIX_BUCKET.get())
          .block(() -> CHEXBlocks.EXOTIC_MIX_FLUID_BLOCK.get());

  public static final RegistryObject<FlowingFluid> EXOTIC_MIX =
      FLUIDS.register("exotic_mix", () -> new CHEXFlowingFluid.Source(EXOTIC_MIX_PROPS));

  public static final RegistryObject<FlowingFluid> EXOTIC_MIX_FLOWING =
      FLUIDS.register("exotic_mix_flowing", () -> new CHEXFlowingFluid.Flowing(EXOTIC_MIX_PROPS));
}
