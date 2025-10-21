# T-022 Pandora Block Set

**Goal**

- Implement block/item assets for Pandora (pandorite variants, spore soil, biolume moss, crystal-clad pandorite, lumicoral, volcanic/ash blocks).

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Loot tables, models, blockstates, recipes.

**Acceptance**

- All listed blocks registered with matching textures/models (placeholders acceptable with TODO note).
- Recipes/loot tables align with progression plan.
- `./gradlew :forge:runData` (if used) + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement block registrations + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
