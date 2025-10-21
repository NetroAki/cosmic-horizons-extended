# T-329 Eden's Garden Flora Generation

**Goal**

- Implement unique flora generation for Eden's Garden with paradise, healing, and harmony-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and paradise effects

**Acceptance**

- Eternal Trees: Trees that grow infinitely and provide perfect materials
- Healing Flowers: Flowers that provide regeneration and healing effects
- Harmony Vines: Vines that connect all life in perfect harmony
- Peaceful Grass: Grass that grows instantly and provides perfect grazing
- Tranquil Moss: Moss that grows on any surface and provides comfort
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Eternal Trees with infinite growth
- [ ] Create Healing Flowers with regeneration effects
- [ ] Add Harmony Vines with life connection
- [ ] Implement Peaceful Grass with instant growth
- [ ] Create Tranquil Moss with comfort effects
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_edens_garden_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
