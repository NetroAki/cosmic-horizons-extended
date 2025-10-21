package com.netroaki.chex.world.library.content;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.GsonHelper;

public class BookContent {
  public static final BookContent EMPTY = new BookContent("", List.of());

  private final String title;
  private final List<String> pages;
  private UUID id;
  private boolean isTranslated = false;

  public BookContent(String title, List<String> pages) {
    this.title = title != null ? title : "";
    this.pages = pages != null ? new ArrayList<>(pages) : new ArrayList<>();
    this.id = UUID.randomUUID();
  }

  public String getTitle() {
    return title;
  }

  public List<String> getPages() {
    return new ArrayList<>(pages);
  }

  public UUID getId() {
    return id;
  }

  public boolean isTranslated() {
    return isTranslated;
  }

  public void setTranslated(boolean translated) {
    isTranslated = translated;
  }

  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putString("title", title);
    tag.putUUID("id", id);
    tag.putBoolean("translated", isTranslated);

    ListTag pagesTag = new ListTag();
    for (String page : pages) {
      pagesTag.add(StringTag.valueOf(page));
    }
    tag.put("pages", pagesTag);

    return tag;
  }

  public static BookContent deserializeNBT(CompoundTag tag) {
    String title = tag.getString("title");
    List<String> pages = new ArrayList<>();

    ListTag pagesTag = tag.getList("pages", Tag.TAG_STRING);
    for (int i = 0; i < pagesTag.size(); i++) {
      pages.add(pagesTag.getString(i));
    }

    BookContent content = new BookContent(title, pages);
    if (tag.hasUUID("id")) {
      content.id = tag.getUUID("id");
    }
    content.isTranslated = tag.getBoolean("translated");

    return content;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.addProperty("title", title);
    json.addProperty("id", id.toString());
    json.addProperty("translated", isTranslated);

    JsonArray pagesArray = new JsonArray();
    for (String page : pages) {
      pagesArray.add(page);
    }
    json.add("pages", pagesArray);

    return json;
  }

  public static BookContent fromJson(String jsonString) {
    JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
    String title = GsonHelper.getAsString(json, "title", "");

    List<String> pages = new ArrayList<>();
    JsonArray pagesArray = GsonHelper.getAsJsonArray(json, "pages", new JsonArray());
    for (JsonElement element : pagesArray) {
      pages.add(element.getAsString());
    }

    BookContent content = new BookContent(title, pages);
    if (json.has("id")) {
      content.id = UUID.fromString(json.get("id").getAsString());
    }
    content.isTranslated = GsonHelper.getAsBoolean(json, "translated", false);

    return content;
  }

  public List<MutableComponent> getFormattedPages() {
    List<MutableComponent> formattedPages = new ArrayList<>();

    for (String page : pages) {
      MutableComponent component = Component.literal("");
      String[] lines = page.split("\n");

      for (int i = 0; i < lines.length; i++) {
        component.append(parseFormatting(lines[i]));
        if (i < lines.length - 1) {
          component.append("\n");
        }
      }

      formattedPages.add(component);
    }

    return formattedPages;
  }

  private MutableComponent parseFormatting(String text) {
    MutableComponent component = Component.literal(text);

    // Simple formatting parser - can be expanded for more complex formatting
    if (text.startsWith("#")) {
      int spaceIndex = text.indexOf(' ');
      if (spaceIndex > 0) {
        String formatCode = text.substring(1, spaceIndex);
        component = Component.literal(text.substring(spaceIndex + 1));

        switch (formatCode.toLowerCase()) {
          case "title" -> component.withStyle(
              Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0x3366FF)));
          case "subtitle" -> component.withStyle(
              Style.EMPTY.withItalic(true).withColor(TextColor.fromRgb(0x6699FF)));
          case "quote" -> component.withStyle(
              Style.EMPTY.withItalic(true).withColor(ChatFormatting.GRAY));
          case "warning" -> component.withStyle(
              Style.EMPTY.withBold(true).withColor(ChatFormatting.RED));
          case "important" -> component.withStyle(
              Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0xFFAA00)));
        }
      }
    }

    return component;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookContent that = (BookContent) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
