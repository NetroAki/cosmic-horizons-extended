# T-027 Pandora GTCEu Integration

## Overview
Integrated Pandora's mineral resources with GregTech CE: Unofficial (GTCEu) to provide advanced processing paths and compatibility. This includes mapping Pandora ores to GTCEu materials and implementing fallback systems for when GTCEu is not present.

## Implementation Details

### 1. Mineral Configuration
- Added Pandora-specific ore generation settings to `chex-minerals.json5`
- Configured biome-specific ore distributions for Pandora's unique biomes
- Set appropriate vein sizes and generation heights for each ore type

### 2. GTCEu Material Mapping
- Mapped Pandora ores to GTCEu materials:
  - Unobtanium → Neutronium
  - Vibranium → Vibranium
  - Adamantium → Adamantium
  - Mithril → Mithril
  - Orichalcum → Orichalcum
  - Element Zero → Naquadah

### 3. Fallback System
- Implemented fallback ore generation for when GTCEu is not installed
- Added config options to force fallback behavior if needed
- Ensured world generation remains consistent regardless of GTCEu presence

### 4. Processing Chains
- Added GTCEu processing recipes for Pandora materials
- Integrated with existing GTCEu material system
- Added custom electrolyzer and centrifuge recipes for Pandora ores

## Configuration

### chex-minerals.json5 Updates
```json5
{
  "version": 1,
  "pandora": {
    "unobtanium_ore": {
      "vein_size": 4,
      "veins_per_chunk": 2,
      "min_y": -64,
      "max_y": 16,
      "biome_filter": "#chex:pandora/underground_ores",
      "gtceu_material": "neutronium"
    },
    "vibranium_ore": {
      "vein_size": 6,
      "veins_per_chunk": 3,
      "min_y": -32,
      "max_y": 48,
      "biome_filter": "#chex:pandora/floating_islands",
      "gtceu_material": "vibranium"
    },
    "adamantium_ore": {
      "vein_size": 3,
      "veins_per_chunk": 1,
      "min_y": -48,
      "max_y": 0,
      "biome_filter": "#chex:pandora/volcanic_wastes",
      "gtceu_material": "adamantium"
    },
    "mithril_ore": {
      "vein_size": 5,
      "veins_per_chunk": 2,
      "min_y": 32,
      "max_y": 96,
      "biome_filter": "#chex:pandora/floating_islands",
      "gtceu_material": "mithril"
    },
    "orichalcum_ore": {
      "vein_size": 4,
      "veins_per_chunk": 3,
      "min_y": 0,
      "max_y": 64,
      "biome_filter": "#chex:pandora/underground_ores",
      "gtceu_material": "orichalcum"
    },
    "element_zero_ore": {
      "vein_size": 3,
      "veins_per_chunk": 1,
      "min_y": -16,
      "max_y": 32,
      "biome_filter": "#chex:pandora/underground_ores",
      "gtceu_material": "naquadah"
    }
  }
}
```

## Technical Implementation

### 1. GTCEu Integration
- Created `GTCEuIntegration` class to handle GTCEu-specific initialization
- Registered ore dictionary entries for cross-mod compatibility
- Added custom material properties for Pandora materials

### 2. Fallback System
- Implemented `GTCEuCompat` interface for clean fallback behavior
- Added runtime checks for GTCEu presence
- Ensured graceful degradation when GTCEu is not installed

### 3. Recipe Generation
- Added datagen for GTCEu recipes
- Implemented custom recipe builders for Pandora materials
- Added ore processing chain from raw ore to ingot

## Testing
- [ ] Verify ore generation in Pandora with GTCEu installed
- [ ] Test ore processing chains
- [ ] Verify fallback behavior without GTCEu
- [ ] Test world generation consistency

## Next Steps
1. Add custom GTCEu machines for Pandora materials
2. Implement custom tool and armor materials
3. Add custom multiblock structures for advanced processing
4. Create custom GTCEu research entries for Pandora technology
