package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public final class FuelRegistry {

    // Fuel mapping for rocket tiers
    private static final Map<Integer, String> TIER_TO_FUEL = new HashMap<>();
    private static final Map<Integer, Integer> TIER_TO_VOLUME = new HashMap<>();

    // Fallback fluids when GTCEu is not available
    public static final RegistryObject<ForgeFlowingFluid.Source> KEROSENE_SOURCE = CHEXRegistries.FLUIDS
            .register("kerosene_source", () -> new ForgeFlowingFluid.Source(keroseneProperties()));

    public static final RegistryObject<ForgeFlowingFluid.Flowing> KEROSENE_FLOWING = CHEXRegistries.FLUIDS
            .register("kerosene_flowing", () -> new ForgeFlowingFluid.Flowing(keroseneProperties()));

    public static final RegistryObject<FluidType> KEROSENE_TYPE = CHEXRegistries.FLUID_TYPES.register("kerosene",
            () -> new FluidType(FluidType.Properties.create()
                    .density(800)
                    .viscosity(1000)
                    .temperature(300)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .supportsBoating(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .fallDistanceModifier(0.0F)
                    .motionScale(0.0D)
                    .lightLevel(0)
                    .descriptionId("fluid.cosmic_horizons_extended.kerosene")));

    public static final RegistryObject<LiquidBlock> KEROSENE_BLOCK = CHEXRegistries.BLOCKS.register("kerosene",
            () -> new LiquidBlock(KEROSENE_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(100.0F)
                    .noLootTable()));

    public static final RegistryObject<Item> KEROSENE_BUCKET = CHEXRegistries.ITEMS.register("kerosene_bucket",
            () -> new BucketItem(KEROSENE_SOURCE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // RP-1 (Rocket Propellant 1)
    public static final RegistryObject<ForgeFlowingFluid.Source> RP1_SOURCE = CHEXRegistries.FLUIDS
            .register("rp1_source", () -> new ForgeFlowingFluid.Source(rp1Properties()));

    public static final RegistryObject<ForgeFlowingFluid.Flowing> RP1_FLOWING = CHEXRegistries.FLUIDS
            .register("rp1_flowing", () -> new ForgeFlowingFluid.Flowing(rp1Properties()));

    public static final RegistryObject<FluidType> RP1_TYPE = CHEXRegistries.FLUID_TYPES.register("rp1",
            () -> new FluidType(FluidType.Properties.create()
                    .density(820)
                    .viscosity(1200)
                    .temperature(280)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .supportsBoating(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .fallDistanceModifier(0.0F)
                    .motionScale(0.0D)
                    .lightLevel(0)
                    .descriptionId("fluid.cosmic_horizons_extended.rp1")));

    public static final RegistryObject<LiquidBlock> RP1_BLOCK = CHEXRegistries.BLOCKS.register("rp1",
            () -> new LiquidBlock(RP1_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(100.0F)
                    .noLootTable()));

    public static final RegistryObject<Item> RP1_BUCKET = CHEXRegistries.ITEMS.register("rp1_bucket",
            () -> new BucketItem(RP1_SOURCE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // LOX (Liquid Oxygen)
    public static final RegistryObject<ForgeFlowingFluid.Source> LOX_SOURCE = CHEXRegistries.FLUIDS
            .register("lox_source", () -> new ForgeFlowingFluid.Source(loxProperties()));

    public static final RegistryObject<ForgeFlowingFluid.Flowing> LOX_FLOWING = CHEXRegistries.FLUIDS
            .register("lox_flowing", () -> new ForgeFlowingFluid.Flowing(loxProperties()));

    public static final RegistryObject<FluidType> LOX_TYPE = CHEXRegistries.FLUID_TYPES.register("lox",
            () -> new FluidType(FluidType.Properties.create()
                    .density(1140)
                    .viscosity(200)
                    .temperature(90)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .supportsBoating(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .fallDistanceModifier(0.0F)
                    .motionScale(0.0D)
                    .lightLevel(0)
                    .descriptionId("fluid.cosmic_horizons_extended.lox")));

    public static final RegistryObject<LiquidBlock> LOX_BLOCK = CHEXRegistries.BLOCKS.register("lox",
            () -> new LiquidBlock(LOX_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(100.0F)
                    .noLootTable()));

    public static final RegistryObject<Item> LOX_BUCKET = CHEXRegistries.ITEMS.register("lox_bucket",
            () -> new BucketItem(LOX_SOURCE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // LH2 (Liquid Hydrogen)
    public static final RegistryObject<ForgeFlowingFluid.Source> LH2_SOURCE = CHEXRegistries.FLUIDS
            .register("lh2_source", () -> new ForgeFlowingFluid.Source(lh2Properties()));

    public static final RegistryObject<ForgeFlowingFluid.Flowing> LH2_FLOWING = CHEXRegistries.FLUIDS
            .register("lh2_flowing", () -> new ForgeFlowingFluid.Flowing(lh2Properties()));

    public static final RegistryObject<FluidType> LH2_TYPE = CHEXRegistries.FLUID_TYPES.register("lh2",
            () -> new FluidType(FluidType.Properties.create()
                    .density(70)
                    .viscosity(50)
                    .temperature(20)
                    .canSwim(false)
                    .canDrown(false)
                    .canPushEntity(false)
                    .canHydrate(false)
                    .supportsBoating(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .fallDistanceModifier(0.0F)
                    .motionScale(0.0D)
                    .lightLevel(0)
                    .descriptionId("fluid.cosmic_horizons_extended.lh2")));

    public static final RegistryObject<LiquidBlock> LH2_BLOCK = CHEXRegistries.BLOCKS.register("lh2",
            () -> new LiquidBlock(LH2_SOURCE, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN)
                    .strength(100.0F)
                    .noLootTable()));

    public static final RegistryObject<Item> LH2_BUCKET = CHEXRegistries.ITEMS.register("lh2_bucket",
            () -> new BucketItem(LH2_SOURCE,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    static {
        // Initialize fuel mappings
        TIER_TO_FUEL.put(1, "cosmic_horizons_extended:kerosene");
        TIER_TO_FUEL.put(2, "cosmic_horizons_extended:kerosene");
        TIER_TO_FUEL.put(3, "cosmic_horizons_extended:rp1");
        TIER_TO_FUEL.put(4, "cosmic_horizons_extended:rp1");
        TIER_TO_FUEL.put(5, "cosmic_horizons_extended:lox");
        TIER_TO_FUEL.put(6, "cosmic_horizons_extended:lh2");
        TIER_TO_FUEL.put(7, "cosmic_horizons_extended:lh2");
        TIER_TO_FUEL.put(8, "cosmic_horizons_extended:lh2");
        TIER_TO_FUEL.put(9, "cosmic_horizons_extended:lh2");
        TIER_TO_FUEL.put(10, "cosmic_horizons_extended:lh2");

        // Initialize volume requirements (in mB)
        TIER_TO_VOLUME.put(1, 1000);
        TIER_TO_VOLUME.put(2, 2000);
        TIER_TO_VOLUME.put(3, 3000);
        TIER_TO_VOLUME.put(4, 4000);
        TIER_TO_VOLUME.put(5, 5000);
        TIER_TO_VOLUME.put(6, 6000);
        TIER_TO_VOLUME.put(7, 7000);
        TIER_TO_VOLUME.put(8, 8000);
        TIER_TO_VOLUME.put(9, 9000);
        TIER_TO_VOLUME.put(10, 10000);
    }

    private static ForgeFlowingFluid.Properties keroseneProperties() {
        return new ForgeFlowingFluid.Properties(KEROSENE_TYPE, KEROSENE_SOURCE, KEROSENE_FLOWING)
                .block(KEROSENE_BLOCK)
                .bucket(KEROSENE_BUCKET);
    }

    private static ForgeFlowingFluid.Properties rp1Properties() {
        return new ForgeFlowingFluid.Properties(RP1_TYPE, RP1_SOURCE, RP1_FLOWING)
                .block(RP1_BLOCK)
                .bucket(RP1_BUCKET);
    }

    private static ForgeFlowingFluid.Properties loxProperties() {
        return new ForgeFlowingFluid.Properties(LOX_TYPE, LOX_SOURCE, LOX_FLOWING)
                .block(LOX_BLOCK)
                .bucket(LOX_BUCKET);
    }

    private static ForgeFlowingFluid.Properties lh2Properties() {
        return new ForgeFlowingFluid.Properties(LH2_TYPE, LH2_SOURCE, LH2_FLOWING)
                .block(LH2_BLOCK)
                .bucket(LH2_BUCKET);
    }

    public static String getFuelForTier(int tier) {
        return TIER_TO_FUEL.getOrDefault(tier, "cosmic_horizons_extended:kerosene");
    }

    public static int getVolumeForTier(int tier) {
        return TIER_TO_VOLUME.getOrDefault(tier, 1000);
    }

    public static boolean isValidFuelForTier(int tier, String fuelId) {
        String expectedFuel = getFuelForTier(tier);
        return expectedFuel.equals(fuelId);
    }

    public static ResourceLocation getFuelResourceLocation(int tier) {
        return ResourceLocation.fromNamespaceAndPath("cosmic_horizons_extended", getFuelForTier(tier).split(":")[1]);
    }

    private FuelRegistry() {
    }
}
