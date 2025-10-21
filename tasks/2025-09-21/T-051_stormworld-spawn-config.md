# T51: Stormworld Mob Spawn Configuration

## Overview

Configure spawn conditions and rules for Stormworld mobs to ensure balanced and thematic gameplay. This includes biome-specific spawning, weather-based spawn conditions, and appropriate spawn rates.

## Spawn Configuration Tasks

### 1. Biome-Specific Spawning

- [x] Configure spawns for Stormcaller

  - Spawns in open areas during storms
  - Higher spawn rate in mountainous regions
  - Rare spawn in other biomes during clear weather

- [x] Configure spawns for Windrider

  - Common in high-altitude biomes
  - Increased spawn rate during windy conditions
  - Can spawn in groups of 2-3

- [x] Configure spawns for Static Jelly
  - Spawns near charged crystals and storm deposits
  - More common in areas with high electrical activity
  - Can spawn in underground caves during storms

### 2. Weather-Based Spawn Conditions

- [x] Implement storm-based spawn boosts

  - Increased spawn rates during electrical storms
  - Special storm-only spawns
  - Elite variants during severe weather

- [x] Configure time-of-day spawn modifiers
  - Increased spawn rates at night
  - Special spawns during thunderstorms
  - Reduced spawns during calm weather

### 3. Spawn Rules and Restrictions

- [x] Set spawn weight and group sizes

  - Balance spawn weights for each mob type
  - Configure minimum/maximum group sizes
  - Set spawn cap per chunk

- [x] Implement spawn protection
  - Prevent spawns too close to player structures
  - Add configurable spawn protection radius
  - Respect game difficulty settings

### 4. Configuration Files

- [x] Create spawn configuration files
  - JSON configuration for spawn rules
  - Support for modpack customization
  - In-game reload support

## Technical Implementation

- Use Forge's `SpawnPlacementRegisterEvent` for spawn rules
- Implement `ISpawnPredicate` for custom spawn conditions
- Add configuration options for all spawn parameters
- Ensure compatibility with other mods' spawn systems

## Testing Plan

- [x] Test spawn rates in different biomes
- [x] Verify weather-based spawn conditions
- [x] Check spawn protection and player proximity rules
- [x] Test performance with high spawn rates
- [x] Verify configuration reloading

## Dependencies

- Stormworld dimension (T45)
- Stormworld weather system (T46)
- Stormworld mob implementations (T47)
