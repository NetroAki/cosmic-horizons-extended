package com.netroaki.chex.item.armor;

import com.netroaki.chex.item.material.CrystalArmorMaterial;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class CrystalArmorItem extends ArmorItem {
  private final CrystalArmorMaterial material;

  public CrystalArmorItem(
      CrystalArmorMaterial material, ArmorItem.Type type, Properties properties) {
    super(material, type, properties);
    this.material = material;
  }

  @Override
  public void onArmorTick(ItemStack stack, Level level, Player player) {
    if (!level.isClientSide) {
      // Apply effects based on full set
      if (hasFullSet(player)) {
        switch (material) {
          case CRYSTAL -> {
            // Basic crystal set bonus: Night Vision and Water Breathing
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.NIGHT_VISION,
                    220, // 11 seconds
                    0,
                    false,
                    false,
                    true));
            player.addEffect(
                new MobEffectInstance(MobEffects.WATER_BREATHING, 220, 0, false, false, true));
          }
          case RARE_CRYSTAL -> {
            // Rare crystal set bonus: Regeneration and Resistance
            player.addEffect(
                new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false, true));
            player.addEffect(
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, false, false, true));
          }
          case ENERGIZED_CRYSTAL -> {
            // Energized crystal set bonus: Strength, Speed, and Fire Resistance
            player.addEffect(
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, true));
            player.addEffect(
                new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, false, false, true));
            player.addEffect(
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, false, false, true));

            // Special ability: Aura that damages nearby enemies
            if (player.tickCount % 40 == 0) { // Every 2 seconds
              AABB area = player.getBoundingBox().inflate(5.0D);
              level.getEntitiesOfClass(LivingEntity.class, area).stream()
                  .filter(e -> e != player && e.isAlive() && !e.isAlliedTo(player))
                  .forEach(e -> e.hurt(level.damageSources().magic(), 2.0F));
            }
          }
        }
      }
    }
  }

  @Override
  public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
    return material == CrystalArmorMaterial.ENERGIZED_CRYSTAL;
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
    return true;
  }

  @Override
  public boolean canBeDepleted() {
    return material.getDurabilityForType(null) > 0;
  }

  private boolean hasFullSet(Player player) {
    return hasArmor(player, EquipmentSlot.HEAD)
        && hasArmor(player, EquipmentSlot.CHEST)
        && hasArmor(player, EquipmentSlot.LEGS)
        && hasArmor(player, EquipmentSlot.FEET);
  }

  private boolean hasArmor(Player player, EquipmentSlot slot) {
    ItemStack stack = player.getItemBySlot(slot);
    return !stack.isEmpty()
        && stack.getItem() instanceof CrystalArmorItem crystalArmor
        && crystalArmor.material == this.material;
  }
}
