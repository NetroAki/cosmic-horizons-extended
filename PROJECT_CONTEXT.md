# Cosmic Horizons Extended (CHEX) - Project Context

**Version**: 0.5.0  
**Minecraft**: 1.20.1 (Forge 47.4.1)  
**Last Updated**: 2025-10-17

## Project Overview

**Cosmic Horizons Extended (CHEX)** is a comprehensive Minecraft 1.20.1 mod that extends the Cosmic Horizons space exploration framework with 7+ new planets, GTCEu ore progression, multi-phase bosses, and data-driven configuration systems. Built on Architectury for multi-loader compatibility (currently Forge-only).

## Current State (v0.5.0)

### Completed Systems

- ✅ **7+ Planets**: Pandora, Arrakis, Kepler-452b, Aqua Mundus, Inferno Prime, Alpha Centauri A, Stormworld, Aurelia Ringworld, Crystalis
- ✅ **30+ Custom Blocks**: Planet-specific terrain, decorations, resources
- ✅ **2 Multi-Phase Bosses**: Spore Tyrant (2 phases), Worldheart Avatar (3 phases)
- ✅ **Planet Discovery**: Auto-discovers Cosmic Horizons/Cosmos planets
- ✅ **Travel Graph**: Tier-based planet accessibility (T1-T10+)
- ✅ **GTCEu Integration**: Mineral vein generation with hot-reload
- ✅ **Suit Hazard System**: Environmental protection per dimension
- ✅ **Player Progression**: Persistent capability tracking
- ✅ **Boss Loot Cores**: Progression gating for special destinations
- ✅ **Configuration Hot-Reload**: Runtime config updates
- ✅ **Comprehensive Documentation**: 4 docs, changelog, updated README

### In Progress / Future

- ⏳ **Client-Side Systems**: Skyboxes, particles, shaders, visual feedback (code excluded from build)
- ⏳ **Additional Fauna**: 15-20 custom entities across planets
- ⏳ **Additional Bosses**: Verdant Colossus, Ocean Sovereign, Magma Titan
- ⏳ **Special Mechanics**: Pressure/oxygen (Aqua Mundus), dynamic weather (Stormworld), solar flares (Alpha Centauri A)
- ⏳ **Performance Optimization**: Chunk generation, entity AI

## Project Structure

```
Cosmic_Horizons_Expanded/
├── common/                          # Loader-neutral code
│   ├── src/main/java/               # Shared Java code
│   │   └── com/netroaki/chex/       # Main package
│   │       ├── entity/              # Entity implementations (per-planet)
│   │       ├── world/               # World generation (per-planet)
│   │       └── CosmicHorizonsExpanded.java  # Secondary main class
├── forge/                           # Forge-specific implementation
│   ├── src/main/java/               # Forge-specific code
│   │   └── com/netroaki/chex/       # Main package
│   │       ├── CHEX.java            # Primary mod entrypoint
│   │       ├── core/                # Core systems
│   │       │   ├── planet/          # Planet registry, discovery
│   │       │   ├── travel/          # Travel graph, validation
│   │       │   ├── fuel/            # Fuel registry
│   │       │   ├── minerals/        # Mineral generation
│   │       │   ├── player/          # Player capabilities
│   │       │   └── suit/            # Suit hazard system
│   │       ├── commands/            # /chex commands
│   │       ├── network/             # Client-server sync
│   │       ├── registry/            # Block, item, entity registrations
│   │       │   ├── blocks/          # Block registrations per planet
│   │       │   ├── items/           # Item registrations
│   │       │   ├── entities/        # Entity type registrations
│   │       │   └── block_entity/    # Block entity registrations
│   │       ├── integration/         # Mod integrations
│   │       │   ├── gtceu/           # GTCEu bridge
│   │       │   ├── terablender/     # TerraBlender regions (optional)
│   │       │   └── jei/             # JEI plugin (optional)
│   │       └── client/              # Client-side code (excluded from build)
│   └── src/main/resources/          # Forge resources
│       ├── assets/                  # Client assets
│       │   └── cosmic_horizons_extended/
│       │       ├── lang/            # Localizations
│       │       ├── textures/        # Block/item textures
│       │       └── models/          # Block/item models
│       └── data/                    # Data-driven content
│           └── cosmic_horizons_extended/
│               ├── config/          # JSON5 configs
│               ├── dimension/       # Dimension JSONs
│               ├── dimension_type/  # Dimension type JSONs
│               ├── worldgen/        # Biomes, features, structures
│               ├── loot_tables/     # Entity loot drops
│               └── recipes/         # Crafting recipes
├── scripts/                         # Utility scripts
│   ├── validate_json.py             # JSON validation tool
│   └── testing/                     # Testing scripts (future)
├── docs/                            # Documentation
│   ├── PLANET_DESIGNS.md            # Planet guide
│   ├── CONFIGURATION_EXAMPLES.md    # Config examples
│   ├── BOSS_ENCOUNTERS.md           # Boss strategies
│   └── MANUAL_TESTING_CHECKLIST.md  # Testing procedures
├── tasks/2025-09-21/                # Task tracking files
├── progress/                        # Progress reports
├── CHANGELOG.md                     # Version history
├── README.md                        # User-facing documentation
├── CLAUDE.md                        # AI pair programming guide
├── AGENTS.md                        # Agent role definitions
├── PROJECT_CONTEXT.md               # This file
├── build.gradle                     # Root build config
└── settings.gradle                  # Gradle project settings
```

