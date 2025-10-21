package com.netroaki.chex.client;

import com.netroaki.chex.registry.FuelRegistry;
import com.netroaki.chex.suits.SuitItems;
import com.netroaki.chex.suits.SuitTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipHandler {

  @SubscribeEvent
  public static void onItemTooltip(ItemTooltipEvent event) {
    ItemStack stack = event.getItemStack();

    // Suit items: show tier and hazard summary
    if (stack.getItem() instanceof SuitItems.SuitItem suit) {
      SuitTiers tier = suit.getSuitTier();
      event
          .getToolTip()
          .add(
              Component.translatable("chex.tooltip.suit.tier", tier.getTier())
                  .withStyle(ChatFormatting.AQUA));
      event
          .getToolTip()
          .add(Component.translatable("chex.tooltip.suit.hazards").withStyle(ChatFormatting.GRAY));
      return;
    }

    // Fuel buckets: show fuel name and typical usage volume for mid-tier launches
    String path = stack.getItem().getDescriptionId();
    if (path.contains("kerosene_bucket")
        || path.contains("rp1_bucket")
        || path.contains("lox_bucket")
        || path.contains("lh2_bucket")) {
      event
          .getToolTip()
          .add(
              Component.translatable("chex.tooltip.fuel_bucket.info")
                  .withStyle(ChatFormatting.GRAY));
      // Example: show a baseline consumption for T3
      FuelRegistry.FuelRequirement req = FuelRegistry.getFuelRequirement(3).orElse(null);
      if (req != null) {
        int mB = FuelRegistry.getFuelVolume(3);
        event
            .getToolTip()
            .add(
                Component.translatable(
                        "chex.tooltip.fuel_bucket.volume", req.getFluidId().toString(), mB)
                    .withStyle(ChatFormatting.DARK_GRAY));
      }
      return;
    }

    // Rocket controller item if present
    if (stack.getDescriptionId().contains("rocket_controller")) {
      event
          .getToolTip()
          .add(
              Component.translatable("chex.tooltip.rocket_controller.summary")
                  .withStyle(ChatFormatting.AQUA));
      event
          .getToolTip()
          .add(
              Component.translatable("chex.tooltip.rocket_controller.hint")
                  .withStyle(ChatFormatting.GRAY));
    }
  }

  @SubscribeEvent
  public static void onTooltipColor(RenderTooltipEvent.Color event) {
    // Minor UX tweak: keep default
  }
}
