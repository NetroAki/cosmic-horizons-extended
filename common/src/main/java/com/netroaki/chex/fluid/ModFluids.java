package com.netroaki.chex.fluid;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, CHEX.MOD_ID);

    // Luminous Kelp Fluid
    public static final RegistryObject<FlowingFluid> SOURCE_LUMINOUS_KELP = FLUIDS.register("luminous_kelp_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.LUMINOUS_KELP_PROPERTIES));
    
    public static final RegistryObject<FlowingFluid> FLOWING_LUMINOUS_KELP = FLUIDS.register("flowing_luminous_kelp",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.LUMINOUS_KELP_PROPERTIES));

    public static final ForgeFlowingFluid.Properties LUMINOUS_KELP_PROPERTIES = new ForgeFlowingFluid.Properties(
            SOURCE_LUMINOUS_KELP,
            FLOWING_LUMINOUS_KELP,
            FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
                    .density(1026)
                    .viscosity(1001)
                    .temperature(283)
                    .luminosity(10)
                    .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                    .overlay(WATER_OVERLAY_RL)
                    .color(0xAA00FFAA))
            .block(() -> com.netroaki.chex.block.AquaBlocks.LUMINOUS_KELP_FLUID.get())
            .bucket(() -> com.netroaki.chex.item.ModItems.LUMINOUS_KELP_BUCKET.get())
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .tickRate(10);

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
