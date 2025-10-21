# Aqua Mundus Mechanics Implementation

## Overview

Implemented and enhanced the core mechanics for the Aqua Mundus dimension, including pressure, oxygen, and thermal systems with full configuration support.

## Features Added/Updated

### 1. Enhanced Pressure System

- Implemented depth-based pressure calculations with configurable scaling
- Added progressive effects based on pressure levels:
  - Movement speed reduction
  - Mining fatigue (increases with depth)
  - Slowness at extreme depths
  - Damage at critical pressure levels
- Configurable base pressure, increase rate, and maximum values

### 2. Improved Oxygen System

- Implemented oxygen depletion when underwater with configurable rates
- Added oxygen restoration when at the surface
- Visual feedback for low oxygen levels
- Configurable maximum oxygen level and restoration rates
- Drowning damage when oxygen is depleted

### 3. Advanced Thermal System

- Implemented biome-based temperature effects with configurable thresholds
- Cold biome effects:
  - Movement speed reduction
  - Mining fatigue in very cold areas
  - Hypothermia damage in extreme cold when not in water
- Hot biome effects:
  - Fire resistance in hot water
  - Movement speed boost in hot water
  - Heat stroke damage in extreme heat when not in water

### 4. Comprehensive Configuration

- Added detailed configuration options for all mechanics
- Created a dedicated config file: `cosmichorizonsexpanded-aqua_mundus.toml`
- All values are configurable in-game and support reloading

## Technical Implementation

- Refactored `AquaMundusMechanics` class with better organization
- Added proper event handling for player ticks and movement
- Implemented configuration using Forge's config system
- Added proper dimension checking for all mechanics
- Optimized performance with efficient calculations
- Added proper null checks and error handling

## Testing Performed

- Verified pressure effects scale correctly with depth
- Confirmed oxygen depletion and restoration work as expected
- Tested thermal effects in different biomes
- Verified configuration changes take effect without restart
- Confirmed effects are only active in Aqua Mundus dimension

## Future Improvements

1. Add visual effects for pressure and oxygen levels
2. Implement specialized diving equipment
3. Add more biome-specific effects and interactions
4. Consider adding thermal vents and other environmental features
5. Add sound effects for pressure changes and oxygen warnings

## Configuration Options

All mechanics can be configured through the `cosmichorizonsexpanded-aqua_mundus.toml` file in the config directory. The configuration is organized into sections for pressure, oxygen, and thermal settings, with detailed tooltips for each option.
