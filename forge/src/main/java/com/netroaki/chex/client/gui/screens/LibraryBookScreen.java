package com.netroaki.chex.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.item.library.LibraryBookItem;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LibraryBookScreen extends Screen {
  private static final ResourceLocation BOOK_TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/gui/book.png");
  private static final int BOOK_WIDTH = 146;
  private static final int BOOK_HEIGHT = 180;

  private final ItemStack bookStack;
  private int leftPos;
  private int topPos;
  private int currentPage;
  private List<String> pages;
  private Button nextButton;
  private Button prevButton;
  private Button closeButton;
  private boolean isTranslated;
  private String language;

  public LibraryBookScreen(ItemStack bookStack) {
    super(Component.translatable("chex.gui.library_book"));
    this.bookStack = bookStack;

    // Extract book data
    if (bookStack.getItem() instanceof LibraryBookItem) {
      LibraryBookItem.BookContent content = LibraryBookItem.getBookContent(bookStack);
      this.pages = content.pages();
      this.isTranslated = content.isTranslated();
      this.language = content.originalLanguage();
    } else {
      this.pages = List.of("Invalid book");
      this.isTranslated = true;
      this.language = "Unknown";
    }
  }

  @Override
  protected void init() {
    super.init();

    this.leftPos = (this.width - BOOK_WIDTH) / 2;
    this.topPos = (this.height - BOOK_HEIGHT) / 2;

    // Navigation buttons
    this.nextButton =
        this.addRenderableWidget(
            Button.builder(Component.literal(">"), button -> nextPage())
                .bounds(this.leftPos + BOOK_WIDTH - 40, this.topPos + 160, 20, 20)
                .build());

    this.prevButton =
        this.addRenderableWidget(
            Button.builder(Component.literal("<"), button -> previousPage())
                .bounds(this.leftPos + 20, this.topPos + 160, 20, 20)
                .build());

    this.closeButton =
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE, button -> onClose())
                .bounds(this.width / 2 - 50, this.topPos + 160, 100, 20)
                .build());

    updateButtonVisibility();
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics);

    // Draw book background
    RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
    guiGraphics.blit(BOOK_TEXTURE, this.leftPos, this.topPos, 0, 0, BOOK_WIDTH, BOOK_HEIGHT);

    // Draw page content
    int textX = this.leftPos + 20;
    int textY = this.topPos + 30;

    // Draw page number
    String pageInfo =
        String.format("%d / %d", this.currentPage + 1, Math.max(1, this.pages.size()));
    guiGraphics.drawString(
        this.font, pageInfo, this.leftPos + BOOK_WIDTH / 2 - 10, this.topPos + 10, 0, false);

    // Draw language indicator if not translated
    if (!this.isTranslated && this.language != null) {
      guiGraphics.drawString(
          this.font,
          "[" + this.language + "]",
          this.leftPos + 20,
          this.topPos + 10,
          0xAA0000,
          false);
    }

    // Draw current page text
    if (!this.pages.isEmpty() && this.currentPage < this.pages.size()) {
      String pageText = this.pages.get(this.currentPage);
      guiGraphics.renderTooltip(
          this.font, this.font.split(Component.literal(pageText), 100), mouseX, mouseY);
    }

    super.render(guiGraphics, mouseX, mouseY, partialTicks);
  }

  private void nextPage() {
    if (this.currentPage < this.pages.size() - 1) {
      this.currentPage++;
      updateButtonVisibility();
    }
  }

  private void previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      updateButtonVisibility();
    }
  }

  private void updateButtonVisibility() {
    this.prevButton.visible = this.currentPage > 0;
    this.nextButton.visible = this.currentPage < this.pages.size() - 1;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }

    // Handle keybinds for page turning
    switch (keyCode) {
      case 262 -> { // Right arrow
        nextPage();
        return true;
      }
      case 263 -> { // Left arrow
        previousPage();
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  public static void open(ItemStack bookStack) {
    Minecraft.getInstance().setScreen(new LibraryBookScreen(bookStack));
  }
}
