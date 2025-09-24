Cosmic Horizons Extended (CHEX)

Forge 1.20.1 addon that auto-discovers Cosmic Horizons (CH) planets, adds new unique planets, and injects GTCEu mineral tiers via data-driven configs.

## Development Tools

### JSON Validation

The project includes a JSON validation script to ensure all JSON files are properly formatted:

```bash
# Run from the project root
python scripts/validate_json.py
```

This script will:
- Recursively scan all JSON and JSON5 files in the project (JSON5 validation requires the optional `json5` Python package; when absent the files are skipped with a warning)
- Validate JSON syntax and structure
- Handle single-line and block comments while respecting JSON strings
- Provide clear error messages with file locations
- Exit with code 0 if all files are valid, 1 if any file is invalid

It automatically skips build directories (like `build/`, `.gradle/`, `node_modules/`).

Setup

- Requires: Forge 1.20.1, optional mods: CH, GTCEu, KubeJS, FTB Quests
- Build: gradlew build
- Datagen: use the data run config created by ForgeGradle in this project

Configs

- config/chex/chex-planets.json5: per-planet overrides (nodule tier / suit tag / name)
- config/chex/chex-minerals.json5: mineral tiers per planet with distributions
- config/chex/fallback_ores.json5: fallback ore blocks when GTCEu is missing
- cosmic_horizons_extended-common.toml: behavior toggles and fuel mapping

Commands

- /chex dumpPlanets → logs and writes run/chex_planets_dump.json
- /chex reload → reloads configs (planets, travel graph, minerals, fallback ores)
- /chex travel <1..10> → lists reachable planets for a tier
- /chex minerals <planetId> → dumps minerals JSON view to chat/logs
- /chex launch <planetId> → validates nodule tier, suit, discovery, and fuel, then teleports

Progression

- Suit tags: chex:suits/suit1..suit5. Dimension entry enforces suit tier (bounce or debuff).
- Fuel mapping: editable per nodule tier in common config. Fuels registered with buckets.
- Nodule tiers: T1..T10; see tooltips on the Nodule Controller item.

Data Generation

- Worldgen JSONs (configured/placed features + biome modifiers) are generated from chex-minerals.json5.
- Biome modifiers are always emitted; if no biomes are provided, default to #minecraft:is_overworld.
- Run the data task and include the generated JSONs in your datapack/mod resources as needed.

TerraBlender Strategy

- CH planets keep their defaults. CHEX provides optional TerraBlender overlays (see TB_STRATEGY.md).

Links

- Cosmic Horizons addon datapack docs: https://cosmic-mod.github.io/addonsupport/
