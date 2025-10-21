package com.netroaki.chex.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PlanetResourcesCategory implements IRecipeCategory<PlanetResourcesRecipe> {
  public static final ResourceLocation UID =
      new ResourceLocation("cosmic_horizons_extended", "planet_resources");
  private final IDrawable icon;
  private final IDrawable background;

  public PlanetResourcesCategory(IGuiHelper gui) {
    this.icon = gui.createBlankDrawable(16, 16);
    this.background = gui.createBlankDrawable(160, 80);
  }

  @Override
  public RecipeType<PlanetResourcesRecipe> getRecipeType() {
    return CHEXJeiPlugin.PLANET_RESOURCES;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("jei.cosmic_horizons_extended.planet_resources");
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(
      IRecipeLayoutBuilder builder, PlanetResourcesRecipe recipe, IFocusGroup focuses) {
    // Descriptor-only category; no slots for now. Text drawn in draw().
  }

  @Override
  public void draw(
      PlanetResourcesRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      net.minecraft.client.gui.GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    var mc = net.minecraft.client.Minecraft.getInstance();
    var font = mc.font;
    String title =
        Component.translatable(
                "jei.cosmic_horizons_extended.planet_resources.title", recipe.planetId())
            .getString();
    guiGraphics.drawString(font, title, 4, 4, 0xFFFFAA, false);
    int y = 18;
    for (String line : recipe.resources()) {
      if (y > 72) break;
      guiGraphics.drawString(font, line, 6, y, 0xFFFFFF, false);
      y += 10;
    }
  }
}
