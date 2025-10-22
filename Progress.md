# Progress Snapshot (2025-10-21)

## Recently Completed

- Compilation pipeline stabilised:
  - Registry/import fixes, base class scaffolds, duplicate method cleanup.
  - Gradle builds (compileJava, check) now succeed with JUnit 5 wired in.
- Placeholder coverage everywhere:
  - scripts/create_placeholder_textures.ps1 generates planet-themed block/item/entity textures plus minimal models.
  - New PlaceholderCubeRenderer draws every CHEX entity as a textured cube so we can spawn/test without crashes.
- Tracking docs:
  - progress/model_animation_tracker.md highlights which entities/blocks still need bespoke art/animations.
  - Stub.md records deferred work (entities, structures, AI) to revisit after terrain systems are ready.
- Arrakis terrain scaffolding (noise, placeholders, ore) added in support of PLANET_DESIGN_TASKS.md tier roadmap.

## In Progress / De-scoped

- Custom entity models, animations, AI behaviours (all deferred; see Stub.md).
- Boss arenas and cinematic encounters.
- Hazard controllers (spore haze, sandstorms, heat auras) and ambient particle FX.

## Immediate Focus

- [x] **Re-enable Terrablender region overlay**  
  - Make Pandora biome set active in CHEXRegion and remove config gating.  
  - Ensure biome source in dimension JSONs points at the rebuilt multi-noise layout.

- [x] **Pandora biome scaffolding**  
  - Added custom datapack features (pandora_fungal_tower, pandora_spore_reed, pandora_biolume_patch, pandora_crystal_cluster, pandora_vent_geyser, pandora_floating_cluster).
  - Terrain now draws on Pandorite variants or biome-specific caps; further tweaks can happen post-validation.

- [x] **Terrain noise tuning**  
  - Added worldgen/noise_settings/pandora_basic.json with biome-aware surface rules (Pandorite grass/dirt, magma crusts, prismarine seafloor).
  - Future adjustments will happen after the first in-game pass.

- [x] **Arrakis terrain bring-up**  
  - Arrakis noise preset (rrakis_basic) wired into the dimension definition and placeholder flora/ore features are active across all Arrakis biomes.

- [ ] **Validation**  
  - TODO: Run ./gradlew :forge:runClient (or :forge:runServer) and inspect Pandora/Arrakis biomes & ores once terrain scaffolding stabilises.
  - Capture findings/screenshots and update this tracker when the sanity pass happens.

## Upcoming Plan

1. Complete in-game validation for Pandora & Arrakis terrains.  
2. Begin Kepler-452b biome/terrain enhancements per PLANET_DESIGN_TASKS.md.  
3. Keep iterating planet-by-planet following the tier order, ticking entries in PLANET_DESIGN_TASKS.md as they reach the same scaffolding stage.
4. Revisit hazard systems, structures, and GTCEu mineral specifics once base terrains are in.

_Keep this file updated as milestones shift so the team sees the current priority at a glance._
