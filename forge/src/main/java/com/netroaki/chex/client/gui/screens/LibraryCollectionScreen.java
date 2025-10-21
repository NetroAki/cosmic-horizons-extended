package com.netroaki.chex.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.netroaki.chex.CHEX;
import com.netroaki.chex.item.library.LibraryBookItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LibraryCollectionScreen extends Screen {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(CHEX.MOD_ID, "textures/gui/library_collection.png");
  private static final int WINDOW_WIDTH = 248;
  private static final int WINDOW_HEIGHT = 166;

  private int leftPos;
  private int topPos;
  private BookList bookList;
  private Button readButton;
  private Button sortButton;
  private int sortMode = 0; // 0 = by title, 1 = by language, 2 = by date found
  private final List<ItemStack> books = new ArrayList<>();

  public LibraryCollectionScreen() {
    super(Component.translatable("chex.gui.library_collection"));

    // Get all books from player's inventory
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.player != null) {
      Inventory inventory = minecraft.player.getInventory();
      for (int i = 0; i < inventory.getContainerSize(); i++) {
        ItemStack stack = inventory.getItem(i);
        if (stack.getItem() instanceof LibraryBookItem) {
          books.add(stack);
        }
      }
    }
  }

  @Override
  protected void init() {
    super.init();

    this.leftPos = (this.width - WINDOW_WIDTH) / 2;
    this.topPos = (this.height - WINDOW_HEIGHT) / 2;

    // Initialize book list
    this.bookList =
        new BookList(
            minecraft,
            WINDOW_WIDTH - 30,
            WINDOW_HEIGHT - 60,
            this.topPos + 20,
            this.topPos + WINDOW_HEIGHT - 40,
            30);
    this.addRenderableWidget(this.bookList);

    // Sort button
    this.sortButton =
        this.addRenderableWidget(
            Button.builder(Component.translatable("chex.gui.sort"), button -> cycleSortMode())
                .bounds(this.leftPos + 10, this.topPos + 10, 60, 20)
                .build());

    // Read button
    this.readButton =
        this.addRenderableWidget(
            Button.builder(
                    Component.translatable("chex.gui.read"),
                    button -> {
                      BookList.Entry entry = this.bookList.getSelected();
                      if (entry != null) {
                        Minecraft.getInstance().setScreen(new LibraryBookScreen(entry.getBook()));
                      }
                    })
                .bounds(this.leftPos + WINDOW_WIDTH - 70, this.topPos + WINDOW_HEIGHT - 30, 60, 20)
                .build());

    // Close button
    this.addRenderableWidget(
        Button.builder(CommonComponents.GUI_DONE, button -> onClose())
            .bounds(this.leftPos + WINDOW_WIDTH / 2 - 50, this.topPos + WINDOW_HEIGHT - 30, 100, 20)
            .build());

    updateButtonStates();
    sortBooks();
  }

  private void cycleSortMode() {
    sortMode = (sortMode + 1) % 3;
    sortBooks();
    updateSortButtonText();
  }

  private void updateSortButtonText() {
    String[] sortNames = {"chex.gui.sort.title", "chex.gui.sort.language", "chex.gui.sort.date"};
    this.sortButton.setMessage(Component.translatable(sortNames[sortMode]));
  }

  private void sortBooks() {
    books.sort(
        (stack1, stack2) -> {
          if (stack1.getItem() instanceof LibraryBookItem book1
              && stack2.getItem() instanceof LibraryBookItem book2) {

            LibraryBookItem.BookContent content1 = book1.getBookContent(stack1);
            LibraryBookItem.BookContent content2 = book2.getBookContent(stack2);

            return switch (sortMode) {
              case 1 -> content1.languageId().compareToIgnoreCase(content2.languageId());
              case 2 -> Integer.compare(
                  book1.getDiscoveryTime(stack1), book2.getDiscoveryTime(stack2));
              default -> content1.title().compareToIgnoreCase(content2.title());
            };
          }
          return 0;
        });

    // Refresh the list
    if (this.bookList != null) {
      this.bookList.refreshList();
    }
  }

  private void updateButtonStates() {
    this.readButton.active = this.bookList.getSelected() != null;
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(guiGraphics);

    // Draw window background
    RenderSystem.setShaderTexture(0, TEXTURE);
    guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Draw title
    guiGraphics.drawString(
        this.font, this.title, this.leftPos + 8, this.topPos + 6, 0x404040, false);

    super.render(guiGraphics, mouseX, mouseY, partialTicks);

    // Draw tooltips
    if (this.bookList != null) {
      this.bookList.renderTooltip(guiGraphics, mouseX, mouseY);
    }
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  class BookList extends ObjectSelectionList<BookList.Entry> {
    public BookList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
      super(minecraft, width, y1 - y0, y0, itemHeight);
      this.setRenderBackground(false);
      this.setRenderTopAndBottom(false);
      this.refreshList();
    }

    public void refreshList() {
      this.clearEntries();

      for (ItemStack book : books) {
        if (book.getItem() instanceof LibraryBookItem) {
          this.addEntry(new Entry(book));
        }
      }
    }

    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
      Entry entry = this.getEntryAtPosition(mouseX, mouseY);
      if (entry != null) {
        guiGraphics.renderTooltip(font, entry.getTooltip(), mouseX, mouseY);
      }
    }

    @Override
    protected int getScrollbarPosition() {
      return this.x1 - 6;
    }

    @Override
    public int getRowWidth() {
      return this.width - 10;
    }

    @OnlyIn(Dist.CLIENT)
    public class Entry extends ObjectSelectionList.Entry<BookList.Entry> {
      private final ItemStack book;
      private final Component displayName;
      private final Component language;

      public Entry(ItemStack book) {
        this.book = book;
        this.displayName = book.getHoverName();

        if (book.getItem() instanceof LibraryBookItem libraryBook) {
          LibraryBookItem.BookContent content = libraryBook.getBookContent(book);
          this.language =
              Component.translatable("chex.language." + content.languageId())
                  .withStyle(
                      style -> style.withColor(content.isTranslated() ? 0x55FF55 : 0xFF5555));
        } else {
          this.language = Component.literal("Unknown");
        }
      }

      @Override
      public void render(
          @NotNull GuiGraphics guiGraphics,
          int index,
          int top,
          int left,
          int width,
          int height,
          int mouseX,
          int mouseY,
          boolean isMouseOver,
          float partialTicks) {
        guiGraphics.drawString(font, this.displayName, left + 2, top + 2, 0xFFFFFF, false);
        guiGraphics.drawString(font, this.language, left + 2, top + 12, 0xFFFFFF, false);

        // Draw a small book icon
        guiGraphics.renderItem(
            new ItemStack(net.minecraft.world.item.Items.BOOK), left + width - 20, top + 2);
      }

      @Override
      public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
          this.select();
          return true;
        }
        return false;
      }

      private void select() {
        BookList.this.setSelected(this);
        updateButtonStates();
      }

      public ItemStack getBook() {
        return book;
      }

      public List<Component> getTooltip() {
        return book.getTooltipLines(
            Minecraft.getInstance().player,
            Minecraft.getInstance().options.advancedItemTooltips()
                ? net.minecraft.world.item.TooltipFlag.ADVANCED
                : net.minecraft.world.item.TooltipFlag.NORMAL);
      }

      @Override
      public @NotNull Component getNarration() {
        return Component.translatable("narrator.select", this.displayName);
      }
    }
  }

  public static void open() {
    Minecraft.getInstance().setScreen(new LibraryCollectionScreen());
  }
}
