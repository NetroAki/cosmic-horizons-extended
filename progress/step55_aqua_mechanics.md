# T-055 Aqua Mundus Mechanics Implementation

## Overview
Implemented the core mechanics for the Aqua Mundus water-world dimension, including oxygen systems, pressure mechanics, and thermal dynamics.

## Features Implemented

### 1. Oxygen System
- Players consume oxygen while underwater
- Oxygen tanks can be equipped to extend underwater breathing time
- Visual feedback with bubble particles
- Configurable oxygen consumption rates and tank capacities
- Drowning damage when oxygen is depleted

### 2. Pressure System
- Depth-based pressure mechanics
- Mining fatigue effects at greater depths
- Pressure damage without proper protection
- Visual feedback for pressure effects
- Configurable depth thresholds and damage intervals

### 3. Thermal System
- Temperature-based effects from biomes and heat sources
- Cold damage in arctic regions
- Heat damage near thermal vents and lava
- Visual effects for temperature extremes
- Configurable temperature thresholds

### 4. Configuration System
- Comprehensive config file for all mechanics
- Easy adjustment of all parameters
- Support for server-side configuration

## Technical Details
- Uses Forge's event system for efficient updates
- Implements persistent player data for oxygen levels
- Includes particle effects for visual feedback
- Integrates with Minecraft's damage and effect systems

## Testing
To test the mechanics:
1. Enter the Aqua Mundus dimension
2. Try swimming at different depths
3. Test near heat sources and in cold biomes
4. Equip diving gear to test pressure protection

## Configuration
All settings can be adjusted in `config/chex-aqua.toml` after first run.

## Next Steps
1. Add crafting recipes for diving equipment
2. Implement the remaining Aqua Mundus features (blocks, mobs, etc.)
3. Add more visual and audio feedback
4. Balance the mechanics based on playtesting
