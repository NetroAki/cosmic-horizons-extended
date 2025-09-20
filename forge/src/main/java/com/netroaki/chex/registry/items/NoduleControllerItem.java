package com.netroaki.chex.registry.items;

import com.netroaki.chex.capabilities.PlayerTierCapability;
import com.netroaki.chex.config.CHEXConfig;
import com.netroaki.chex.registry.NoduleTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class NoduleControllerItem extends Item {
    public NoduleControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        final int[] tierBox = new int[] { 0 };
        if (level != null && level.isClientSide && net.minecraft.client.Minecraft.getInstance().player != null) {
            net.minecraft.client.Minecraft.getInstance().player.getCapability(PlayerTierCapability.INSTANCE)
                    .ifPresent(cap -> {
                        tierBox[0] = cap.getRocketTier();
                        tooltip.add(Component.literal("Nodule Tier: T" + tierBox[0])
                                .withStyle(ChatFormatting.GOLD));
                    });
        } else {
            tooltip.add(Component.literal("Nodule Tier: (unknown)").withStyle(ChatFormatting.DARK_GRAY));
        }

        // Show recommended fuel for each tier succinctly
        tooltip.add(Component.literal("Fuel Mapping:").withStyle(ChatFormatting.YELLOW));
        for (int t = 1; t <= 3; t++) { // keep short
            NoduleTiers nt = NoduleTiers.getByTier(t);
            if (nt == null)
                continue;
            String fuelId = CHEXConfig.getFuelForTier(nt);
            tooltip.add(Component.literal("  T" + t + ": " + fuelId).withStyle(ChatFormatting.GRAY));
        }

        // Material theme for current tier (example)
        if (tierBox[0] > 0) {
            NoduleTiers nt = NoduleTiers.getByTier(tierBox[0]);
            if (nt != null) {
                tooltip.add(Component.literal("Material: " + nt.getMaterialName()).withStyle(ChatFormatting.AQUA));
            }
        }
    }
}
