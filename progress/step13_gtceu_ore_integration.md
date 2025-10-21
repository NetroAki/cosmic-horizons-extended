# GTCEu Ore Integration for Pandora

## Overview

This document outlines the implementation of GTCEu ore integration for the Pandora dimension, including fallback ore configurations.

## Implementation Details

### 1. Fallback Ore Mappings

Added fallback mappings for Pandora-specific ores in `fallback_ores.json5`:

- `bismuthinite` → `minecraft:copper_ore`
- `phosphorite` → `minecraft:glowstone`
- `vanadium` → `minecraft:iron_ore`
- `manganese` → `minecraft:iron_ore`
- `cobalt` → `minecraft:iron_ore`

These mappings ensure that if GTCEu is not installed, the game will use appropriate vanilla blocks as substitutes for Pandora's ores.

### 2. Ore Distribution

Pandora's ore distribution is configured in `chex-minerals.json5` with the following characteristics:

- **Bioluminescent Forest**: Rich in Cobalt and Nickel
- **Floating Mountains**: Abundant Titanium and Vanadium deposits
- **Ocean Depths**: Manganese and rare earth elements
- **Volcanic Wasteland**: Heat-resistant metals like Tungsten and Iridium
- **Sky Islands**: Lightweight and conductive materials like Aluminum and Copper

### 3. Configuration

- **Rocket Tier**: 4
- **Target Tier**: MV (Medium Voltage)
- **Biome-Specific Distributions**: Each biome has unique ore generation rules

## Testing

- [ ] Verify ore generation in each Pandora biome
- [ ] Test fallback behavior when GTCEu is not installed
- [ ] Check ore generation at different Y-levels
- [ ] Verify biome-specific ore distribution

## Next Steps

1. Add visual effects for unique ores
2. Implement custom ore processing recipes
3. Add tooltips and documentation for new ores
4. Create custom textures for Pandora-specific ores
