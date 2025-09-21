# CHEX Development Context

Last updated: 2025-09-20

## Project Snapshot
- **Platform**: Forge 1.20.1, Java 17. Multi-module layout with `common` (loader-neutral core logic/config) and `forge` loader entry point.
- **Mod Goals**: Provide CHEX-exclusive planets, progression gate around rocket/suit tiers, integrate with Cosmic Horizons dimensions, and hook GTCEu mineral ladders.
- **Core Systems**: Travel graph + configs (`common/core/TravelGraphCore`, `forge/travel/TravelGraph`) map rocket tiers to planet IDs; capability + commands manage player tier state; configs expose fuel mapping, TerraBlender overlay flag, and suit enforcement knobs.
- **Planet Data**: `forge/registry/PlanetRegistry` seeds explicit CHEX worlds (Pandora, Arrakis, Aqua Mundus, Inferno Prime, Crystalis, Exotica, Aurelia ring, Torus world, Hollow world, Dyson Swarm, Neutron Star Forge, Kepler-452b, Alpha Centauri A) with placeholder metadata. Cosmic Horizons discovery currently hardcodes Moon/Mercury/Mars only, with runtime discovery writing `_discovered_planets.json`.
- **Blocks & Assets**: Custom block set for Pandora/Arrakis (pandorite variants, arrakite sandstone, biolume moss, lumicoral, spice node, etc.) registered in `CHEXBlocks` with basic models + blockstates. No items beyond block items yet.
- **Biomes**: JSON definitions exist for Pandora (5 variants), Arrakis (5 variants), plus Exotica, Hollow World, Torus, Kepler (simple templates). `CHEXBiomes` bootstrap uses placeholder generation/mob settings but distinct colors. TerraBlender region (`CHEXRegion`) wires parameter points + surface rules conditioned on `enableTerraBlenderOverlay` config, but placement coverage needs tuning to align per-planet climates.
- **Worldgen**: `CHEXWorldGenProvider` stubs out mineral feature/placed feature creation; actual ore placement logic is still TODO. Density function classes exist (ring/torus/dyson/hollow) but integration status not verified. Biome modifiers now hook Pandora/Arrakis features; ore/mineral bootstrap remains TODO.
- **Travel & Progression**: Travel graph extends tiers 1-10 with CHEX worlds intermixed; relies on `TravelConfigLoader` for overrides. Suit/rocket configs in place, but fuel/volume mapping still defaulted to LH2 tiers >3.
- **Client UX**: Toast/tooltips packages scaffolded, but actual messaging usage pending. Crash log from 2025-09-11 resolved per user notes.
- **Docs & References**: `PROJECT_CONTEXT.md`, `Checklist`, `MISSING_FEATURES.md`, and `TB_STRATEGY.md` outline broader plan; this file tracks current snapshot for day-to-day execution.

## High-Level Observations
- Worldgen pipeline is mostly scaffolding; mineral distributions now load from config (biome ids/tags or explicit block IDs) but still need advanced density functions and bespoke features per planet.
- Planet registry now pulls from the discovery dump (fallbacks remain); future work can refine metadata beyond the defaults.
- TerraBlender overlay now only injects Pandora/Arrakis; other CHEX worlds stay gated until their datapacks mature.
- Block assets now have baseline loot tables, but recipes and in-world feature hookups remain open.
- GTCEu mineral integration needs actual data path from configs to worldgen bootstrapping.

## Working To-Do (see dedicated section below)
- Populate prioritized tasks derived from these observations and execute sequentially.
## To-Do Board (2025-09-20)
- [x] **Attach Pandora & Arrakis features**: Hook all existing placed features into their target biomes via Forge biome modifiers and ensure the biomes reference the correct generation steps.
- [x] **Tighten TerraBlender overlay scope**: Update `CHEXRegion` so the overlay only injects the biomes we currently support (Pandora + Arrakis) and leaves future placeholders behind a TODO to avoid polluting vanilla climates.
- [x] **Add default loot tables for CHEX blocks**: ensure each custom block drops itself to make survival playable.
- [x] **Wire mineral config to runtime features**: load `chex-minerals.json5` into runtime ore features with GTCEu + fallback handling (supports biome ids/tags and explicit blocks).






