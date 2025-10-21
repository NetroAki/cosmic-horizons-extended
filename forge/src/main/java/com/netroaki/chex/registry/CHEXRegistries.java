package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.registry.blocks.CHEXBlocks;
import com.netroaki.chex.registry.entities.CHEXEntities;
import com.netroaki.chex.registry.features.CHEXFeatures;
import com.netroaki.chex.registry.items.CHEXItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CHEXRegistries {

  // Deferred Registers - Creative tabs removed for now to avoid compilation
  // issues

  // Registry references for other classes to use
  public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS =
      com.netroaki.chex.registry.items.CHEXItems.ITEMS;
  public static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS =
      com.netroaki.chex.registry.blocks.CHEXBlocks.BLOCKS;
  // Fluids temporarily disabled pending stabilization
  public static final DeferredRegister<net.minecraft.world.level.material.Fluid> FLUIDS = null;
  public static final DeferredRegister<net.minecraftforge.fluids.FluidType> FLUID_TYPES = null;
  public static final DeferredRegister<net.minecraft.world.entity.EntityType<?>> ENTITY_TYPES =
      com.netroaki.chex.registry.entities.CHEXEntities.ENTITY_TYPES;
  public static final DeferredRegister<net.minecraft.world.level.levelgen.feature.Feature<?>>
      FEATURES = com.netroaki.chex.registry.features.CHEXFeatures.FEATURES;

  // Particle types
  public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>>
      PARTICLE_TYPES = com.netroaki.chex.particles.SpiceParticleType.PARTICLE_TYPES;

  public static void register(net.minecraftforge.eventbus.api.IEventBus eventBus) {
    CHEX.LOGGER.info("Registering CHEX registries...");

    // Register blocks first, then items, then fluids, then entities (order matters)
    CHEXBlocks.BLOCKS.register(eventBus);
    com.netroaki.chex.registry.CHEXBlockEntities.BLOCK_ENTITIES.register(eventBus);
    CHEXItems.ITEMS.register(eventBus);
    // Fluids disabled
    CHEXEntities.ENTITY_TYPES.register(eventBus);
    CHEXFeatures.FEATURES.register(eventBus);
    com.netroaki.chex.registry.sounds.CHEXSounds.SOUND_EVENTS.register(eventBus);

    // Register creative tabs
    CHEXCreativeTabs.register(eventBus);

    // Creative tabs registration removed for now

    CHEX.LOGGER.info("CHEX registries registered successfully");
  }

  @SubscribeEvent
  public static void buildContents(BuildCreativeModeTabContentsEvent event) {
    // Add our items to the appropriate creative tabs
    if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
      // Add building blocks
      event.accept(CHEXBlocks.ROCKET_ASSEMBLY_TABLE);
      event.accept(CHEXBlocks.FUEL_REFINERY);
    }

    if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
      // Add tools and utilities
      event.accept(CHEXItems.ROCKET_CONTROLLER);
      event.accept(CHEXItems.FUEL_GAUGE);
    }

    // Ingredients tab entries for fluid buckets disabled

    if (event.getTabKey() == CreativeModeTabs.COMBAT) {
      // Add space suits
      event.accept(CHEXItems.SPACE_SUIT_HELMET_T1);
      event.accept(CHEXItems.SPACE_SUIT_CHESTPLATE_T1);
      event.accept(CHEXItems.SPACE_SUIT_LEGGINGS_T1);
      event.accept(CHEXItems.SPACE_SUIT_BOOTS_T1);
    }
  }
}
