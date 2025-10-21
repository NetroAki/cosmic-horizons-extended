# T-292 Torus World Flora Generation

**Goal**

- Implement unique flora generation for Torus World with toroidal, gravity, and energy-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and toroidal effects

**Acceptance**

- Torus Trees: trees adapted to toroidal gravity
- Gravity Vines: plants that grow along gravity lines
- Radiant Flowers: flowers that glow with energy
- Null-G Moss: moss that grows in zero-gravity zones
- Spine Lichen: lichen that grows on structural elements
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Torus Trees with toroidal adaptation
- [ ] Create Gravity Vines with gravity line growth
- [ ] Add Radiant Flowers with energy glow
- [ ] Implement Null-G Moss with zero-gravity growth
- [ ] Create Spine Lichen with structural growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
