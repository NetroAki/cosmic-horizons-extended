# Pandora GTCEu Integration - Task T-027

## Overview

This document outlines the implementation of GTCEu ore integration for the Pandora dimension in Cosmic Horizons Expanded. The task involved mapping Pandora ores to GTCEu equivalents and updating the `chex-minerals.json5` configuration file.

## Changes Made

### 1. Updated Pandora Ore Distribution

- Added bismuthinite, phosphorite, and beryllium ores to Pandora's biomes
- Balanced ore distribution across different biomes for better gameplay progression
- Ensured all ores have appropriate vein sizes, counts, and Y-level ranges

### 2. Biome-Specific Ore Distribution

#### Bioluminescent Forest

- Added phosphorite ore as a medium vein (5 per chunk, Y=-16 to 32)
- Kept existing cobalt and nickel ores
- Added lithium patches for energy-related materials

#### Floating Mountains

- Added beryllium as a medium vein (4 per chunk, Y=-24 to 32)
- Maintained titanium and vanadium as primary ores
- Added aluminum patches for additional resources

#### Volcanic Wasteland

- Added bismuthinite as a medium vein (4 per chunk, Y=-32 to 24)
- Kept tungsten, chromium, and iridium ores
- Positioned ores to encourage exploration of dangerous areas

### 3. Fallback Ore Support

- Added fallback entries for all new ores to ensure compatibility when GTCEu is not installed
- Mapped GTCEu ore tags to fallback block IDs:
  - `gtceu:ores/bismuthinite` → `cosmic_horizons_extended:fallback_bismuthinite_ore`
  - `gtceu:ores/phosphorite` → `cosmic_horizons_extended:fallback_phosphorite_ore`
  - `gtceu:ores/beryllium` → `cosmic_horizons_extended:fallback_beryllium_ore`

## Testing

- Verified that all ore entries are properly formatted in `chex-minerals.json5`
- Confirmed fallback ore mappings are correctly defined
- Validated that all biome names match the existing biome registry

## Next Steps

1. Test the ore generation in-game to ensure proper distribution
2. Verify that fallback ores generate correctly when GTCEu is not installed
3. Update documentation with the new ore distribution details

## Related Files

- `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5`
- `forge/src/main/resources/data/cosmic_horizons_extended/config/fallback_ores.json5`

## Completion Status

- [x] Add new ore distributions for Pandora
- [x] Update fallback ore mappings
- [ ] Test ore generation in-game
- [ ] Update documentation with new ore information
