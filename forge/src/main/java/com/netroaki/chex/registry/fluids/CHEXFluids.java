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

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, CHEX.MOD_ID);

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister
            .create(ForgeRegistries.Keys.FLUID_TYPES, CHEX.MOD_ID);

    // Minimal functional fluids so JEI and buckets/tooltips don't NPE

    public static final RegistryObject<FluidType> KEROSENE_FLUID_TYPE = FLUID_TYPES.register("kerosene",
            () -> new CHEXFluidType(FluidType.Properties.create()
                    .density(800)
                    .viscosity(2000)
                    .temperature(300)
                    .lightLevel(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .supportsBoating(false)));

    private static final ForgeFlowingFluid.Properties KEROSENE_PROPS = new ForgeFlowingFluid.Properties(
            KEROSENE_FLUID_TYPE,
            () -> CHEXFluids.KEROSENE.get(),
            () -> CHEXFluids.KEROSENE_FLOWING.get())
            .bucket(() -> CHEXItems.KEROSENE_BUCKET.get())
            .block(() -> CHEXBlocks.KEROSENE_FLUID_BLOCK.get());

    public static final RegistryObject<FlowingFluid> KEROSENE = FLUIDS.register("kerosene",
            () -> new CHEXFlowingFluid.Source(KEROSENE_PROPS));

    public static final RegistryObject<FlowingFluid> KEROSENE_FLOWING = FLUIDS.register("kerosene_flowing",
            () -> new CHEXFlowingFluid.Flowing(KEROSENE_PROPS));

    public static final RegistryObject<FluidType> RP1_FLUID_TYPE = FLUID_TYPES.register("rp1",
            () -> new CHEXFluidType(FluidType.Properties.create()
                    .density(820)
                    .viscosity(1800)
                    .temperature(280)
                    .lightLevel(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .supportsBoating(false)));

    private static final ForgeFlowingFluid.Properties RP1_PROPS = new ForgeFlowingFluid.Properties(
            RP1_FLUID_TYPE,
            () -> CHEXFluids.RP1.get(),
            () -> CHEXFluids.RP1_FLOWING.get())
            .bucket(() -> CHEXItems.RP1_BUCKET.get())
            .block(() -> CHEXBlocks.RP1_FLUID_BLOCK.get());

    public static final RegistryObject<FlowingFluid> RP1 = FLUIDS.register("rp1",
            () -> new CHEXFlowingFluid.Source(RP1_PROPS));

    public static final RegistryObject<FlowingFluid> RP1_FLOWING = FLUIDS.register("rp1_flowing",
            () -> new CHEXFlowingFluid.Flowing(RP1_PROPS));

    public static final RegistryObject<FluidType> LOX_FLUID_TYPE = FLUID_TYPES.register("lox",
            () -> new CHEXFluidType(FluidType.Properties.create()
                    .density(1140)
                    .viscosity(1000)
                    .temperature(90)
                    .lightLevel(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canExtinguish(true)
                    .canConvertToSource(false)
                    .supportsBoating(false)));

    private static final ForgeFlowingFluid.Properties LOX_PROPS = new ForgeFlowingFluid.Properties(
            LOX_FLUID_TYPE,
            () -> CHEXFluids.LOX.get(),
            () -> CHEXFluids.LOX_FLOWING.get())
            .bucket(() -> CHEXItems.LOX_BUCKET.get())
            .block(() -> CHEXBlocks.LOX_FLUID_BLOCK.get());

    public static final RegistryObject<FlowingFluid> LOX = FLUIDS.register("lox",
            () -> new CHEXFlowingFluid.Source(LOX_PROPS));

    public static final RegistryObject<FlowingFluid> LOX_FLOWING = FLUIDS.register("lox_flowing",
            () -> new CHEXFlowingFluid.Flowing(LOX_PROPS));

    public static final RegistryObject<FluidType> LH2_FLUID_TYPE = FLUID_TYPES.register("lh2",
            () -> new CHEXFluidType(FluidType.Properties.create()
                    .density(70)
                    .viscosity(200)
                    .temperature(20)
                    .lightLevel(0)
                    .canSwim(false)
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .supportsBoating(false)));

    private static final ForgeFlowingFluid.Properties LH2_PROPS = new ForgeFlowingFluid.Properties(
            LH2_FLUID_TYPE,
            () -> CHEXFluids.LH2.get(),
            () -> CHEXFluids.LH2_FLOWING.get())
            .bucket(() -> CHEXItems.LH2_BUCKET.get())
            .block(() -> CHEXBlocks.LH2_FLUID_BLOCK.get());

    public static final RegistryObject<FlowingFluid> LH2 = FLUIDS.register("lh2",
            () -> new CHEXFlowingFluid.Source(LH2_PROPS));

    public static final RegistryObject<FlowingFluid> LH2_FLOWING = FLUIDS.register("lh2_flowing",
            () -> new CHEXFlowingFluid.Flowing(LH2_PROPS));
}
