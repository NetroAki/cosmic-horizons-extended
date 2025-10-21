package com.netroaki.chex.effect;

// import com.netroaki.chex.api.suit.ISuitTier; // TODO: Implement suit API
// import com.netroaki.chex.api.suit.SuitTier; // TODO: Implement suit API
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class RadiationEffect extends MobEffect {
  private static final String RADIATION_RESISTANCE_UUID = "10d03e72-2060-45f1-9a5c-3d3a4f2a1b0c";

  public RadiationEffect() {
    super(MobEffectCategory.HARMFUL, 0x7CFC00); // Light green color
    addAttributeModifier(
        Attributes.ARMOR, RADIATION_RESISTANCE_UUID, -2.0, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (entity instanceof Player player) {
      // TODO: Check suit tier for protection when suit API is implemented
      // ISuitTier suitTier = SuitTier.getSuitTier(player);
      // if (suitTier.getTier() >= 4) { // Suit IV or higher required
      // return; // No damage if wearing proper protection
      // }

      // Apply damage based on amplifier (flares increase this)
      float damage = 1.0f + (amplifier * 0.5f);
      player.hurt(player.damageSources().onFire(), damage);

      // Additional effects for visual feedback
      if (player.level().random.nextFloat() < 0.1f) {
        player.setSecondsOnFire(1);
      }
    }
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    // Apply effect every 20 ticks (1 second)
    return duration % 20 == 0;
  }
}
