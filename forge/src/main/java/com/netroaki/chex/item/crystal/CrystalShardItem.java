package com.netroaki.chex.item.crystal;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CrystalShardItem extends Item {
  private final CrystalType type;

  public CrystalShardItem(Properties properties, CrystalType type) {
    super(properties);
    this.type = type;
  }

  public CrystalType getCrystalType() {
    return type;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(Component.translatable("tooltip.chex.crystal_shard." + type.name().toLowerCase()));

    if (type == CrystalType.ENERGIZED) {
      tooltip.add(
          Component.translatable("tooltip.chex.crystal_shard.energized.desc")
              .withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE));
    }
  }

  public enum CrystalType {
    COMMON,
    RARE,
    ENERGIZED
  }
}
