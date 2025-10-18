# Cosmic Horizons Extended (CHEX)

**Version**: 0.5.0  
**Minecraft**: 1.20.1 (Forge 47.4.1)

Forge 1.20.1 addon that auto-discovers Cosmic Horizons (CH) planets, adds 7+ new unique planets with custom biomes, and injects GTCEu mineral progression via data-driven configs.

## Features

- **7+ Fully Implemented Planets**: Pandora, Arrakis, Kepler-452b, Aqua Mundus, Inferno Prime, Alpha Centauri A, Stormworld, Aurelia Ringworld, Crystalis
- **30+ Custom Blocks**: Planet-specific terrain, decorations, and resources
- **Multi-Phase Boss Encounters**: Spore Tyrant, Worldheart Avatar (2 bosses, 5 phases total)
- **GTCEu Ore Integration**: Tier-based progression (MV → HV → EV → IV → LuV → ZPM)
- **Data-Driven Configuration**: Hot-reloadable JSON5 configs for planets, minerals, hazards, and travel
- **Suit Hazard System**: Environmental protection requirements per dimension
- **Player Progression Tracking**: Persistent rocket tiers, suit tiers, discovered planets
- **Boss Loot Cores**: Unlock advanced progression and special destinations

## Requirements

- **Minecraft Forge 1.20.1** (47.4.1+)
- **Java 17** (required for build and runtime)
- **GregTech CEu (GTCEu)** - Hard dependency for ore processing
- **Cosmic Horizons** or **Cosmos** - Strongly recommended for planet discovery integration

## Quick Start

1. **Build**: `./gradlew build`
2. **Install**: Place JAR in `mods/` folder alongside GTCEu and Cosmic Horizons
3. **Launch**: Start Minecraft to generate default configs
4. **Travel**: Use `/chex travel <tier>` to see reachable planets
5. **Explore**: Ensure proper rocket tier, suit, and fuel before launching

## Planets Overview

| Planet             | Tier | Rocket | Biomes | Highlights                                       |
| ------------------ | ---- | ------ | ------ | ------------------------------------------------ |
| **Pandora**        | T3   | 5      | 5      | Bioluminescent forests, 2 bosses, toxic spores   |
| **Arrakis**        | T4   | 6      | 5      | Desert dunes, spice resources, sandstorms        |
| **Kepler-452b**    | T6   | 8      | 5      | Earth-like forests, multi-layer trees, temperate |
| **Aqua Mundus**    | T4   | 6      | 5      | Ocean world, underwater mining, bioluminescence  |
| **Inferno Prime**  | T5   | 7      | 5      | Volcanic lava seas, extreme heat, ultrawarm      |
| **Alpha Centauri** | T5   | 7      | 3      | Star surface, 8x scale, radiation hazards        |
| **Stormworld**     | T10+ | 10+    | 4      | Dynamic weather, 512-block height, lightning     |
| **Aurelia Ring**   | T8   | 10     | 6      | Megastructure, custom gravity, spaceports        |
| **Crystalis**      | T8   | 9      | 3      | Frozen crystals, diamond fields, frostbite       |

See `docs/PLANET_DESIGNS.md` for detailed planet information.

## Development Tools

### JSON Validation

The project includes a JSON validation script to ensure all JSON files are properly formatted:

```bash
# Run from the project root
python scripts/validate_json.py
```

This script will:

- Recursively scan all JSON files in the project
- Validate JSON syntax and structure
- Handle common JSON extensions like comments and trailing commas
- Provide clear error messages with file locations
- Exit with code 0 if all files are valid, 1 if any file is invalid

It automatically skips build directories (like `build/`, `.gradle/`, `node_modules/`).

## Core Systems

- **Planet Discovery**: Auto-discovers Cosmic Horizons/Cosmos planets at server start
- **Travel Graph**: Tier-based planet accessibility with validation
- **Suit Hazard System**: Environmental protection requirements per dimension
- **Mineral Generation**: GTCEu ore vein integration with hot-reload support
- **Player Progression**: Persistent tracking of rocket/suit tiers, unlocked planets
- **Boss Loot Cores**: Progression-gated access to special destinations
- **Launch Validation**: Checks nodule tier, suit tier, fuel, cargo mass, destination keys
- **Configuration Hot-Reload**: Runtime config updates via `/chex reload` commands

## Commands

### Planet Management

- `/chex dumpPlanets` - Export all discovered planets to JSON
- `/chex travel <tier>` - List planets accessible at rocket tier (1-10+)
- `/chex launch <planetId>` - Validate and teleport to planet
- `/chex reload` - Reload planet/travel/mineral/hazard configs

### Progression Management

- `/chex unlock rocket <tier> <player>` - Unlock rocket tier for player
- `/chex unlock suit <tier> <player>` - Unlock suit tier for player
- `/chex unlock planet <id> <player>` - Unlock planet discovery for player

### Resource Management