## Planet Implementations

### Tier 3 Planets

#### Pandora (T3, Rocket Tier 5)

**Biomes**: 5 (Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands)

**Blocks**:

- Pandorite Stone (terrain)
- Biolume Moss (light level 7)
- Lumicoral (light level 10)
- Spore Soil (farmland-like)

**Entities**:

- Sporefly (neutral/hostile variants)
- Glowbeast (passive, placeholder)

**Bosses**:

- **Spore Tyrant** (2 phases): Spore clouds, Sporefly summons, regeneration
- **Worldheart Avatar** (3 phases): Guardian summons, rooting vines, enrage timer

**Loot Cores**:

- Pandoran Heart Seed (T6 progression)
- Worldheart Fragment (T10 progression, Neutron Forge access)

**Hazards**: Toxic spores (Suit T1 required)

**Status**: ✅ Complete (blocks, entities, bosses, hazards, audio)

---

### Tier 4 Planets

#### Arrakis (T4, Rocket Tier 6)

**Biomes**: 5 (Great Dunes, Spice Mines, Polar Ice Caps, Sietch Strongholds, Stormlands)

**Blocks**:

- Arrakite Sandstone (4 variants: regular, smooth, cut, chiseled)
- Spice Node (resource block)
- Crystalline Salt (decorative)
- Sietch Stone (shelter material)
- Stormglass (transparent)

**Flora**:

- Spice Cactus (desert biomes)
- Ice Reeds (polar ice caps)
- Desert Shrub (scrub biomes)

**Items**:

- Dried Spice (smelting product)

**Custom Sky**: Red/orange twilight gradient, foggy atmosphere (ArrakisSkyEffects)

**GTCEu Ores**: Bauxite, ilmenite (HV tier)

**Hazards**: Sandstorms (reduced visibility), extreme heat (Suit T2 required)

**Status**: ✅ Complete (blocks, flora, sky effects, ore gen)

---

#### Aqua Mundus (T4, Rocket Tier 6)

**Biomes**: 5 (Shallow Seas, Kelp Forests, Abyssal Trenches, Hydrothermal Vents, Ice Shelves)

**Blocks**:

- Aqua Vent Basalt (light level 5, volcanic vents)
- Aqua Manganese Nodule (ore block, XP drops 2-5)
- Aqua Luminous Kelp (light level 8, underwater lighting)
- Aqua Ice Shelf Slab (frozen surface areas)

**GTCEu Ores**: Platinum, iridium, palladium (EV tier)

**Hazards**: Pressure damage at depth (planned), oxygen management (planned)

**Fauna (Planned)**: Luminfish, Hydrothermal drones, Abyss leviathan, Tidal jelly

**Boss (Planned)**: **Ocean Sovereign** - Multi-head eel with sonic/whirlpool attacks

**Status**: ✅ Dimension + blocks complete, fauna/boss deferred

---

### Tier 5 Planets

#### Inferno Prime (T5, Rocket Tier 7)

**Biomes**: 5 (Lava Seas, Basalt Flats, Obsidian Isles, Ash Wastes, Magma Caverns)

**Blocks**:

- Inferno Stone (terrain)
- Inferno Ash (ground cover)

**GTCEu Ores**: Niobium, tantalum, uranium, osmium (IV tier)

**Hazards**: Extreme heat (Suit T3 required), ultrawarm environment, lava damage

