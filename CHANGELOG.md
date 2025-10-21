# Changelog - Cosmic Horizons Extended

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased] - Future Development

### Planned Features

- **Client-Side Systems**: Skyboxes, particle effects, shaders, hazard visual feedback
- **Advanced Entities**: 15-20 custom fauna entities across planets
- **Additional Bosses**: Verdant Colossus, Ocean Sovereign, Magma Titan
- **Special Mechanics**: Pressure/oxygen (Aqua Mundus), dynamic weather (Stormworld), solar flares (Alpha Centauri A)
- **Performance**: Optimization pass, chunk generation improvements

---

## [0.5.0] - 2025-10-17 - Mid-Tier Planets & Documentation

### Added

**New Dimensions**:

- **Kepler-452b** (T6, Rocket Tier 8): Earth-like planet with 5 biomes
  - Temperate Forest, Highlands, River Valleys, Meadowlands, Rocky Scrub
  - Multi-layer tree canopies with hanging decorations
- **Aqua Mundus** (T4, Rocket Tier 6): Ocean world with 5 biomes
  - Shallow Seas, Kelp Forests, Abyssal Trenches, Hydrothermal Vents, Ice Shelves
  - Underwater resource collection opportunities
- **Inferno Prime** (T5, Rocket Tier 7): Volcanic planet with 5 biomes
  - Lava Seas, Basalt Flats, Obsidian Isles, Ash Wastes, Magma Caverns
  - Ultrawarm environment, extreme heat hazards
- **Alpha Centauri A** (T5, Rocket Tier 7): Star-surface megastructure with 3 biomes
  - Photosphere, Corona, Magnetosphere
  - 8x coordinate scaling, maximum ambient light
- **Stormworld** (T10+, Rocket Tier 10+): Dynamic weather planet with 4 biomes
  - Lower Layer, Mid Layer, Upper Layer, Eye
  - Extended 512-block height, layered atmosphere

**New Blocks** (Kepler-452b):

- Kepler Wood Log (rotated pillar block)
- Kepler Wood Leaves (decay mechanics)
- Kepler Moss (instant-break, decorative)
- Kepler Vines (instant-break, decorative)
- Kepler Blossom (light level 3, glowing flowers)

**New Blocks** (Aqua Mundus):

- Aqua Vent Basalt (light level 5, volcanic vents)
- Aqua Manganese Nodule (ore block, drops XP 2-5)
- Aqua Luminous Kelp (light level 8, underwater lighting)
- Aqua Ice Shelf Slab (frozen surface areas)

**GTCEu Ore Distributions**:

- Kepler-452b: Beryllium, fluorite, ruby, sapphire (LuV tier)
- Aqua Mundus: Platinum, iridium, palladium (EV tier)
- Inferno Prime: Niobium, tantalum, uranium, osmium (IV tier)
- Alpha Centauri A: High-tier photonic materials (planned)
- Stormworld: Advanced materials (planned)

**Documentation**:

- `docs/PLANET_DESIGNS.md`: Comprehensive planet guide (7 planets, biomes, resources, hazards)
- `docs/CONFIGURATION_EXAMPLES.md`: Config examples for planets, minerals, suits, visual filters
- `docs/BOSS_ENCOUNTERS.md`: Boss strategies, mechanics, loot drops for Spore Tyrant & Worldheart Avatar
- `docs/MANUAL_TESTING_CHECKLIST.md`: Comprehensive manual testing procedures (90+ checks)
- `CHANGELOG.md`: This file

**Localization**:

- English translations for all new blocks

### Changed

- Updated all dimension JSONs to use `multi_noise` biome sources for better terrain generation
- Improved biome distribution parameters for more realistic planet landscapes

### Fixed

- Gradle cache corruption issues (FileAlreadyExistsException)
- Spotless formatting violations in Markdown files

---

## [0.4.0] - 2025-10-16 - Arrakis Implementation

### Added

**New Dimension**:

- **Arrakis** (T4, Rocket Tier 6): Desert planet with 5 biomes
  - Great Dunes, Spice Mines, Polar Ice Caps, Sietch Strongholds, Stormlands
  - Sandstorm weather effects
  - Spice resource collection

**New Blocks** (Arrakis):

- Arrakite Sandstone (4 variants: regular, smooth, cut, chiseled)
- Spice Node (special resource block)
- Crystalline Salt (decorative)
- Sietch Stone (shelter material)
- Stormglass (transparent block)

**New Flora** (Arrakis):

- Spice Cactus (desert biomes)
- Ice Reeds (polar ice caps)
- Desert Shrub (scrub biomes)

**New Items** (Arrakis):

- Dried Spice (smelting product)

**Custom Sky Effects**:

- ArrakisSkyEffects: Red/orange twilight gradient, foggy atmosphere

**GTCEu Ore Distributions**:

- Arrakis: Bauxite, ilmenite (HV tier)

### Changed

- Consolidated Arrakis sub-biomes into single "Arrakis" planet
- Updated TravelGraph to reflect new planet accessibility