- `/chex minerals <planetId>` - Display mineral config for planet
- `/chex minerals reload` - Reload mineral distributions

### Debug Tools

- `/chex lagprofiler start/stop/status` - Performance profiling tool

## Configuration

| File                                   | Purpose                         | Hot-Reload              |
| -------------------------------------- | ------------------------------- | ----------------------- |
| `chex-planets.json5`                   | Planet property overrides       | `/chex reload`          |
| `chex-minerals.json5`                  | GTCEu ore distributions         | `/chex minerals reload` |
| `chex-suit-hazards.json5`              | Environmental hazard rules      | `/chex reload`          |
| `chex-visual-filters.json5`            | Per-dimension fog/tint effects  | `/chex reload`          |
| `cosmic_horizons_extended-common.toml` | Behavior toggles, fuel mappings | Requires restart        |

See `docs/CONFIGURATION_EXAMPLES.md` for detailed examples and best practices.

## Progression System

### Rocket Tiers (T1-T10+)

Unlock higher rocket tiers to access more distant planets:

- **T1-T3**: Starting planets (basic resources)
- **T4-T6**: Mid-tier planets (Arrakis, Aqua Mundus, Kepler-452b)
- **T7-T9**: Advanced planets (Inferno Prime, Alpha Centauri A, Crystalis)
- **T10+**: Megastructures (Stormworld, Aurelia Ringworld, Neutron Forge)

### Suit Tiers (T1-T5)

Environmental protection requirements:

- **Suit T1**: Basic hazards (toxic air, spores) - Pandora
- **Suit T2**: Moderate hazards (heat, cold) - Arrakis
- **Suit T3**: Severe hazards (extreme heat) - Inferno Prime
- **Suit T4**: Extreme hazards (radiation, frostbite) - Crystalis, Alpha Centauri A
- **Suit T5**: Megastructure hazards - Dyson Swarm, Neutron Forge

### Boss Loot Cores

Defeat bosses to unlock advanced progression:

- **Pandoran Heart Seed** (Spore Tyrant) → Rocket T6 components
- **Worldheart Fragment** (Worldheart Avatar) → Rocket T10 components, Neutron Forge access

See `docs/BOSS_ENCOUNTERS.md` for strategies and mechanics.

## JEI Integration

CHEX includes optional JEI plugin support:

- **Rocket Assembly Category**: Shows T1-T5 nodule crafting recipes
- **Planet Resources Category**: Displays mineral samples per planet

## Data Generation

Worldgen JSONs (configured/placed features, biome modifiers) are generated from `chex-minerals.json5`:

```bash
./gradlew :forge:runData
```

Generated files are placed in `forge/src/main/generated/data/cosmic_horizons_extended/`.

## Optional Integration

- **TerraBlender**: Custom biome injection (see `TB_STRATEGY.md`)
- **KubeJS**: Scripting support for custom recipes/events
- **JEI**: Recipe and resource displays
- **FTB Quests**: Quest integration via commands
- **AzureLib**: Animated entity models (future)

## Documentation

- **`docs/PLANET_DESIGNS.md`**: Comprehensive planet guide (biomes, resources, hazards)
- **`docs/CONFIGURATION_EXAMPLES.md`**: Config examples and best practices
- **`docs/BOSS_ENCOUNTERS.md`**: Boss strategies, mechanics, loot drops
- **`docs/MANUAL_TESTING_CHECKLIST.md`**: Manual testing procedures (90+ checks)
- **`CHANGELOG.md`**: Version history and feature changelog
- **`CLAUDE.md`**: AI pair programming guide
- **`AGENTS.md`**: Agent role definitions

## Troubleshooting

### Launch Denied

- Check rocket tier: `/chex travel <tier>`
- Verify suit tier: Must have required suit tag
- Check fuel: Ensure correct fuel type and volume
- Check boss cores: Some destinations require loot cores (e.g., Neutron Forge requires Sovereign Heart)

### Ores Not Generating

- Verify config: Run `python scripts/validate_json.py`
- Reload minerals: `/chex minerals reload`
- Check biome IDs: Use `/locate biome <biome_id>`
- Lower rarity values in `chex-minerals.json5`

### Config Changes Not Applied

- Run `/chex reload` for planet/hazard/visual configs
- Run `/chex minerals reload` for mineral distributions
- Restart server for runtime config changes (`cosmic_horizons_extended-common.toml`)

### Build Failures

- Ensure Java 17 installed: `java -version`
- Clean Gradle cache: `./gradlew clean`
- Delete Fabric-Loom cache if needed: `rm -rf .gradle/caches/fabric-loom/`
- Re-run build: `./gradlew build --no-daemon`

## Links

- **Cosmic Horizons Addon Docs**: https://cosmic-mod.github.io/addonsupport/
- **GTCEu Wiki**: https://gregtech.overminddl1.com/
- **TerraBlender Docs**: https://github.com/Glitchfiend/TerraBlender

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
