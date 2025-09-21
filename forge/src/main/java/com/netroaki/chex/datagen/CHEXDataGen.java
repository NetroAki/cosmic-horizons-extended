package com.netroaki.chex.datagen;

import com.netroaki.chex.CHEX;
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
    boolean server = event.includeServer();

    if (server) {
      generator.addProvider(true, new MineralsDataProvider(packOutput));
    }
  }
}
