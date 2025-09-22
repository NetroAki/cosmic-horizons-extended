# T-022 Pandora Blocks Implementation

## Overview
This document tracks the implementation of custom blocks for the Pandora dimension, including their properties, textures, and integration points.

## Block List

### 1. Pandorite Blocks
- **Pandorite Stone**: Base stone variant for Pandora
- **Polished Pandorite**: Decorative variant
- **Pandorite Bricks**: Decorative building block
- **Chiseled Pandorite**: Decorative block with carved patterns
- **Pandorite Pillar**: Vertical pillar variant

### 2. Soil & Vegetation
- **Spore Soil**: Fertile ground for Pandoran flora
- **Biolume Moss**: Glowing ground cover
- **Biolume Vines**: Hanging bioluminescent plants
- **Pandoran Grass**: Native grass variant
- **Pandoran Tall Grass**: Taller grass variant

### 3. Crystals & Minerals
- **Crystal-Clad Pandorite**: Pandorite with crystal growths
- **Pandoran Crystal Cluster**: Large crystal formation
- **Small Pandoran Crystal**: Decorative crystal
- **Pandoran Crystal Block**: Solid crystal block

### 4. Special Blocks
- **Lumicoral**: Glowing coral-like formations
- **Glowvine**: Climbing bioluminescent plant
- **Floating Rock**: Gravity-affected decorative block
- **Spore Particle Emitter**: Emits spore particles

## Technical Implementation

### Block Properties
- **Hardness**: 1.5-3.0 (stone-like)
- **Resistance**: 6.0-9.0 (blast resistant)
- **Luminance**: 0-15 (for glowing blocks)
- **Sound Type**: STONE for rocks, PLANT for vegetation

### Texturing
- Base textures: 16x16 PNG with normal/specular maps
- PBR materials for enhanced visuals
- Animated textures for special effects

### Integration Points
- **GTCEu**: Ore processing compatibility
- **TerraBlender**: Biome-specific generation
- **Cosmology**: Dimensional properties

## Next Steps
1. Implement block models and textures
2. Create blockstate JSONs
3. Add loot tables and recipes
4. Test in-game generation
5. Document block properties and uses

## Testing Notes
- Verify block hardness and resistance values
- Test light emission for glowing blocks
- Check biome-specific generation
- Verify block interactions and tool requirements
- Test rendering performance with multiple blocks
