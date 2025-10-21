package com.netroaki.chex.world.aqua_mundus;

import com.netroaki.chex.world.aqua_mundus.mechanics.AquaMundusConfig;
import com.netroaki.chex.world.aqua_mundus.mechanics.AquaMundusMechanics;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/** Handles registration and setup of Aqua Mundus mechanics. */
public class AquaMundusMechanicsSetup {

  /** Registers all Aqua Mundus mechanics. */
  public static void register() {
    // Register configuration
    ModLoadingContext.get()
        .registerConfig(
            ModConfig.Type.COMMON,
            AquaMundusConfig.SPEC,
            "cosmichorizonsexpanded-aqua_mundus.toml");

    // Register event handlers
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    // Register event listeners
    modEventBus.addListener(AquaMundusMechanicsSetup::onCommonSetup);

    // Register our event handler class
    forgeEventBus.register(AquaMundusMechanics.class);
  }

  /** Handles common setup for Aqua Mundus mechanics. */
  private static void onCommonSetup(FMLCommonSetupEvent event) {
    // Any common setup code can go here
    event.enqueueWork(
        () -> {
          // Setup code that needs to run on the main thread
          // For example, registering capabilities or syncing data
        });
  }
}
