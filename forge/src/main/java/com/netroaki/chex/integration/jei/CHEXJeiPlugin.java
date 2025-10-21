package com.netroaki.chex.integration.jei;

import com.mojang.logging.LogUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

@JeiPlugin
public class CHEXJeiPlugin implements IModPlugin {
  private static final Logger LOGGER = LogUtils.getLogger();
  public static final ResourceLocation UID =
      new ResourceLocation("cosmic_horizons_extended", "chex");

  public static final RecipeType<RocketAssemblyRecipe> ROCKET_ASSEMBLY =
      RecipeType.create("cosmic_horizons_extended", "rocket_assembly", RocketAssemblyRecipe.class);
  public static final RecipeType<PlanetResourcesRecipe> PLANET_RESOURCES =
      RecipeType.create(
          "cosmic_horizons_extended", "planet_resources", PlanetResourcesRecipe.class);

  @Override
  public ResourceLocation getPluginUid() {
    return UID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
        new RocketAssemblyCategory(registration.getJeiHelpers().getGuiHelper()));
    registration.addRecipeCategories(
        new PlanetResourcesCategory(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    // Populate Rocket Assembly with basic tiers 1-5
    java.util.List<RocketAssemblyRecipe> rockets = new java.util.ArrayList<>();
    for (int t = 1; t <= 5; t++) {
      rockets.add(new RocketAssemblyRecipe(t));
    }
    registration.addRecipes(ROCKET_ASSEMBLY, rockets);

    // Populate Planet Resources by scanning a few known planets from bundled config
    try {
      java.util.List<PlanetResourcesRecipe> planets = PlanetResourcesRecipe.fromBundledMinerals();
      registration.addRecipes(PLANET_RESOURCES, planets);
    } catch (Exception e) {
      LOGGER.warn(
          "CHEX JEI: failed to load planet resources from minerals config: {}", e.toString());
    }
  }
}
