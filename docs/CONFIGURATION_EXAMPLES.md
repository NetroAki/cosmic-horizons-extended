# Configuration Examples - Cosmic Horizons Extended

**Version**: 1.0  
**Last Updated**: 2025-10-17

## Overview

CHEX uses JSON5 configuration files for data-driven planet, mineral, and progression management. This document provides examples and best practices.

---

## Configuration Files

| File                        | Purpose                        | Location                                                         |
| --------------------------- | ------------------------------ | ---------------------------------------------------------------- |
| `chex-planets.json5`        | Planet property overrides      | `forge/src/main/resources/data/cosmic_horizons_extended/config/` |
| `chex-minerals.json5`       | GTCEu mineral distributions    | `forge/src/main/resources/data/cosmic_horizons_extended/config/` |
| `chex-suit-hazards.json5`   | Dimension hazard rules         | `forge/src/main/resources/data/cosmic_horizons_extended/config/` |
| `chex-visual-filters.json5` | Per-dimension fog/tint effects | `forge/src/main/resources/data/cosmic_horizons_extended/config/` |
| `config.toml`               | Runtime behavior toggles       | `config/cosmic_horizons_extended-common.toml`                    |

---

## 1. Planet Overrides (`chex-planets.json5`)

### Purpose

Override default planet properties (nodule tier, suit tier, display name) discovered from Cosmic Horizons.

### Example: Override Pandora Properties

```json5
{
  planets: [
    {
      id: "cosmic_horizons:pandora",
      noduleTier: 5, // Rocket tier required (default: 1)
      suitTag: "chex:suits/suit1", // Environmental suit tier (default: none)
      displayName: "Pandora - Bioluminescent Moon", // Override display name
    },
  ],
}
```

### Example: Override Multiple Planets

```json5
{
  planets: [
    {
      id: "cosmic_horizons:pandora",
      noduleTier: 5,
      suitTag: "chex:suits/suit1",
      displayName: "Pandora",
    },
    {
      id: "cosmic_horizons_extended:kepler_452b",
      noduleTier: 8,
      suitTag: null, // No suit required (Earth-like)
      displayName: "Kepler-452b (Terrestrial)",
    },
    {
      id: "cosmic_horizons_extended:aqua_mundus",
      noduleTier: 6,
      suitTag: "chex:suits/suit2",
      displayName: "Aqua Mundus (Ocean World)",
    },
  ],
}
```

### Properties

- **`id`** (string, required): Planet dimension ID (e.g., `"cosmic_horizons:pandora"`)
- **`noduleTier`** (int, optional): Rocket tier requirement (1-10+, default: 1)
- **`suitTag`** (string, optional): Suit tier tag (e.g., `"chex:suits/suit3"`, default: null)
- **`displayName`** (string, optional): Human-readable name (default: auto-generated from ID)

### Hot-Reload

Run `/chex reload` to apply changes without server restart.

---

## 2. Mineral Distribution (`chex-minerals.json5`)

### Purpose

Configure GTCEu ore vein distributions per planet and biome.

### Example: Basic Ore Vein

```json5
{
  minerals: [
    {
      planetId: "cosmic_horizons:pandora",
      biomes: [], // Empty = all biomes on planet
      veinType: "gtceu:copper_tin",
      minY: 10,
      maxY: 80,
      size: 60,
      rarity: 160,
      tier: "MV",
    },
  ],
}
```

### Example: Biome-Specific Distribution

```json5
{
  minerals: [
    {
      planetId: "cosmic_horizons_extended:arrakis",
      biomes: ["cosmic_horizons_extended:arrakis_spice_mines"],
      veinType: "gtceu:bauxite",
      minY: 30,
      maxY: 60,
      size: 80,
      rarity: 120,
      tier: "HV",
    },
    {
      planetId: "cosmic_horizons_extended:arrakis",
      biomes: ["cosmic_horizons_extended:arrakis_dunes"],
      veinType: "gtceu:ilmenite",
      minY: 10,
      maxY: 50,
      size: 50,
      rarity: 200,
      tier: "HV",
    },
  ],
}
```

### Example: High-Tier Planet Resources

```json5
{
  minerals: [
    {
      planetId: "cosmic_horizons_extended:aqua_mundus",
      biomes: [], // All biomes
      veinType: "gtceu:sheldonite",
      minY: -20,
      maxY: 40,
      size: 100,
      rarity: 80,
      tier: "EV",
    },
    {
      planetId: "cosmic_horizons_extended:inferno_prime",
      biomes: ["cosmic_horizons_extended:inferno_magma_caverns"],
      veinType: "gtceu:pitchblende",
      minY: 0,
      maxY: 50,
      size: 120,
      rarity: 60,
      tier: "IV",
    },
  ],
}
```

