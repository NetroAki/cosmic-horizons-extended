package com.netroaki.chex.client.tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.registry.NoduleTiers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

/**
 * Custom tooltip component for rocket items showing tier and fuel info
 */
public class RocketTooltipComponent implements ClientTooltipComponent {

    private final NoduleTiers rocketTier;
    private final String fuelType;
    private final int fuelVolume;
    private final String[] accessiblePlanets;

    public RocketTooltipComponent(NoduleTiers rocketTier, String fuelType, int fuelVolume, String[] accessiblePlanets) {
        this.rocketTier = rocketTier;
        this.fuelType = fuelType;
        this.fuelVolume = fuelVolume;
        this.accessiblePlanets = accessiblePlanets;
    }

    @Override
    public int getHeight() {
        return 30 + (accessiblePlanets.length * 10); // Base height + planet lines
    }

    @Override
    public int getWidth(Font font) {
        int maxWidth = 0;

        // Check width of tier text
        maxWidth = Math.max(maxWidth, font.width("Tier: " + rocketTier.getDisplayName()));

        // Check width of fuel text
        maxWidth = Math.max(maxWidth, font.width("Fuel: " + fuelType + " (" + fuelVolume + "mB)"));

        // Check width of accessible planets text
        maxWidth = Math.max(maxWidth, font.width("Accessible Planets:"));
        for (String planet : accessiblePlanets) {
            maxWidth = Math.max(maxWidth, font.width("• " + planet));
        }

        return maxWidth + 10; // Add some padding
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();

        // Render tier information
        guiGraphics.drawString(font, "Tier: " + rocketTier.getDisplayName(), x, y, 0xFFFFFF);

        // Render fuel requirements
        guiGraphics.drawString(font, "Fuel: " + fuelType + " (" + fuelVolume + "mB)", x, y + 10, 0x00AAFF);

        // Render accessible planets
        guiGraphics.drawString(font, "Accessible Planets:", x, y + 20, 0xFFFF00);

        int planetY = y + 30;
        for (String planet : accessiblePlanets) {
            guiGraphics.drawString(font, "• " + planet, x, planetY, 0xAAAAAA);
            planetY += 10;
        }
    }
}
