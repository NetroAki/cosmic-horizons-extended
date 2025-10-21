# T-289 Torus World Dimension Setup

**Goal**

- Create toroidal dimension with wrap-around terrain, gravity manipulation mechanics, and null-gravity zones for Torus World (Tier 13 gravity tech).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/torus_world.json`
- Toroidal environment and gravity systems
- Wrap-around terrain and null-gravity mechanics

**Acceptance**

- Toroidal dimension with wrap-around terrain
- Gravity manipulation mechanics
- Null-gravity zones
- Torus-based terrain generation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create toroidal dimension JSON
- [ ] Implement wrap-around terrain mechanics
- [ ] Add gravity manipulation systems
- [ ] Configure null-gravity zones
- [ ] Set torus-based terrain generation
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
