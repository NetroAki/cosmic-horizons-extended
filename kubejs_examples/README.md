# CHEX KubeJS Integration

This directory contains examples and documentation for using CHEX's tier system with KubeJS for recipe gating and conditional crafting.

## Overview

CHEX provides a comprehensive tier-based progression system with:

- **10 Rocket Tiers** (T1-T10) for space travel
- **5 Suit Tiers** (I-V) for environmental protection
- **Planet Discovery** system for unlocking new destinations
- **Mineral Discovery** system for unlocking new materials

## Available Functions

### Player Tier Checking

- `PlayerTier.hasRocketTier(player, tier)` - Check if player has unlocked rocket tier
- `PlayerTier.hasSuitTier(player, tier)` - Check if player has unlocked suit tier
- `PlayerTier.getRocketTier(player)` - Get player's current rocket tier
- `PlayerTier.getSuitTier(player)` - Get player's current suit tier

### Discovery Checking

- `PlayerTier.hasDiscoveredPlanet(player, planetId)` - Check if player has discovered planet
- `PlayerTier.hasDiscoveredMineral(player, mineralId)` - Check if player has discovered mineral

### Unlocking Functions

- `PlayerTier.unlockPlanet(player, planetId)` - Unlock a planet for a player
- `PlayerTier.unlockMineral(player, mineralId)` - Unlock a mineral for a player
- `PlayerTier.unlockRocketTier(player, tier)` - Unlock a rocket tier for a player
- `PlayerTier.unlockSuitTier(player, tier)` - Unlock a suit tier for a player

### Information Functions

- `PlayerTier.getDiscoveredPlanets(player)` - Get all discovered planets for a player
- `PlayerTier.getDiscoveredMinerals(player)` - Get all discovered minerals for a player
- `PlayerTier.canAccessPlanet(player, planetId)` - Check if player can access a planet
- `PlayerTier.getRequiredRocketTier(planetId)` - Get required rocket tier for a planet
- `PlayerTier.getRequiredSuitTier(planetId)` - Get required suit tier for a planet

## Available Conditions

### Basic Tier Conditions

- `kubejs:player_has_tier` - Player has specific tier (rocket or suit)
- `kubejs:player_min_rocket_tier` - Player has minimum rocket tier
- `kubejs:player_min_suit_tier` - Player has minimum suit tier
- `kubejs:player_tier_range` - Player has tier within range

### Discovery Conditions

- `kubejs:player_has_discovered_planet` - Player has discovered specific planet
- `kubejs:player_has_discovered_mineral` - Player has discovered specific mineral

## Example Usage

### Basic Recipe Gating

```javascript
// T2 Rocket - requires T2 rocket tier
event
  .shaped("cosmic_horizons_extended:rocket_t2", [" S ", "S S", "S S"], {
    S: "minecraft:iron_ingot",
  })
  .condition("kubejs:player_has_tier", {
    tier: 2,
    type: "rocket",
  });
```

### Complex Conditions

```javascript
// Quantum Fuel Tank - requires T8 rocket AND discovered planet
event
  .shaped("cosmic_horizons_extended:quantum_fuel_tank", ["DDD", "D D", "DDD"], {
    D: "minecraft:netherite_ingot",
  })
  .condition("kubejs:player_has_tier", {
    tier: 8,
    type: "rocket",
  })
  .condition("kubejs:player_has_discovered_planet", {
    planet: "cosmic_horizons_extended:exotica",
  });
```

### Tier Range Conditions

```javascript
// Advanced equipment for mid-game players
event
  .shaped(
    "cosmic_horizons_extended:advanced_equipment",
    ["III", "I I", "III"],
    {
      I: "minecraft:gold_ingot",
    }
  )
  .condition("kubejs:player_tier_range", {
    minTier: 3,
    maxTier: 7,
    type: "rocket",
  });
```

## Integration with Other Mods

### GregTech Integration

```javascript
// Draconium Alloy - requires discovered draconium mineral
event
  .shaped("cosmic_horizons_extended:draconium_alloy", ["DDD", "D D", "DDD"], {
    D: "gtceu:draconium_ingot",
  })
  .condition("kubejs:player_has_discovered_mineral", {
    mineral: "draconium",
  });
```

### Cosmic Horizons Integration

```javascript
// Mars-specific equipment
event
  .shaped("cosmic_horizons_extended:mars_equipment", ["MMM", "M M", "MMM"], {
    M: "cosmos:mars_rock",
  })
  .condition("kubejs:player_has_discovered_planet", {
    planet: "cosmos:mars",
  });
```

## Advanced Features

### Custom Events

CHEX provides custom events that KubeJS can listen to:

- `chex:rocket_tier_unlocked` - When a player unlocks a rocket tier
- `chex:suit_tier_unlocked` - When a player unlocks a suit tier
- `chex:planet_discovered` - When a player discovers a planet
- `chex:mineral_discovered` - When a player discovers a mineral
- `chex:rocket_launch_attempt` - When a player attempts to launch a rocket
- `chex:rocket_launch_success` - When a rocket launch succeeds
- `chex:rocket_launch_failure` - When a rocket launch fails

### Quest Integration

CHEX integrates with popular quest mods:

- **FTB Quests** - Custom conditions and rewards
- **Quest Progression** - Track tier unlocks and discoveries
- **Custom Rewards** - Unlock tiers, planets, and minerals
- **Condition Checking** - Verify player progress

### Dynamic Progression

- **Planet Discovery Triggers** - Craft items, reach coordinates, complete quests
- **Mineral Discovery** - Mining, crafting, exploration
- **Tier Rewards** - Custom items, effects, and unlocks
- **Suit Enforcement** - Gradual damage, environmental effects
- **Fuel Consumption** - Distance-based, tier-based fuel costs

## Progression Flow

1. **T1-T2**: Basic rockets, Suit I, Moon exploration
2. **T3-T4**: Advanced rockets, Suit II, Mars/Pandora exploration
3. **T5-T6**: Heavy rockets, Suit III, Outer planets
4. **T7-T8**: Exotic rockets, Suit IV, Exoplanets
5. **T9-T10**: Ultimate rockets, Suit V, Megastructures

## Tips

- Use `PlayerTier.getRocketTier(player)` to create dynamic recipes
- Combine multiple conditions for complex gating
- Use planet discovery for location-specific recipes
- Use mineral discovery for material-specific recipes
- Consider using tier ranges for mid-game content

## Troubleshooting

- Make sure KubeJS is installed and loaded
- Check that CHEX is loaded before KubeJS
- Verify planet/mineral IDs match exactly
- Use `/chex getTier` command to check player tiers
- Use `/chex status` command to check player progress