**Fauna (Planned)**: Lava elementals, Ash wraiths, Magma serpents

**Boss (Planned)**: **Magma Titan** - Fire-based boss with lava mechanics

**Status**: ✅ Dimension complete, blocks/fauna/boss deferred

---

#### Alpha Centauri A (T5, Rocket Tier 7)

**Biomes**: 3 (Photosphere, Corona, Magnetosphere)

**Blocks**: None implemented yet

**Dimension Properties**:

- Maximum ambient light (1.0)
- Fixed time (perpetual noon)
- Ultrawarm environment
- 8x coordinate scaling (megastructure scale)
- End-like sky effects

**GTCEu Ores**: High-tier photonic materials (planned)

**Hazards**: Extreme heat and radiation (Suit T4+ required), solar flares (planned)

**Status**: ✅ Dimension complete, blocks/mechanics deferred

---

### Tier 6 Planets

#### Kepler-452b (T6, Rocket Tier 8)

**Biomes**: 5 (Temperate Forest, Highlands, River Valleys, Meadowlands, Rocky Scrub)

**Blocks**:

- Kepler Wood Log (rotated pillar block)
- Kepler Wood Leaves (decay mechanics)
- Kepler Moss (instant-break, decorative)
- Kepler Vines (instant-break, decorative)
- Kepler Blossom (light level 3, glowing flowers)

**GTCEu Ores**: Beryllium, fluorite, ruby, sapphire (LuV tier)

**Hazards**: Mild (Earth-like environment), standard mob spawning

**Fauna (Planned)**: River grazers, Meadow flutterwings, Scrub stalkers

**Boss (Planned)**: **Verdant Colossus** - Nature-based boss in ancient grove

**Status**: ✅ Dimension + blocks complete, fauna/boss deferred

---

### Tier 8 Planets

#### Aurelia Ringworld (T8, Rocket Tier 10)

**Biomes**: 6 (Plains, Forest, Mountains, River, Edge, Structural)

**Blocks**:

- Ringworld Wall (unbreakable boundary)
- Aurelian Wood (custom tree species)
- Aurelian Leaves (foliage)
- Aurelian Grass (ground cover)
- Aurelia Wall (structural material)
- Arc Scenery Block (client-rendered ring arc)

**Custom Mechanics**:

- **Gravity System**: Centripetal force simulation (entity rotation)
- **Teleportation**: Spaceport structures for fast travel
- **Custom Chunk Generator**: Ring structure terrain generation

**GTCEu Ores**: Draconium, awakened draconium (ZPM tier)

**Status**: ✅ Complete (dimension, blocks, mechanics)

---

#### Crystalis (T8, Rocket Tier 9)

**Biomes**: 3 (Diamond Fields, Frosted Plains, Ice Caves)

**Blocks**:

- Crystalis Crystal (decorative)
- Crystalis Clear Glass (transparent variant)
- Crystal Core Ore (special ore block)

**GTCEu Ores**: Beryllium, fluorite, ruby, sapphire (LuV tier)

**Hazards**: Frostbite (Suit T4 required), snow blindness, extreme cold

**Custom Mechanics (Planned)**: Crystal growth over time

**Status**: ✅ Complete (dimension, blocks)

---

### Tier 10+ Megastructures

#### Stormworld (T10+, Rocket Tier 10+)

**Biomes**: 4 (Lower Layer, Mid Layer, Upper Layer, Eye)

**Dimension Properties**:

- Extended 512-block height
- Layered atmosphere (biome distribution by Y-level)
- Dynamic weather system (planned)

**Hazards**: Electrical storms, lightning strikes (planned), wind forces (planned), layered gravity

**Status**: ✅ Dimension complete, mechanics deferred

---

## Core Systems Implementation

### Planet Discovery System

**Location**: `forge/src/main/java/com/netroaki/chex/core/planet/PlanetDiscovery.java`

**Features**:

- Auto-discovers Cosmic Horizons/Cosmos planets at server start
- Writes snapshot to `run/chex_discovered_planets.json`
- Integrates with PlanetRegistry for runtime access

**Status**: ✅ Complete

---

### Planet Registry

**Location**: `forge/src/main/java/com/netroaki/chex/core/planet/PlanetRegistry.java`

**Features**:

- Central registry for all planets (CH + CHEX)
- Supports runtime overrides via `chex-planets.json5`
- Hot-reload support (`/chex reload`)
- Stores nodule tier, suit tag, display name, dimension ID

