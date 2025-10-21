package com.netroaki.chex.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.world.library.item.LibraryBookMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LibraryBookScreen extends AbstractContainerScreen<LibraryBookMenu> {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/gui/library_book.png");

  private static final int IMAGE_WIDTH = 176;
  private static final int IMAGE_HEIGHT = 180;

  public LibraryBookScreen(LibraryBookMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    this.imageWidth = IMAGE_WIDTH;
    this.imageHeight = IMAGE_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);

    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;

    // Draw the background
    guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

    // Draw the book pages based on the book type
    switch (this.menu.getBookType()) {
      case LORE -> {
        // Draw lore book background
        guiGraphics.blit(TEXTURE, x + 30, y + 15, 0, 180, 116, 140);
        // Add lore text rendering here
      }
      case KNOWLEDGE -> {
        // Draw knowledge book background
        guiGraphics.blit(TEXTURE, x + 30, y + 15, 116, 180, 116, 140);
        // Add knowledge text rendering here
      }
      case SECRET -> {
        // Draw secret book background
        guiGraphics.blit(TEXTURE, x + 30, y + 15, 232, 180, 116, 140);
        // Add secret text rendering here
      }
    }
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics);
    super.render(guiGraphics, mouseX, mouseY, partialTicks);
    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    // Draw the title
    guiGraphics.drawString(
        this.font,
        this.title,
        (this.imageWidth - this.font.width(this.title)) / 2,
        6,
        0x404040,
        false);

    // Draw the inventory label
    guiGraphics.drawString(
        this.font,
        this.playerInventoryTitle,
        this.inventoryLabelX,
        this.inventoryLabelY,
        0x404040,
        false);
  }
}
