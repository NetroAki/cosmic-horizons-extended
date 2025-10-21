# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build System

Multi-module Minecraft Forge mod using Architectury conventions:

- `./gradlew build` - Build all modules
- `./gradlew check` - Run Spotless formatting checks
- `./gradlew spotlessApply` - Auto-format all Java, JSON, JSON5, and Markdown files
- `./gradlew forge:runClient` - Launch Minecraft client for testing
- `./gradlew forge:runServer` - Launch dedicated server
- `./gradlew forge:runData` - Run data generators

### Module Structure

- **Root** - Aggregator project with shared Spotless configuration
- **common/** - Loader-neutral code (sources disabled, compiled via forge module)
- **forge/** - Forge-specific implementation (includes common sources via srcDir)

The forge module compiles common sources under Loom to resolve Minecraft classes. Certain unstable/incomplete subsystems are excluded via `exclude` directives in `forge/build.gradle`.

## Key Dependencies

Hard dependencies:

- **Minecraft Forge 1.20.1** (47.4.1)
- **GregTech CEu (GTCEu)** - Hard dependency for mineral processing and progression
- **Cosmic Horizons** or **Cosmos** - Planet discovery and dimension integration (strongly recommended)

Optional integrations:

- **TerraBlender** - Custom biome injection
- **JEI** - Recipe/resource information displays
- **KubeJS** - Scripting support
- **AzureLib** - Animated entity models

## Architecture Overview

### Dual Main Classes

- `forge/.../CHEX.java` - Primary mod entrypoint, handles planet/travel/progression systems
- `common/.../CosmicHorizonsExpanded.java` - Entity/structure/world gen systems (being consolidated)

### Core Systems

**Planet Registry & Discovery**

- `PlanetRegistry.java` - Central registry of all planets with tier/suit requirements
- `PlanetDiscovery.java` - Auto-discovers Cosmic Horizons planets at server start
- `PlanetOverrides.java` + `chex-planets.json5` - Runtime overrides for planet properties

**Travel & Progression**

- `TravelGraph.java` - Defines valid rocket tier → planet mappings
- `NoduleTiers.java` - T1-T10 rocket nodule tiers
- `SuitTiers.java` - T1-T5 environmental suit tiers
- `FuelRegistry.java` - Rocket fuel types and tier mappings
- `LaunchHooks.java` - Launch validation (nodule tier, suit, fuel, cargo mass, destination keys)

**Configuration System**

- `chex-planets.json5` - Per-planet nodule tier, suit tag, display name overrides
- `chex-minerals.json5` - GTCEu mineral distributions per planet/biome
- `fallback_ores.json5` - Fallback ore blocks (not used; GTCEu is hard dependency)
- `chex-suit-hazards.json5` - Environmental hazard rules per dimension
- `chex-visual-filters.json5` - Per-dimension fog tint/effects
- `cosmic_horizons_extended-common.toml` - Behavior toggles, fuel mappings

**Mineral/Resource Integration**

- `MineralGenerationRegistry.java` - Loads mineral config, generates worldgen JSONs
- `MineralsRuntime.java` - Runtime config cache with hot-reload support
- `GregTechBridge.java` - GTCEu integration bridge
- Mineral configs support tier-based progression (MV, HV, EV, etc.)

**Boss & Progression**

- Boss core matrix system (data-driven progression gates)
- Boss controllers drop loot cores that unlock higher tiers
- Some destinations require specific boss cores (e.g., Sovereign Heart for Neutron Forge)

**Hazards & Environment**

- Suit hazard system validates player suit tier against dimension requirements
- Bounces players or applies debuffs if insufficient protection
- Sandstorm visibility, Crystalis visual effects, Dyson/Neutron Forge hazards

### Package Organization

**Registry packages** (`forge/.../registry/`):

- `blocks/` - Block registrations per planet (ArrakisBlocks, PandoraBlocks, etc.)
- `items/` - Item registrations per planet
- `entities/` - Entity type registrations
- `block_entity/` - Block entity registrations
- `biome/`, `dimension/` - TerraBlender/worldgen registrations

**Planet implementation** (`common/.../entity/`, `common/.../world/`):

- `entity/arrakis/`, `entity/aqua_mundus/`, etc. - Per-planet entities
- `world/arrakis/`, `world/aqua_mundus/`, etc. - Per-planet worldgen features
- `entity/boss/` - Boss entities (Stellar Avatar, Ocean Sovereign, etc.)

**Commands** (`forge/.../commands/`):

- `ChexCommands.java` - All `/chex` subcommands
- `LoreCommand.java` - Lore/quest system commands

## Development Workflow

### Configuration Hot-Reload

- `/chex reload` - Reloads planets, travel graph, minerals, fallback ores
- `/chex minerals reload` - Reloads just mineral distributions
- Config changes take effect without server restart

### Testing & Validation

- `/chex dumpPlanets` - Writes `run/chex_planets_dump.json` with all discovered planets
- `/chex travel <tier>` - Lists reachable planets for a nodule tier
- `/chex minerals <planetId>` - Dumps mineral config for a planet
- `/chex launch <planetId>` - Validates and teleports player (checks nodule/suit/fuel/keys)
- `python scripts/validate_json.py` - Validates all JSON files in project

### Launch Validation Flow

1. Check nodule tier vs planet requirements
2. Check suit tier tag (e.g., `chex:suits/suit3`)
3. Check planet discovery status
4. Check fuel type and volume in rocket
5. Check special unlock items (e.g., boss cores)
6. Apply cargo mass and fuel quality modifiers
7. Teleport or send localized denial message

### Entity/World Generation Exclusions

Many subsystems are temporarily excluded from compilation in `forge/build.gradle` under `sourceSets.main.java.exclude`. Check this list before debugging missing classes:

- Quest system (`quest/**`)
- Client systems (`client/**`, `CHEXClient.java`)
- Legacy configs (`config/legacy/**`)
- Incomplete entities (Glowbeast, CliffHunter, SkyGrazer, boss entities)
- World systems (`world/**`, biome providers, structure systems)

Re-enable by removing relevant `exclude` lines after implementation stabilizes.

## Data Generation

Worldgen JSONs (configured/placed features, biome modifiers) are generated from `chex-minerals.json5`:

- Run `./gradlew forge:runData` to regenerate
- Biome modifiers default to `#minecraft:is_overworld` if no biomes specified
- Generated files go to `forge/src/main/generated/`

## Common Commands

### Planet Management

```
/chex dumpPlanets          # Export all discovered planets
/chex reload               # Reload all configs
/chex travel <tier>        # List planets reachable at tier
/chex launch <planetId>    # Validate and teleport to planet
```

### Progression Management

```
/chex unlock rocket <tier> <player>   # Unlock rocket tier for player
/chex unlock suit <tier> <player>     # Unlock suit tier for player
/chex unlock planet <id> <player>     # Unlock planet for player
```

### Debug Tools

```
/chex lagprofiler start    # Start lag profiler (20ms sampling)
/chex lagprofiler stop     # Stop and dump profile
/chex lagprofiler status   # Check profiler status
/chex minerals <planetId>  # Dump mineral config for planet
```

## Code Style

Spotless enforces:

- Google Java Format with long string reflow
- Import ordering and unused import removal
- Prettier for JSON/JSON5/Markdown

Run `./gradlew spotlessApply` before committing.

## Important Notes

- **GTCEu is required**: Do not add fallback ore processing; GTCEu integration is intentional
- **Cosmic Horizons strongly recommended**: Mod works standalone but designed for CH integration
- **Two main classes**: `CHEX.java` (primary) and `CosmicHorizonsExpanded.java` (being consolidated)
- **Windows paths**: Repository uses Windows-style paths (`C:\Users\...`); use quotes in Bash commands
- **Java 17 required**: Set `org.gradle.java.home` in `gradle.properties` to JDK 17 path
- **Excluded code**: Many subsystems excluded in `forge/build.gradle`; check before debugging missing classes

## Planet Tiers & Progression

Planets organized by nodule tier (T1-T10+):

- **T3**: Pandora (5 biomes: Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands)
- **T4**: Arrakis (5 biomes: Great Dunes, Spice Mines, Polar Ice Caps, Sietch Strongholds, Stormlands)
- **T5**: Alpha Centauri A (Photosphere/Corona zones with radiation hazards)
- **T6**: Kepler-452b (Forest, Highlands, Rivers, Meadow, Scrub biomes)
- **T7**: Aqua Mundus (Ocean world with pressure/oxygen mechanics)
- **T8**: Inferno Prime (Lava seas, ash wastes, extreme heat)
- **T9**: Crystalis (Diamond fields, frosted plains, cryogenic hazards)
- **T10**: Stormworld (Layered biomes, lightning/gravity mechanics)
- **T11+**: Megastructures (Ringworld, Exotica, Torus World, Hollow World, Dyson Swarm, Neutron Star Forge)

Boss cores unlock higher tiers and special destinations.

## JEI Integration

JEI plugin registers:

- **Rocket Assembly** category - Shows T1-T5 nodule recipes/requirements
- **Planet Resources** category - Displays mineral samples per planet

Compile-only dependency; not required at runtime.
