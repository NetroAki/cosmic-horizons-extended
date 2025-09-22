package com.netroaki.chex.integration.gtceu;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for GTCEu compatibility.
 * Provides fallback implementations when GTCEu is not present.
 */
public interface GTCEuCompat {
    GTCEuCompat INSTANCE = GTCEuCompatImpl.INSTANCE;
    
    /**
     * @return True if GTCEu is loaded and available
     */
    boolean isGTCEuLoaded();
    
    /**
     * Gets the GTCEu material for a given ore block.
     * @param block The ore block
     * @return The GTCEu material name, or null if not found or GTCEu is not loaded
     */
    @Nullable
    String getMaterialForOre(Block block);
    
    /**
     * Gets the GTCEu tier for a given material.
     * @param materialName The material name
     * @return The GTCEu tier, or -1 if not found or GTCEu is not loaded
     */
    int getMaterialTier(String materialName);
    
    /**
     * Processes an ore block according to GTCEu standards.
     * @param state The block state to process
     * @return The processed item stack, or the original block if GTCEu is not loaded
     */
    ItemStack processOre(BlockState state);
    
    /**
     * Gets the energy value for a given material in EU/t.
     * @param materialName The material name
     * @return The energy value, or 0 if not found or GTCEu is not loaded
     */
    long getEnergyValue(String materialName);
    
    /**
     * Gets the voltage tier for a given voltage in EU/t.
     * @param voltage The voltage in EU/t
     * @return The voltage tier name, or "ULV" if GTCEu is not loaded
     */
    String getVoltageTier(long voltage);
    
    /**
     * Registers a custom material with GTCEu.
     * @param name The material name
     * @param color The material color in 0xRRGGBB format
     * @param tier The material tier (1-14)
     * @return True if registration was successful
     */
    boolean registerCustomMaterial(String name, int color, int tier);
}
