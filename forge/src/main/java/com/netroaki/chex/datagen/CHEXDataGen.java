package com.netroaki.chex.datagen;

import com.netroaki.chex.CHEX;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CHEXDataGen {

  private CHEXDataGen() {}

  @SubscribeEvent
  public static void onGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    boolean server = event.includeServer();
    ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

    if (server) {
      // Generate mineral data
      generator.addProvider(true, new MineralsDataProvider(packOutput));
      
      // Generate Pandora features
      generator.addProvider(true, new PandoraFeatureProvider(packOutput, lookupProvider, existingFileHelper));
      
      // Generate loot tables for Pandora blocks
      generator.addProvider(true, new LootTableProvider(
          packOutput,
          Set.of(),
          List.of(
              new LootTableProvider.SubProviderEntry(
                  PandoraLootTableProvider::new,
                  LootContextParamSets.BLOCK
              )
          )
      ));
    }
  }
}
