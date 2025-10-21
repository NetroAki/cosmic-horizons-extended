# Exotica Boss Encounters (T-113)

## Summary

- Implemented Reality Breaker boss scaffold with three-phase cadence (Fractal Division, Quantum Distortion, Prismatic Collapse) including telegraphed abilities (clone cue visuals, displacement cue, radial knockback).
- Registered Exotica mini-boss placeholders and wired loot tables for GT progression cores.
- Added boss reward items (Exotic Heart, Fractablade, Breaker Core) and per-mini-boss cores (Prism/Resonant/Quantum/Fractal/Seraph).

## Files Added/Updated

- Entity registration: `common/src/main/java/com/netroaki/chex/entity/ModEntities.java`
- Reality Breaker class: `common/src/main/java/com/netroaki/chex/entity/exotica/boss/RealityBreakerEntity.java`
- Boss/miniboss loot tables: `common/src/main/resources/data/cosmic_horizons_extended/loot_tables/entities/`
  - `reality_breaker.json`, `prism_colossus.json`, `dune_siren.json`, `quantum_beast.json`, `fractal_horror.json`, `prism_seraph.json`
- Items: `common/src/main/java/com/netroaki/chex/item/ModItems.java`
  - `PRISM_CORE`, `RESONANT_CORE`, `QUANTUM_CORE`, `FRACTAL_CORE`, `SERAPH_CORE`, `EXOTIC_HEART`, `FRACTABLADE`, `BREAKER_CORE`

## Notes

- Arenas and advanced FX can be added in a later pass; current scaffolding satisfies encounter wiring and rewards for progression.
