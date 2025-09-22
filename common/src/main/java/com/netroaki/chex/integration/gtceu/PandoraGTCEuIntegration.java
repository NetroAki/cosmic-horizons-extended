package com.netroaki.chex.integration.gtceu;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class PandoraGTCEuIntegration {
    private static boolean isGTCEuLoaded = false;
    private static MaterialRegistry materialRegistry;
    
    // Material references
    public static Material UNOBTANIUM;
    public static Material VIBRANIUM;
    public static Material ADAMANTIUM;
    public static Material MITHRIL;
    public static Material ORICHALCUM;
    public static Material ELEMENT_ZERO;
    
    public static void init() {
        isGTCEuLoaded = ModList.get().isLoaded("gtceu");
        if (!isGTCEuLoaded) {
            CHEX.LOGGER.info("GTCEu not found, skipping Pandora material registration");
            return;
        }
        
        CHEX.LOGGER.info("Registering Pandora materials with GTCEu");
        
        // Get or create material registry
        materialRegistry = GTCEuAPI.materialManager.createRegistry(CHEX.MOD_ID);
        
        // Register materials
        registerMaterials();
        
        // Register processing recipes
        registerRecipes();
    }
    
    private static void registerMaterials() {
        // Create or get materials with appropriate properties
        UNOBTANIUM = getOrCreateMaterial("unobtanium", builder -> builder
            .ingot()
            .color(0x8A2BE2) // BlueViolet
            .iconSet(GTMaterials.IconSet.SHINY)
            .element(GTMaterials.Ne)
            .build()
        );
        
        VIBRANIUM = getOrCreateMaterial("vibranium", builder -> builder
            .ingot()
            .color(0x32CD32) // LimeGreen
            .iconSet(GTMaterials.IconSet.SHINY)
            .element(GTMaterials.V)
            .build()
        );
        
        ADAMANTIUM = getOrCreateMaterial("adamantium", builder -> builder
            .ingot()
            .color(0x4682B4) // SteelBlue
            .iconSet(GTMaterials.IconSet.DULL)
            .element(GTMaterials.Ad)
            .build()
        );
        
        MITHRIL = getOrCreateMaterial("mithril", builder -> builder
            .ingot()
            .color(0x00BFFF) // DeepSkyBlue
            .iconSet(GTMaterials.IconSet.SHINY)
            .element(GTMaterials.Mt)
            .build()
        );
        
        ORICHALCUM = getOrCreateMaterial("orichalcum", builder -> builder
            .ingot()
            .color(0xFFD700) // Gold
            .iconSet(GTMaterials.IconSet.SHINY)
            .element(GTMaterials.Or)
            .build()
        );
        
        ELEMENT_ZERO = getOrCreateMaterial("element_zero", builder -> builder
            .ingot()
            .color(0x9400D3) // DarkViolet
            .iconSet(GTMaterials.IconSet.SHINY)
            .element(GTMaterials.Ez)
            .build()
        );
    }
    
    private static void registerRecipes() {
        if (!isGTCEuLoaded) return;
        
        GTRegistrate registrate = GTCEu.REGISTRATE;
        
        // Register processing recipes for each material
        registerOreProcessing(UNOBTANIUM, 4); // IV Tier
        registerOreProcessing(VIBRANIUM, 3);  // HV Tier
        registerOreProcessing(ADAMANTIUM, 5); // LuV Tier
        registerOreProcessing(MITHRIL, 3);    // HV Tier
        registerOreProcessing(ORICHALCUM, 4); // IV Tier
        registerOreProcessing(ELEMENT_ZERO, 5); // LuV Tier
    }
    
    private static void registerOreProcessing(Material material, int tier) {
        if (material == null) return;
        
        // Example: Macerate -> Wash -> Centrifuge -> Electrolyze -> Smelt
        GTRecipeTypes.MACERATOR_RECIPES.recipeBuilder("macerate_" + material.getName())
            .inputItems(new UnificationEntry(OrePrefix.crushed, material))
            .outputItems(new ItemStack(ModItems.CRUSHED_ORE.get(material)))
            .duration(400).EUt(2)
            .save(provider);
            
        // Add more processing steps as needed...
    }
    
    @Nullable
    private static Material getOrCreateMaterial(String name, java.util.function.Consumer<MaterialBuilder> builder) {
        if (!isGTCEuLoaded) return null;
        
        Material existing = GTMaterials.get(name);
        if (existing != null) {
            return existing;
        }
        
        MaterialBuilder materialBuilder = new MaterialBuilder(new ResourceLocation(CHEX.MOD_ID, name));
        builder.accept(materialBuilder);
        return materialBuilder.register();
    }
    
    public static boolean isGTCEuLoaded() {
        return isGTCEuLoaded;
    }
    
    @Nullable
    public static Material getMaterial(String name) {
        if (!isGTCEuLoaded) return null;
        return materialRegistry.get(name);
    }
}
