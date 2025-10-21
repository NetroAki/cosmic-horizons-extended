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

public class RocketAssemblyCategory implements IRecipeCategory<RocketAssemblyRecipe> {
  public static final ResourceLocation UID =
      new ResourceLocation("cosmic_horizons_extended", "rocket_assembly");
  private final IDrawable icon;
  private final IDrawable background;

  public RocketAssemblyCategory(IGuiHelper gui) {
    this.icon = gui.createBlankDrawable(16, 16);
    this.background = gui.createBlankDrawable(150, 18);
  }

  @Override
  public RecipeType<RocketAssemblyRecipe> getRecipeType() {
    return CHEXJeiPlugin.ROCKET_ASSEMBLY;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("jei.cosmic_horizons_extended.rocket_assembly");
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
      IRecipeLayoutBuilder builder, RocketAssemblyRecipe recipe, IFocusGroup focuses) {
    // Minimal descriptor-only category; no slots required for now.
  }

  @Override
  public void draw(
      RocketAssemblyRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      net.minecraft.client.gui.GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    var mc = net.minecraft.client.Minecraft.getInstance();
    var font = mc.font;
    String line =
        Component.translatable("jei.cosmic_horizons_extended.rocket_assembly.tier", recipe.tier())
            .getString();
    guiGraphics.drawString(font, line, 4, 4, 0xFFFFFF, false);
  }
}