**Status**: ✅ Complete

---

### Travel Graph

**Location**: `forge/src/main/java/com/netroaki/chex/core/travel/TravelGraph.java`

**Features**:

- Maps rocket nodule tiers (T1-T10+) to accessible planets
- Validates travel routes before launch
- Supports tier-based progression gating

**Status**: ✅ Complete

---

### Player Tier Capability

**Location**: `forge/src/main/java/com/netroaki/chex/core/player/PlayerTierCapability.java`

**Features**:

- Stores unlocked rocket tiers, suit tiers, discovered planets, mineral samples
- Persists across sessions (NBT serialization)
- Client-server synchronization via `CHEXNetwork`

**Status**: ✅ Complete

---

### Fuel Registry

**Location**: `forge/src/main/java/com/netroaki/chex/core/fuel/FuelRegistry.java`

**Features**:

- Maps fluid types to rocket tiers
- Supports fallback fluids (configurable)
- Hot-reload support

**Status**: ✅ Complete

---

### Mineral Generation Registry

**Location**: `forge/src/main/java/com/netroaki/chex/core/minerals/MineralGenerationRegistry.java`

**Features**:

- Loads ore distributions from `chex-minerals.json5`
- Generates worldgen JSONs (configured/placed features, biome modifiers)
- Hot-reload support (`/chex minerals reload`)
- Supports tier-based progression (MV, HV, EV, IV, LuV, ZPM)

**Status**: ✅ Complete

---

### Suit Hazard System

**Location**: `forge/src/main/java/com/netroaki/chex/core/suit/`

**Features**:

- Per-dimension suit tier checks
- Bounce-back or debuff mechanics
- Configurable via `chex-suit-hazards.json5`
- Hot-reload support

**Status**: ✅ Complete

---

### Boss Loot Core System

**Location**: `forge/src/main/java/com/netroaki/chex/core/boss/`

**Features**:

- Boss core matrix progression data (data-driven)
- Destination gating (e.g., Sovereign Heart for Neutron Forge)
- Loot core item registration
- Recipe integration

**Status**: ✅ Complete (framework)

---

## Commands

All commands are implemented in `forge/src/main/java/com/netroaki/chex/commands/ChexCommands.java`:

### Planet Management

- `/chex dumpPlanets` - Export discovered planets to JSON
- `/chex travel <tier>` - List accessible planets at rocket tier
- `/chex launch <planetId>` - Validate and teleport to planet
- `/chex reload` - Reload planet/travel/mineral/hazard configs

### Progression Management

- `/chex unlock rocket <tier> <player>` - Unlock rocket tier
- `/chex unlock suit <tier> <player>` - Unlock suit tier
- `/chex unlock planet <id> <player>` - Unlock planet discovery

### Resource Management

- `/chex minerals <planetId>` - Display mineral config
- `/chex minerals reload` - Reload mineral distributions

### Debug Tools

- `/chex lagprofiler start/stop/status` - Performance profiling

**Status**: ✅ All commands implemented and tested

---

## Configuration Files

### `chex-planets.json5`

**Location**: `forge/src/main/resources/data/cosmic_horizons_extended/config/`

**Purpose**: Per-planet property overrides (nodule tier, suit tag, display name)

**Hot-Reload**: `/chex reload`

**Status**: ✅ Complete

---

### `chex-minerals.json5`

**Location**: `forge/src/main/resources/data/cosmic_horizons_extended/config/`

**Purpose**: GTCEu ore vein distributions per planet/biome

**Hot-Reload**: `/chex minerals reload`

**Status**: ✅ Complete

---

### `chex-suit-hazards.json5`

**Location**: `forge/src/main/resources/data/cosmic_horizons_extended/config/`

**Purpose**: Environmental hazard rules per dimension

**Hot-Reload**: `/chex reload`

**Status**: ✅ Complete

---

### `chex-visual-filters.json5`

**Location**: `forge/src/main/resources/data/cosmic_horizons_extended/config/`

**Purpose**: Per-dimension fog tint/effects (client-side)

**Hot-Reload**: `/chex reload`

**Status**: ⏳ Config exists, client code excluded from build

---

### `cosmic_horizons_extended-common.toml`

**Location**: `config/cosmic_horizons_extended-common.toml`

**Purpose**: Runtime behavior toggles, fuel mappings

**Hot-Reload**: Requires server restart

**Status**: ✅ Complete

---

## Dependencies

### Hard Dependencies

