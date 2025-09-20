package com.netroaki.chex.client.tooltips;

import com.netroaki.chex.CHEX;
import com.netroaki.chex.suits.SuitTiers;
import com.netroaki.chex.suits.SuitItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Manages custom tooltips for CHEX items
 */
@Mod.EventBusSubscriber(modid = CHEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CHEXTooltipManager {

    @SubscribeEvent
    public static void onTooltipRender(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();

        // Add suit tooltips
        if (stack.getItem() instanceof SuitItems.SuitItem suitItem) {
            SuitTiers suitTier = suitItem.getSuitTier();
            int protection = suitTier.getTier() * 20; // Simple protection calculation
            var hazards = suitTier.getProtectedHazards();

            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal("§6=== SUIT INFORMATION ==="));
            tooltip.add(Component.literal("§eTier: §f" + suitTier.getDisplayName()));
            tooltip.add(Component.literal("§aProtection: §f" + protection + "%"));
            tooltip.add(Component.literal("§6Protected Hazards:"));

            for (var hazard : hazards) {
                tooltip.add(Component.literal("§7• " + hazard.name()));
            }
        }

        // Add rocket tooltips (placeholder - would need rocket items)
        // This would be implemented when rocket items are created
    }
}
