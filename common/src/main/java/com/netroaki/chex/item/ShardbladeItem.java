package com.netroaki.chex.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class ShardbladeItem extends SwordItem {
  private static final int FREEZE_DURATION = 100; // 5 seconds
  private static final float FREEZE_CHANCE = 0.3F; // 30% chance to freeze on hit

  public ShardbladeItem() {
    super(
        Tiers.DIAMOND,
        5,
        -2.4F,
        new Item.Properties()
            .durability(1561) // Same as diamond sword
            .rarity(Rarity.RARE));
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    // Apply freeze effect based on chance
    if (!attacker.level().isClientSide && attacker.getRandom().nextFloat() < FREEZE_CHANCE) {
      // Apply freeze effect
      target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, FREEZE_DURATION, 2));
      target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, FREEZE_DURATION, 1));

      // Create ice spikes around the target
      if (!target.level().isClientSide) {
        createIceSpikes(target.level(), target.getX(), target.getY(), target.getZ());
      }

      // Play sound
      attacker
          .level()
          .playSound(
              null,
              target.getX(),
              target.getY(),
              target.getZ(),
              net.minecraft.sounds.SoundEvents.GLASS_BREAK,
              target.getSoundSource(),
              1.0F,
              1.0F);
    }

    return super.hurtEnemy(stack, target, attacker);
  }

  private void createIceSpikes(Level level, double x, double y, double z) {
    // Create ice spikes in a circle around the target
    for (int i = 0; i < 8; i++) {
      double angle = i * Math.PI * 2.0 / 8.0;
      double offsetX = Math.cos(angle) * 2.0;
      double offsetZ = Math.sin(angle) * 2.0;

      // Find ground position
      net.minecraft.core.BlockPos pos =
          new net.minecraft.core.BlockPos((int) (x + offsetX), (int) y, (int) (z + offsetZ));

      // Create a spike of packed ice
      int height = 1 + level.random.nextInt(2);
      for (int j = 0; j < height; j++) {
        net.minecraft.core.BlockPos spikePos = pos.above(j);
        if (level.isEmptyBlock(spikePos)) {
          level.setBlockAndUpdate(
              spikePos, net.minecraft.world.level.block.Blocks.PACKED_ICE.defaultBlockState());
        }
      }
    }

    // Add particle effects
    for (int i = 0; i < 20; i++) {
      double offsetX = (level.random.nextDouble() - 0.5) * 4.0;
      double offsetZ = (level.random.nextDouble() - 0.5) * 4.0;
      level.addParticle(
          net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
          x + offsetX,
          y + 0.5,
          z + offsetZ,
          0,
          0.1,
          0);
    }
  }

  @Override
  public void inventoryTick(
      ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    super.inventoryTick(stack, level, entity, slotId, isSelected);

    // Add frosty particles when held
    if (isSelected && level.isClientSide && level.random.nextFloat() < 0.1F) {
      level.addParticle(
          net.minecraft.core.particles.ParticleTypes.SNOWFLAKE,
          entity.getX() + (level.random.nextDouble() - 0.5) * 0.5,
          entity.getY() + 1.0 + level.random.nextDouble() * 0.5,
          entity.getZ() + (level.random.nextDouble() - 0.5) * 0.5,
          0,
          0.05,
          0);
    }
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("item.chex.shardblade.desc"));
    tooltip.add(Component.translatable("item.chex.shardblade.effect", (int) (FREEZE_CHANCE * 100)));
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true; // Always has enchantment glint
  }
}
