package com.netroaki.chex.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

public class MonarchCoreItem extends Item implements IForgeItem {

  public MonarchCoreItem() {
    super(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC).fireResistant());
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("item.chex.monarch_core.desc"));
    tooltip.add(Component.translatable("item.chex.monarch_core.crafting"));
  }

  @Override
  public boolean hasCustomEntity(ItemStack stack) {
    return true; // Prevent destruction in fire/lava
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true; // Always has enchantment glint
  }

  @Override
  public void inventoryTick(
      ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
    super.inventoryTick(stack, level, entity, slotId, isSelected);

    // Add subtle particle effect
    if (level.isClientSide && level.random.nextFloat() < 0.05F) {
      double x = entity.getX() + (level.random.nextDouble() - 0.5) * 0.5;
      double y = entity.getY() + level.random.nextDouble() * 2.0;
      double z = entity.getZ() + (level.random.nextDouble() - 0.5) * 0.5;

      level.addParticle(net.minecraft.core.particles.ParticleTypes.ENCHANT, x, y, z, 0, 0.1, 0);
    }
  }
}