### Properties

- **`planetId`** (string, required): Planet dimension ID
- **`biomes`** (array, optional): List of biome IDs (empty = all biomes)
- **`veinType`** (string, required): GTCEu vein type (e.g., `"gtceu:copper_tin"`)
- **`minY`** (int, required): Minimum Y-level for vein spawning
- **`maxY`** (int, required): Maximum Y-level for vein spawning
- **`size`** (int, required): Vein cluster size (larger = more ore per vein)
- **`rarity`** (int, required): Rarity factor (higher = less common)
- **`tier`** (string, required): Progression tier (`"MV"`, `"HV"`, `"EV"`, `"IV"`, `"LuV"`, `"ZPM"`)

### Vein Types (Common GTCEu Examples)

| Vein Type           | Ores                          | Best Tier |
| ------------------- | ----------------------------- | --------- |
| `gtceu:copper_tin`  | Copper, Tin                   | MV        |
| `gtceu:coal`        | Coal, Lignite                 | MV        |
| `gtceu:iron`        | Iron, Magnetite               | HV        |
| `gtceu:bauxite`     | Bauxite, Ilmenite, Aluminium  | HV        |
| `gtceu:sheldonite`  | Sheldonite (Platinum/Iridium) | EV        |
| `gtceu:pitchblende` | Uranium, Thorium              | IV        |
| `gtceu:scheelite`   | Tungsten, Scheelite           | LuV       |
| `gtceu:naquadah`    | Naquadah, Enriched Naquadah   | ZPM       |

### Hot-Reload

Run `/chex minerals reload` to apply changes without server restart.

---

## 3. Suit Hazards (`chex-suit-hazards.json5`)

### Purpose

Define environmental hazards and required suit tiers per dimension.

### Example: Basic Hazard Configuration

```json5
{
  hazards: [
    {
      dimensionId: "cosmic_horizons:pandora",
      requiredSuitTag: "chex:suits/suit1",
      bounceOnFail: true,
      message: "chex.hazard.pandora.toxic_spores",
    },
    {
      dimensionId: "cosmic_horizons_extended:inferno_prime",
      requiredSuitTag: "chex:suits/suit3",
      bounceOnFail: true,
      message: "chex.hazard.inferno.extreme_heat",
    },
  ],
}
```

### Example: Advanced Hazard with Debuffs

```json5
{
  hazards: [
    {
      dimensionId: "cosmic_horizons_extended:crystalis",
      requiredSuitTag: "chex:suits/suit4",
      bounceOnFail: false, // Allow entry, apply debuff
      message: "chex.hazard.crystalis.frostbite",
      effects: [
        {
          effectId: "minecraft:slowness",
          amplifier: 2,
          duration: 200, // 10 seconds (ticks)
        },
        {
          effectId: "minecraft:mining_fatigue",
          amplifier: 1,
          duration: 200,
        },
      ],
    },
  ],
}
```

### Properties

- **`dimensionId`** (string, required): Dimension resource location
- **`requiredSuitTag`** (string, required): Item tag for required suit (e.g., `"chex:suits/suit2"`)
- **`bounceOnFail`** (boolean, optional): If true, teleport player back (default: true)
- **`message`** (string, optional): Localization key for denial message
- **`effects`** (array, optional): Status effects to apply if `bounceOnFail` is false

### Hot-Reload

Run `/chex reload` to apply changes without server restart.

---

## 4. Visual Filters (`chex-visual-filters.json5`)

### Purpose

Define fog color, tint, and visual effects for dimensions (client-side).

### Example: Sandstorm Effect (Arrakis)

```json5
{
  filters: [
    {
      dimensionId: "cosmic_horizons_extended:arrakis",
      fogColor: [0.8, 0.5, 0.2], // RGB (orange fog)
      fogDensity: 0.05,
      skyTint: [1.0, 0.7, 0.4], // Orange/yellow sky
      enableSandstorm: true,
    },
  ],
}
```

### Example: Underwater Tint (Aqua Mundus)

```json5
{
  filters: [
    {
      dimensionId: "cosmic_horizons_extended:aqua_mundus",
      fogColor: [0.2, 0.4, 0.6], // Deep blue
      fogDensity: 0.08,
      skyTint: [0.3, 0.5, 0.7],
      enableUnderwaterEffect: true,
    },
  ],
}
```

### Properties

- **`dimensionId`** (string, required): Dimension resource location
- **`fogColor`** (array [float, float, float], optional): RGB fog color (0.0-1.0)
- **`fogDensity`** (float, optional): Fog thickness (0.0-1.0)
- **`skyTint`** (array [float, float, float], optional): Sky color tint
- **`enableSandstorm`** (boolean, optional): Enable sandstorm particle overlay
- **`enableUnderwaterEffect`** (boolean, optional): Apply underwater distortion

