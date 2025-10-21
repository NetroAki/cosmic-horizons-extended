# Ocean Sovereign Arena

## Overview

The Ocean Sovereign Arena is a challenging boss structure that generates in deep ocean biomes. It serves as the lair of the Ocean Sovereign, a powerful water-based boss mob.

## Structure Details

- **Location**: Generates in deep ocean biomes, fully submerged underwater
- **Dimensions**: 32x32x16 blocks (WxLxH)
- **Materials**: Prismarine, dark prismarine, sea lanterns, and glass
- **Features**:
  - Central arena with multiple levels
  - Water-filled chambers
  - Hidden treasure rooms
  - Boss spawn platform

## Technical Implementation

### Structure Generation

- Registered in `ocean_sovereign_arena.json`
- Uses custom structure processor for boss spawning
- Integrates with the game's structure generation system

### Dependencies

- `OceanSovereignEntity`: The boss mob
- `OceanSovereignSpawnerProcessor`: Handles boss spawning
- `OceanSovereignArenaPiece`: Manages structure placement

## Loot

- Prismarine shards and crystals
- Rare enchanted tridents
- Ocean-related artifacts
- Custom boss drops

## Spawning Conditions

- Generates in deep ocean biomes
- Requires at least 15 blocks of water above the ocean floor
- Spawns at Y-levels between 30 and 45 (relative to sea level)

## Notes for Developers

1. The structure uses a custom processor for boss spawning
2. The arena is designed to be challenging but fair
3. Consider player progression when balancing the boss fight
4. The structure should be rare to maintain its special status
