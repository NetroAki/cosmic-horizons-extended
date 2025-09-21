package com.netroaki.chex.registry;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.effects.RadiationEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CHEXEffects {
  public static final DeferredRegister<MobEffect> EFFECTS =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CHEX.MOD_ID);

  public static final RegistryObject<MobEffect> RADIATION =
      EFFECTS.register("radiation", RadiationEffect::new);

  private CHEXEffects() {}

  public static class SimpleEffect extends MobEffect {
    public SimpleEffect(MobEffectCategory category, int color) {
      super(category, color);
    }
  }
}
