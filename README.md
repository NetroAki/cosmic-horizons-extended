Cosmic Horizons Extended (CHEX)

Forge 1.20.1 addon that auto-discovers Cosmic Horizons (CH) planets, adds new unique planets, and injects GTCEu mineral tiers via data-driven configs.

Setup

- Requires: Forge 1.20.1, optional mods: CH, GTCEu, KubeJS, FTB Quests
- Build: gradlew build
- Datagen: use the data run config created by ForgeGradle in this project

Configs

- config/chex/chex-planets.json5: per-planet overrides (rocket tier / suit tag / name)
- config/chex/chex-minerals.json5: mineral tiers per planet with distributions
- config/chex/fallback_ores.json5: fallback ore blocks when GTCEu is missing
- cosmic_horizons_extended-common.toml: behavior toggles and fuel mapping

Commands

- /chex dumpPlanets → logs and writes config/chex/planets_dump.json
- /chex reload → reloads configs and rebuilds travel graph
- /chex travel <1..10> → lists reachable planets for a tier
- /chex minerals <planetId> → dumps minerals JSON for a planet to logs

Progression

- Suit tags: chex:suits/suit1..suit5. Dimension entry enforces suit tier (bounce or debuff).
- Fuel mapping: editable per rocket tier in common config. Fuels registered with buckets.

Data Generation

- Worldgen JSONs (configured/placed features + biome modifiers) are generated from chex-minerals.json5.
- Run the data task and include the generated JSONs in your datapack/mod resources as needed.

Links

- Cosmic Horizons addon datapack docs: https://cosmic-mod.github.io/addonsupport/
