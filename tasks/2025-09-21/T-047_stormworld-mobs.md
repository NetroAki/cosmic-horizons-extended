# T47: Stormworld Mobs Implementation

## Overview

Create unique mobs that inhabit the Stormworld dimension, focusing on storm and electricity themes. These mobs should interact with the weather system and provide unique challenges and rewards.

## Mob List

### 1. Stormcaller

- **Type**: Hostile
- **Behavior**:
  - Summons lightning strikes during storms
  - Gains strength from electrical storms
  - Can create small storm clouds that follow players
- **Drops**:
  - Storm Core (used in electrical equipment)
  - Charged Essence (crafting material)

### 2. Windrider

- **Type**: Neutral/Flying
- **Behavior**:
  - Glides on wind currents
  - More agile during storms
  - Can create wind gusts to knock back players
- **Drops**:
  - Gale Feathers (for flight-related items)
  - Wind Essence (for potions)

### 3. Static Jelly

- **Type**: Passive/Hostile when provoked
- **Behavior**:
  - Absorbs electricity from the environment
  - Can discharge electricity when threatened
  - Glows during electrical storms
- **Drops**:
  - Electric Gel (conductive material)
  - Glowing Core (light source)

## Implementation Tasks

### 1. Entity Classes

- [ ] Create `StormcallerEntity` class
- [x] Create `WindriderEntity` class
- [x] Create `StaticJellyEntity` class

### 2. AI and Behavior

- [ ] Implement storm-sensing behavior
- [ ] Add weather-based stat modifiers
- [ ] Create unique movement patterns

### 3. Special Abilities

- [ ] Lightning summoning for Stormcaller
- [ ] Wind gust ability for Windrider
- [ ] Electric discharge for Static Jelly

### 4. Drops and Loot Tables

- [ ] Configure mob drops
- [ ] Add special storm-related loot
- [ ] Implement rare drops for elite variants

### 5. Spawn Conditions

- [ ] Configure biome-specific spawning
- [ ] Add weather-based spawn conditions
- [ ] Balance spawn rates

## Technical Details

- Use Forge's entity system
- Implement `IAnimatable` for smooth animations
- Use data-driven approach for stats and abilities
- Add configuration options for all parameters

## Dependencies

- Stormworld dimension (T45)
- Stormworld weather system (T46)
