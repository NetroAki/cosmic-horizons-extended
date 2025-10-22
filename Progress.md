# Progress Snapshot (2025-10-21)

## Recently Completed

- Compilation pipeline stabilised:
  - Registry/import fixes, base class scaffolds, duplicate method cleanup.
  - Gradle builds (`compileJava`, `check`) now succeed with JUnit 5 wired in.
- Placeholder coverage everywhere:
  - `scripts/create_placeholder_textures.ps1` generates planet-themed block/item/entity textures plus minimal models.
  - New `PlaceholderCubeRenderer` draws every CHEX entity as a textured cube so we can spawn/test without crashes.
- Tracking docs:
  - `progress/model_animation_tracker.md` highlights which entities/blocks still need bespoke art/animations.
  - `Stub.md` records deferred work (entities, structures, AI) to revisit after terrain systems are ready.

## In Progress / De-scoped

- Custom entity models, animations, AI behaviours (all deferred; see `Stub.md`).
- Boss arenas and cinematic encounters.
- Hazard controllers (spore haze, sandstorms, heat auras) and ambient particle FX.

## Immediate Focus

- [x] **Re‑enable Terrablender region overlay**  
  - Make Pandora biome set active in `CHEXRegion` and remove config gating.  
  - Ensure biome source in dimension JSONs points at the rebuilt multi-noise layout.

- [x] **Pandora biome scaffolding**  
  - Added custom datapack features (`pandora_fungal_tower`, `pandora_spore_reed`, `pandora_biolume_patch`, `pandora_crystal_cluster`, `pandora_vent_geyser`, `pandora_floating_cluster`) and wired them into their respective biomes.
  - Terrain now draws on Pandorite variants or biome-specific caps; further tweaks can happen post-validation.

- [x] **Terrain noise tuning**  
  - Added `worldgen/noise_settings/pandora_basic.json` with biome-aware surface rules (Pandorite grass/dirt, magma crusts, prismarine seafloor).
  - Future adjustments will happen after the first in-game pass.

- [x] **GregTech ore baseline**  
  - Added Pandorite stone block family + `stone_ore_replaceables` tag so GT fallback can embed ore targets.
  - Introduced `pandora_reference_ore` placeholder (with beryllium/lithium fallbacks) ready to swap with the GT mineral table when enabled.

- [ ] **Validation**  
  - TODO: Run `./gradlew :forge:runClient` (or `:forge:runServer`) and inspect Pandora biomes/ores once terrain scaffolding stabilises.
  - Capture findings/screenshots and update this tracker when the sanity pass happens.

- [x] **Arrakis terrain bring-up**  
  - Arrakis noise preset (`arrakis_basic`) with biome-aware surface rules is wired into the dimension definition.
  - Placeholder vegetation/features (dune shrubs, spice cactus/node deposits, ice reeds, rock piles, stormglass) and an interim ore distribution (`arrakis_reference_ore`) are active across all Arrakis biomes.

_Keep this file updated as milestones shift so the team sees the current priority at a glance._