### Client-Side Requirement

Visual filters require client-side code (`CHEXClient.java`), currently excluded from build. This is a future enhancement.

---

## 5. Runtime Config (`cosmic_horizons_extended-common.toml`)

### Purpose

Toggle runtime behaviors and fuel mappings.

### Example: Full Configuration

```toml
[general]
# Enable planet discovery auto-registration
enablePlanetDiscovery = true

# Enable mineral generation
enableMineralGeneration = true

# Enable suit hazard checks
enableSuitHazards = true

# Enable boss encounters
enableBosses = true

[fuel]
# Fuel type mappings for rocket tiers
fuelTypeT1 = "minecraft:water"
fuelTypeT2 = "gtceu:rocket_fuel"
fuelTypeT3 = "gtceu:dense_hydrazine_fuel"
fuelTypeT4 = "gtceu:liquid_hydrogen"
fuelTypeT5 = "gtceu:naquadah_fuel"

[debug]
# Enable verbose logging
verboseLogging = false

# Enable lag profiler sampling
enableLagProfiler = false
```

### Properties

#### General Section

- **`enablePlanetDiscovery`** (boolean): Auto-register Cosmic Horizons planets (default: true)
- **`enableMineralGeneration`** (boolean): Generate GTCEu ore veins (default: true)
- **`enableSuitHazards`** (boolean): Enforce suit requirements (default: true)
- **`enableBosses`** (boolean): Spawn boss entities (default: true)

#### Fuel Section

- **`fuelTypeT1-T5`** (string): Fluid resource location for each rocket tier

#### Debug Section

- **`verboseLogging`** (boolean): Enable detailed logging (default: false)
- **`enableLagProfiler`** (boolean): Enable `/chex lagprofiler` command (default: false)

### Hot-Reload

Config changes require server restart.

---

## Best Practices

### 1. Testing Configurations

Always test after modifying configs:

```bash
./gradlew check                          # Validate build
python scripts/validate_json.py          # Validate JSON5 files
./gradlew :forge:runServer               # Test in-game
```

### 2. Mineral Rarity Tuning

- **Low Rarity (50-100)**: Common ores (copper, iron)
- **Medium Rarity (100-200)**: Uncommon ores (bauxite, nickel)
- **High Rarity (200-400)**: Rare ores (platinum, iridium)
- **Very High Rarity (400+)**: Extremely rare (naquadah, draconium)

### 3. Y-Level Guidelines

- **Surface Ores**: `minY: 50, maxY: 120`
- **Mid-Depth Ores**: `minY: 10, maxY: 80`
- **Deep Ores**: `minY: -64, maxY: 30`
- **Underwater Ores**: `minY: -20, maxY: 60` (for ocean planets)

### 4. Suit Tier Progression

- **Suit T1**: Basic hazards (toxic air, spores)
- **Suit T2**: Moderate hazards (heat, cold)
- **Suit T3**: Severe hazards (extreme heat/cold)
- **Suit T4**: Extreme hazards (radiation, frostbite)
- **Suit T5**: Megastructures (star surfaces, black holes)

### 5. Backup Before Editing

Always back up config files before making changes:

```bash
cp forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5 chex-minerals.json5.backup
```

---

## Common Issues

### Issue: Ores Not Generating

**Cause**: Biome ID mismatch or rarity too high

**Solution**:

1. Check biome IDs: `/locate biome cosmic_horizons_extended:arrakis_dunes`
2. Lower rarity value (e.g., 160 → 80)
3. Reload config: `/chex minerals reload`

### Issue: Players Bounce from Planet

**Cause**: Missing suit tier

**Solution**:

1. Check suit requirement: `/chex dumpPlanets`
2. Give suit: `/give @p cosmic_horizons_extended:suit_tier_2{SuitTag:"chex:suits/suit2"}`
3. Or disable hazard in `chex-suit-hazards.json5`

### Issue: Config Changes Not Applied

**Cause**: No hot-reload performed

**Solution**:

- Run `/chex reload` (planets, hazards, visual filters)
- Run `/chex minerals reload` (mineral distributions)
- Restart server (runtime config changes)

---

## References

- **Planet Registry**: `forge/src/main/java/com/netroaki/chex/core/planet/PlanetRegistry.java`
- **Mineral Registry**: `forge/src/main/java/com/netroaki/chex/core/minerals/MineralGenerationRegistry.java`
- **Fuel Registry**: `forge/src/main/java/com/netroaki/chex/core/fuel/FuelRegistry.java`
- **Commands**: `forge/src/main/java/com/netroaki/chex/commands/ChexCommands.java`

---

**End of Document**
