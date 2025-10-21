package com.netroaki.chex.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class FrozenHeartItem extends Item {
  private static final int COOLDOWN_TICKS = 200; // 10 seconds cooldown
  private static final int EFFECT_RADIUS = 8;
  private static final int EFFECT_DURATION = 200; // 10 seconds

  public FrozenHeartItem(Properties properties) {
    super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 40; // 2 seconds to charge up
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    if (!level.isClientSide && entity instanceof Player player) {
      // Check cooldown
      if (player.getCooldowns().isOnCooldown(this)) {
        return stack;
      }

      // Apply effects to nearby entities
      AABB area = player.getBoundingBox().inflate(EFFECT_RADIUS);
      List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

      for (LivingEntity target : entities) {
        if (target != player) {
          // Freeze enemies
          target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, 2));
          target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, EFFECT_DURATION, 2));

          // Damage mobs
          if (target.isInvertedHealAndHarm()) {
            target.hurt(level.damageSources().magic(), 4.0F);
          }
        } else {
          // Buff the player
          player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, 1));
          player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION / 2, 1));
        }
      }

      // Visual effects
      for (int i = 0; i < 50; i++) {
        double angle = level.random.nextDouble() * Math.PI * 2.0;
        double dist = level.random.nextDouble() * EFFECT_RADIUS;
        double x = player.getX() + Math.cos(angle) * dist;
        double z = player.getZ() + Math.sin(angle) * dist;

        level.addParticle(
            net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
            x,
            player.getY() + 1.0,
            z,
            0,
            0.1,
            0);
      }

      // Play sound
      level.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          net.minecraft.sounds.SoundEvents.GLASS_BREAK,
          player.getSoundSource(),
          1.0F,
          0.5F);

      // Set cooldown
      player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);

      // Damage the item (if not in creative)
      if (!player.getAbilities().instabuild) {
        stack.hurt(1, level.random, null);
      }
    }

    return stack;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("item.chex.frozen_heart.desc"));
  }
}
