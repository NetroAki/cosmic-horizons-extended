package com.netroaki.chex.item;

import com.netroaki.chex.effect.ModEffects;
import com.netroaki.chex.item.armor.AquaMundusArmorMaterial;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AquaMundusArmorItem extends ArmorItem {
  private static final int PRESSURE_RESISTANCE_DURATION = 200; // 10 seconds (in ticks)
  private static final int PRESSURE_RESISTANCE_AMPLIFIER = 0;

  public AquaMundusArmorItem(ArmorItem.Type type, Properties properties) {
    super(AquaMundusArmorMaterial.AQUA_MUNDUS, type, properties);
  }

  @Override
  public void onArmorTick(ItemStack stack, Level level, Player player) {
    if (!level.isClientSide()) {
      // Check if player is wearing a full set
      if (hasFullArmorSet(player)) {
        // Apply pressure resistance effect
        player.addEffect(
            new MobEffectInstance(
                ModEffects.PRESSURE_RESISTANCE.get(),
                PRESSURE_RESISTANCE_DURATION,
                PRESSURE_RESISTANCE_AMPLIFIER,
                false, // Don't show particles (we'll handle them in the effect class)
                false, // Don't show icon in HUD
                true // Show particles in first-person
                ));

        // Additional effects when in water
        if (player.isInWater()) {
          // Restore oxygen and food when in water
          if (player.tickCount % 10 == 0) {
            player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + 2));
            player.getFoodData().eat(1, 0.5f);
          }

          // Increase swim speed
          player.setDeltaMovement(player.getDeltaMovement().scale(1.1));

          // Reduce fall damage when entering water
          if (player.fallDistance > 3.0F && !player.onGround()) {
            player.fallDistance = 0.0F;
          }
        }

        // Night vision when fully submerged
        if (player.isUnderWater()) {
          player.addEffect(
              new MobEffectInstance(
                  net.minecraft.world.effect.MobEffects.NIGHT_VISION,
                  300, // 15 seconds
                  0,
                  false,
                  false,
                  false));
        }
      }
    }
  }

  private boolean hasFullArmorSet(Player player) {
    // Check if player is wearing all Aqua Mundus armor pieces
    return hasArmorPiece(player, EquipmentSlot.HEAD)
        && hasArmorPiece(player, EquipmentSlot.CHEST)
        && hasArmorPiece(player, EquipmentSlot.LEGS)
        && hasArmorPiece(player, EquipmentSlot.FEET);
  }

  private boolean hasArmorPiece(Player player, EquipmentSlot slot) {
    ItemStack armorStack = player.getItemBySlot(slot);
    return !armorStack.isEmpty() && armorStack.getItem() instanceof AquaMundusArmorItem;
  }
}