- **Minecraft Forge 1.20.1** (47.4.1+)
- **Java 17** (build and runtime)
- **GregTech CEu (GTCEu)** - Ore processing and progression

### Strongly Recommended

- **Cosmic Horizons** or **Cosmos** - Planet discovery integration

### Optional Integrations

- **TerraBlender** - Custom biome injection (implemented, optional)
- **JEI** - Recipe/resource displays (implemented, optional)
- **KubeJS** - Scripting support (compatible, not directly integrated)
- **FTB Quests** - Quest integration via commands (compatible)
- **AzureLib** - Animated entity models (planned, not yet integrated)

---

## Documentation

- **`docs/PLANET_DESIGNS.md`**: Comprehensive guide (7 planets, biomes, resources, hazards)
- **`docs/CONFIGURATION_EXAMPLES.md`**: Config examples, best practices
- **`docs/BOSS_ENCOUNTERS.md`**: Boss strategies, mechanics, loot drops
- **`docs/MANUAL_TESTING_CHECKLIST.md`**: Testing procedures (90+ checks)
- **`CHANGELOG.md`**: Version history, feature changelog
- **`README.md`**: User-facing documentation
- **`CLAUDE.md`**: AI pair programming guide
- **`AGENTS.md`**: Agent role definitions

---

## Build System

### Gradle Tasks

- `./gradlew build` - Build all modules
- `./gradlew check` - Run Spotless formatting checks + tests
- `./gradlew spotlessApply` - Auto-format Java, JSON, Markdown
- `./gradlew :forge:runClient` - Launch Minecraft client
- `./gradlew :forge:runServer` - Launch dedicated server
- `./gradlew :forge:runData` - Run data generators

### Module Structure

- **Root**: Aggregator project with shared Spotless config
- **common/**: Loader-neutral code (sources disabled, compiled via forge module)
- **forge/**: Forge-specific implementation (includes common sources via srcDir)

### Source Exclusions

Many subsystems are temporarily excluded from compilation in `forge/build.gradle` under `sourceSets.main.java.exclude`:

- Client systems (`client/**`, `CHEXClient.java`)
- Quest system (`quest/**`)
- Legacy configs (`config/legacy/**`)
- Incomplete entities (Glowbeast, CliffHunter, SkyGrazer, some boss entities)
- World systems (`world/**`, biome providers, structure systems)

Re-enable by removing relevant `exclude` lines after implementation stabilizes.

---

## Testing

### Validation Tools

- **JSON Validation**: `python scripts/validate_json.py` (validates 747+ files)
- **Spotless**: Enforces code style (Google Java Format, Prettier)
- **Gradle Check**: Runs linting and compilation checks

### Manual Testing

See `docs/MANUAL_TESTING_CHECKLIST.md` for comprehensive testing procedures (90+ checks).

### Performance Profiling

Use `/chex lagprofiler start/stop/status` to profile server performance.

---

## Version History

| Version | Date       | Focus                           | Planets | Blocks | Entities |
| ------- | ---------- | ------------------------------- | ------- | ------ | -------- |
| 0.5.0   | 2025-10-17 | Mid-tier planets, documentation | +5      | +9     | 0        |
| 0.4.0   | 2025-10-16 | Arrakis implementation          | +1      | +8     | 0        |
| 0.3.0   | 2025-10-15 | Pandora ecosystem & bosses      | +1      | +4     | +6       |
| 0.2.0   | 2025-10-10 | Crystalis & Aurelia Ringworld   | +2      | +9     | 0        |
| 0.1.0   | 2025-10-05 | Core systems, commands, config  | 0       | 0      | 0        |
| 0.0.1   | 2025-10-01 | Initial project structure       | 0       | 0      | 0        |

---

## Future Roadmap

### Short-Term (Next Release)

- Re-enable client-side systems (skyboxes, particles, shaders)
- Implement Kepler-452b fauna (3 entities)
- Implement Aqua Mundus fauna (4 entities)
- Complete Ocean Sovereign boss

### Mid-Term

- Implement Inferno Prime fauna + Magma Titan boss
- Implement Kepler-452b Verdant Colossus boss
- Add pressure/oxygen mechanics (Aqua Mundus)
- Add dynamic weather system (Stormworld)
- Add solar flare events (Alpha Centauri A)

### Long-Term

- Performance optimization pass
- Multiplayer stress testing
- Release packaging
- CurseForge/Modrinth publication

---

**End of Document**
