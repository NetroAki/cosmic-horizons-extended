package com.netroaki.chex.worldgen;

import com.netroaki.chex.CHEX;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CHEXBiomeModifiers {
  private CHEXBiomeModifiers() {}

  private static final DeferredRegister<BiomeModifier> BIOME_MODIFIERS =
      DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIERS, CHEX.MOD_ID);

  private static final DeferredRegister<com.mojang.serialization.Codec<? extends BiomeModifier>>
      SERIALIZERS =
          DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, CHEX.MOD_ID);

  public static final RegistryObject<com.mojang.serialization.Codec<MineralBiomeModifier>>
      MINERAL_CODEC = SERIALIZERS.register("mineral_modifier", () -> MineralBiomeModifier.CODEC);

  public static final RegistryObject<BiomeModifier> MINERAL_MODIFIER =
      BIOME_MODIFIERS.register("dynamic_minerals", MineralBiomeModifier::new);

  public static void register(IEventBus bus) {
    SERIALIZERS.register(bus);
    BIOME_MODIFIERS.register(bus);
  }
}
