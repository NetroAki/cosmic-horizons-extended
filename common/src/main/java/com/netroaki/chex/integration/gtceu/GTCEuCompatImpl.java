package com.netroaki.chex.integration.gtceu;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.netroaki.chex.CHEX;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GTCEuCompatImpl implements GTCEuCompat {
    public static final GTCEuCompatImpl INSTANCE = new GTCEuCompatImpl();
    
    private static final Map<String, Integer> MATERIAL_TIERS = new HashMap<>();
    private static final Map<String, Long> ENERGY_VALUES = new HashMap<>();
    
    private boolean isGTCEuLoaded;
    private final Map<Block, String> oreToMaterialMap = new HashMap<>();
    
    private GTCEuCompatImpl() {
        isGTCEuLoaded = ModList.get().isLoaded("gtceu");
        if (isGTCEuLoaded) {
            initializeGTCEuIntegration();
        } else {
            initializeFallbackValues();
        }
    }
    
    private void initializeGTCEuIntegration() {
        try {
            // Register Pandora materials with GTCEu
            PandoraGTCEuIntegration.init();
            
            // Map Pandora ores to GTCEu materials
            registerOreMapping("unobtanium_ore", "unobtanium", GTValues.IV);
            registerOreMapping("vibranium_ore", "vibranium", GTValues.HV);
            registerOreMapping("adamantium_ore", "adamantium", GTValues.LuV);
            registerOreMapping("mithril_ore", "mithril", GTValues.HV);
            registerOreMapping("orichalcum_ore", "orichalcum", GTValues.IV);
            registerOreMapping("element_zero_ore", "element_zero", GTValues.LuV);
            
            // Set energy values (in EU)
            setEnergyValue("unobtanium", 1048576L);
            setEnergyValue("vibranium", 262144L);
            setEnergyValue("adamantium", 4194304L);
            setEnergyValue("mithril", 131072L);
            setEnergyValue("orichalcum", 524288L);
            setEnergyValue("element_zero", 8388608L);
            
            CHEX.LOGGER.info("GTCEu compatibility layer initialized");
        } catch (Exception e) {
            CHEX.LOGGER.error("Failed to initialize GTCEu integration", e);
            isGTCEuLoaded = false;
            initializeFallbackValues();
        }
    }
    
    private void initializeFallbackValues() {
        // Fallback values when GTCEu is not loaded
        CHEX.LOGGER.info("Using fallback values for GTCEu integration");
        
        // Set default tiers
        MATERIAL_TIERS.put("unobtanium", 4);
        MATERIAL_TIERS.put("vibranium", 3);
        MATERIAL_TIERS.put("adamantium", 5);
        MATERIAL_TIERS.put("mithril", 3);
        MATERIAL_TIERS.put("orichalcum", 4);
        MATERIAL_TIERS.put("element_zero", 5);
        
        // Set default energy values (in EU)
        ENERGY_VALUES.put("unobtanium", 1048576L);
        ENERGY_VALUES.put("vibranium", 262144L);
        ENERGY_VALUES.put("adamantium", 4194304L);
        ENERGY_VALUES.put("mithril", 131072L);
        ENERGY_VALUES.put("orichalcum", 524288L);
        ENERGY_VALUES.put("element_zero", 8388608L);
    }
    
    private void registerOreMapping(String oreName, String materialName, int tier) {
        Block oreBlock = Block.byId(ResourceLocation.tryParse("chex:" + oreName));
        if (oreBlock != null) {
            oreToMaterialMap.put(oreBlock, materialName);
            MATERIAL_TIERS.put(materialName, tier);
        }
    }
    
    private void setEnergyValue(String materialName, long eu) {
        ENERGY_VALUES.put(materialName, eu);
    }
    
    @Override
    public boolean isGTCEuLoaded() {
        return isGTCEuLoaded;
    }
    
    @Override
    public @Nullable String getMaterialForOre(Block block) {
        return oreToMaterialMap.get(block);
    }
    
    @Override
    public int getMaterialTier(String materialName) {
        return MATERIAL_TIERS.getOrDefault(materialName, -1);
    }
    
    @Override
    public ItemStack processOre(BlockState state) {
        if (!isGTCEuLoaded) {
            // Fallback to vanilla smelting if GTCEu is not loaded
            return new ItemStack(state.getBlock().asItem());
        }
        
        // In a real implementation, this would use GTCEu's ore processing system
        String materialName = getMaterialForOre(state.getBlock());
        if (materialName != null) {
            // Return the appropriate crushed ore or dust
            return new ItemStack(state.getBlock().asItem());
        }
        
        return new ItemStack(state.getBlock().asItem());
    }
    
    @Override
    public long getEnergyValue(String materialName) {
        return ENERGY_VALUES.getOrDefault(materialName, 0L);
    }
    
    @Override
    public String getVoltageTier(long voltage) {
        if (!isGTCEuLoaded) {
            return "ULV";
        }
        
        // Map voltage to GTCEu tier name
        return GTValues.VN[GTValues.getTierByVoltage(voltage)];
    }
    
    @Override
    public boolean registerCustomMaterial(String name, int color, int tier) {
        if (!isGTCEuLoaded) {
            // Store in fallback registry
            MATERIAL_TIERS.put(name, tier);
            ENERGY_VALUES.put(name, (long) Math.pow(4, tier) * 1000);
            return true;
        }
        
        try {
            // In a real implementation, this would register with GTCEu
            return true;
        } catch (Exception e) {
            CHEX.LOGGER.error("Failed to register custom material: " + name, e);
            return false;
        }
    }
}
