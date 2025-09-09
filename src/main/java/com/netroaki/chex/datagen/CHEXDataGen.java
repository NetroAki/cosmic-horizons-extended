package com.netroaki.chex.datagen;

import com.netroaki.chex.CHEX;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CHEXDataGen {
    private CHEXDataGen() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookup = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new CHEXWorldgenProvider(output, lookup));
    }
}