---

## [0.3.0] - 2025-10-15 - Pandora Ecosystem & Bosses

### Added

**New Dimension**:

- **Pandora** (T3, Rocket Tier 5): Bioluminescent jungle moon with 5 biomes
  - Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands
  - Glowing flora and hostile fauna

**New Blocks** (Pandora):

- Pandorite Stone (terrain block)
- Biolume Moss (light level 7, spreading moss)
- Lumicoral (light level 10, coral-like decorations)
- Spore Soil (farmland-like block)

**New Entities** (Pandora):

- Sporefly (ambient creature, neutral/hostile variants)
- Glowbeast (passive herbivore, placeholder)
- **Spore Tyrant** (boss): 2-phase fungal boss
  - Phase 1: Spore clouds, Sporefly summons
  - Phase 2: Enraged mode, denser spore clouds
  - Drops: Pandoran Heart Seed, Biolume Moss, XP
- **Worldheart Avatar** (boss): 3-phase ultimate boss
  - Phase 1: Guardian summons, healing zones
  - Phase 2: Rooting vines, spore traps
  - Phase 3: Enrage timer, mass summons, death pulse
  - Drops: Worldheart Fragment, Pandoran Heart Seed, high-tier loot

**Boss Loot Cores**:

- Pandoran Heart Seed (from Spore Tyrant): Used for Rocket T6 components
- Worldheart Fragment (from Worldheart Avatar): Used for Rocket T10 components, Neutron Forge access

**Custom Sky Effects**:

- PandoraSkyEffects: Bioluminescent glow, twilight hues

**Audio** (Pandora):

- Ambient sounds for Bioluminescent Forest
- Boss fight music triggers
- Sporefly wing buzzing sounds

### Changed

- Consolidated Pandora sub-biomes into single "Pandora" planet

---

## [0.2.0] - 2025-10-10 - Crystalis & Aurelia Ringworld

### Added

**New Dimension**:

- **Crystalis** (T8, Rocket Tier 9): Frozen crystal planet with 3 biomes
  - Diamond Fields, Frosted Plains, Ice Caves
  - Extreme cold hazards (frostbite mechanics)

**New Blocks** (Crystalis):

- Crystalis Crystal (decorative crystal blocks)
- Crystalis Clear Glass (transparent variant)
- Crystal Core Ore (special ore block)

**New Dimension**:

- **Aurelia Ringworld** (T8, Rocket Tier 10): Megastructure habitat with 6 biomes
  - Plains, Forest, Mountains, River, Edge, Structural
  - Custom gravity simulation (centripetal force)
  - Spaceport teleportation structures

**New Blocks** (Aurelia):

- Ringworld Wall (unbreakable boundary)
- Aurelian Wood (custom tree species)
- Aurelian Leaves (foliage)
- Aurelian Grass (ground cover)
- Aurelia Wall (structural material)
- Arc Scenery Block (client-rendered ring arc)

**GTCEu Ore Distributions**:

- Crystalis: Beryllium, fluorite, ruby, sapphire (LuV tier)
- Aurelia Ringworld: Draconium, awakened draconium (ZPM tier)

**Custom Mechanics**:

- Ringworld Chunk Generator: Custom terrain generation for ring structure
- Gravity System: Entity rotation based on position (simulation of centripetal force)
- Teleportation: Spaceport structures for fast travel across ring

### Changed

- Updated biome tags for ore generation (`#chex:planet_all`)

---

## [0.1.0] - 2025-10-05 - Core Systems & Foundation

### Added

**Core Systems**:

- **Planet Registry**: Central registry for all planets with discovery system
  - Auto-discovers Cosmic Horizons/Cosmos planets at server start
  - Supports runtime overrides via `chex-planets.json5`
  - Hot-reload support (`/chex reload`)
- **Travel Graph**: Tier-based planet accessibility system
  - Maps rocket nodule tiers (T1-T10) to accessible planets
  - Validates travel routes before launch
- **Player Tier Capability**: Persistent player progression data
  - Tracks unlocked rocket tiers, suit tiers, discovered planets, mineral samples
  - Client-server synchronization via `CHEXNetwork`
- **Fuel Registry**: Fuel management for rocket tiers
  - Maps fluid types to rocket tiers
  - Supports fallback fluids if GTCEu not present
  - Hot-reload support
- **Mineral Generation Registry**: GTCEu ore integration
  - Loads ore distributions from `chex-minerals.json5`
  - Generates worldgen JSONs (configured/placed features, biome modifiers)
  - Hot-reload support (`/chex minerals reload`)
- **Suit Hazard System**: Environmental protection requirements
  - Per-dimension suit tier checks
  - Bounce-back or debuff mechanics
  - Configurable via `chex-suit-hazards.json5`

**Configuration Files**:

- `chex-planets.json5`: Planet property overrides (nodule tier, suit tier, display name)
- `chex-minerals.json5`: GTCEu mineral distributions per planet/biome
- `fallback_ores.json5`: Fallback ore blocks (not used; GTCEu is hard dependency)
- `chex-suit-hazards.json5`: Environmental hazard rules per dimension
- `chex-visual-filters.json5`: Per-dimension fog tint/effects (client-side, future)
- `cosmic_horizons_extended-common.toml`: Runtime behavior toggles, fuel mappings

**Commands**:

- `/chex dumpPlanets`: Export all discovered planets to JSON
- `/chex reload`: Reload planets, travel graph, minerals, fallback ores
- `/chex travel <tier>`: List planets accessible at rocket tier
- `/chex launch <planetId>`: Validate and teleport to planet
- `/chex unlock rocket <tier> <player>`: Unlock rocket tier for player
- `/chex unlock suit <tier> <player>`: Unlock suit tier for player
- `/chex unlock planet <id> <player>`: Unlock planet for player
- `/chex minerals <planetId>`: Dump mineral config for planet
- `/chex minerals reload`: Reload mineral distributions
- `/chex lagprofiler start/stop/status`: Performance profiling tool

**Networking**:

- `PlayerTierSyncMessage`: Syncs player capabilities between server and client
- `CHEXNetwork`: Handles packet registration and dispatch

**Validation Tools**:

- `scripts/validate_json.py`: Validates all JSON/JSON5 files in project
- Spotless code formatting (Google Java Format, Prettier)

**Build System**:

- Multi-module Architectury setup (common + forge)
- Gradle tasks: `build`, `check`, `spotlessApply`, `runClient`, `runServer`, `runData`
- Source set exclusions for unstable/incomplete subsystems

**Dependencies**:

- Minecraft Forge 1.20.1 (47.4.1)
- GregTech CEu (GTCEu) - Hard dependency
- Cosmic Horizons or Cosmos - Strongly recommended
- TerraBlender (optional) - Biome injection
- JEI (optional) - Recipe/resource displays
- KubeJS (optional) - Scripting support
- AzureLib (optional) - Animated entity models

### Changed

- Dual main classes: `CHEX.java` (primary entrypoint) and `CosmicHorizonsExpanded.java` (being consolidated)

### Technical Details

**Java Packages**:

- `com.netroaki.chex.core.planet`: Planet registry, discovery, overrides
- `com.netroaki.chex.core.travel`: Travel graph, launch validation
- `com.netroaki.chex.core.fuel`: Fuel registry, tier mappings
- `com.netroaki.chex.core.minerals`: Mineral generation, runtime config
- `com.netroaki.chex.core.player`: Player capabilities, progression tracking
- `com.netroaki.chex.core.suit`: Suit hazard system, validation
- `com.netroaki.chex.commands`: All `/chex` command implementations
- `com.netroaki.chex.network`: Packet handling, client-server sync
- `com.netroaki.chex.registry`: Block, item, entity, dimension registrations
- `com.netroaki.chex.integration.gtceu`: GTCEu bridge for ore generation
- `com.netroaki.chex.integration.terablender`: TerraBlender biome regions (optional)
- `com.netroaki.chex.integration.jei`: JEI plugin for recipe/planet displays (optional)

**Data Generation**:

- Worldgen JSONs generated from `chex-minerals.json5` via Gradle `runData` task
- Generated files: `forge/src/main/generated/data/cosmic_horizons_extended/`

---

## [0.0.1] - 2025-10-01 - Initial Commit

### Added

- Project structure (common + forge modules)
- Basic mod metadata (mods.toml, fabric.mod.json)
- Gradle build configuration
- LICENSE (Apache 2.0)
- README.md
- CLAUDE.md (AI pair programming guide)
- AGENTS.md (agent role definitions)

---

## Version History Summary

| Version | Date       | Focus                           | Planets Added      | Blocks Added | Entities Added        |
| ------- | ---------- | ------------------------------- | ------------------ | ------------ | --------------------- |
| 0.5.0   | 2025-10-17 | Mid-tier planets, documentation | 5 dimensions       | +9 blocks    | 0                     |
| 0.4.0   | 2025-10-16 | Arrakis implementation          | Arrakis            | +8 blocks    | 0                     |
| 0.3.0   | 2025-10-15 | Pandora ecosystem & bosses      | Pandora            | +4 blocks    | +4 entities, 2 bosses |
| 0.2.0   | 2025-10-10 | Crystalis & Aurelia Ringworld   | Crystalis, Aurelia | +9 blocks    | 0                     |
| 0.1.0   | 2025-10-05 | Core systems, commands, config  | 0 (foundation)     | 0            | 0                     |
| 0.0.1   | 2025-10-01 | Initial project structure       | 0                  | 0            | 0                     |

---

## Acknowledgments

- **GregTech CEu Team**: For the incredible ore processing system
- **Cosmic Horizons/Cosmos Team**: For the space exploration framework
- **TerraBlender Team**: For the biome injection API
- **JEI Team**: For the recipe display system
- **AzureLib Team**: For the entity animation library
- **Minecraft Forge Team**: For the modding framework

---

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

**End of Changelog**
