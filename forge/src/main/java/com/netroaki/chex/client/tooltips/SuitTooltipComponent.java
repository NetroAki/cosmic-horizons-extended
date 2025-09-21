package com.netroaki.chex.client.tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.netroaki.chex.suits.SuitTiers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

/** Custom tooltip component for suit items showing tier and protection info */
public class SuitTooltipComponent implements ClientTooltipComponent {

  private final SuitTiers suitTier;
  private final int protection;
  private final String[] hazards;

  public SuitTooltipComponent(SuitTiers suitTier, int protection, String[] hazards) {
    this.suitTier = suitTier;
    this.protection = protection;
    this.hazards = hazards;
  }

  @Override
  public int getHeight() {
    return 20 + (hazards.length * 10); // Base height + hazard lines
  }

  @Override
  public int getWidth(Font font) {
    int maxWidth = 0;

    // Check width of tier text
    maxWidth = Math.max(maxWidth, font.width("Tier: " + suitTier.getDisplayName()));

    // Check width of protection text
    maxWidth = Math.max(maxWidth, font.width("Protection: " + protection + "%"));

    // Check width of hazard texts
    for (String hazard : hazards) {
      maxWidth = Math.max(maxWidth, font.width("• " + hazard));
    }

    return maxWidth + 10; // Add some padding
  }

  @Override
  public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
    PoseStack poseStack = guiGraphics.pose();

    // Render tier information
    guiGraphics.drawString(font, "Tier: " + suitTier.getDisplayName(), x, y, 0xFFFFFF);

    // Render protection percentage
    guiGraphics.drawString(font, "Protection: " + protection + "%", x, y + 10, 0x00FF00);

    // Render hazard protection
    int hazardY = y + 20;
    for (String hazard : hazards) {
      guiGraphics.drawString(font, "• " + hazard, x, hazardY, 0xFFAA00);
      hazardY += 10;
    }
  }
}
