package com.netroaki.chex.item;

import com.netroaki.chex.CosmicHorizonsExpanded;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OceanCoreItem extends Item {
  private static final int DURABILITY = 1561; // Same as Netherite
  private static final int EFFECT_DURATION = 300; // 15 seconds
  private static final int EFFECT_AMPLIFIER = 1;

  public OceanCoreItem() {
    super(new Item.Properties().durability(DURABILITY).rarity(Rarity.EPIC).fireResistant());
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public int getEnchantmentValue() {
    return 15; // Same as Netherite
  }

  @Override
  public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
    return repair.is(Items.PRISMARINE_CRYSTALS) || super.isValidRepairItem(toRepair, repair);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);

    tooltip.add(
        Component.translatable("item." + CosmicHorizonsExpanded.MOD_ID + ".ocean_core.desc")
            .withStyle(ChatFormatting.DARK_AQUA));

    if (stack.isEnchanted()) {
      return;
    }

    tooltip.add(
        Component.translatable("item." + CosmicHorizonsExpanded.MOD_ID + ".ocean_core.effect")
            .withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true; // Always has the enchantment glint
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    // Apply effects on hit
    if (!target.level().isClientSide) {
      target.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, EFFECT_DURATION, EFFECT_AMPLIFIER));
      target.addEffect(
          new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));

      // Push target away
      Vec3 knockback = attacker.getLookAngle().scale(1.5).add(0, 0.5, 0);
      target.push(knockback.x, knockback.y, knockback.z);

      // Damage the item
      stack.hurtAndBreak(
          1, attacker, (entity) -> entity.broadcastBreakEvent(attacker.getUsedItemHand()));

      // Play sound
      target.playSound(
          ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.elder_guardian.hurt")),
          1.0F,
          1.0F);
    }

    return true;
  }

  @Override
  public void onCraftedBy(ItemStack stack, Level level, Player player) {
    // Add a random enchantment when crafted
    if (!level.isClientSide) {
      // 30% chance for Depth Strider III
      if (level.random.nextFloat() < 0.3F) {
        stack.enchant(
            ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("minecraft:depth_strider")),
            3);
      }
      // 20% chance for Impaling V
      else if (level.random.nextFloat() < 0.2F) {
        stack.enchant(
            ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("minecraft:impaling")), 5);
      }
    }
  }

  // Register the item in the mod's item registry
  public static final RegistryObject<OceanCoreItem> OCEAN_CORE =
      // TODO: Fix when RegistryObject.create method is available
      // RegistryObject.create(
      // new ResourceLocation(CosmicHorizonsExpanded.MOD_ID, "ocean_core"),
      // ForgeRegistries.ITEMS,
      // CosmicHorizonsExpanded.MOD_ID);
      null; // Placeholder
}
