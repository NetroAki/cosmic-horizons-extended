# T-303 Shattered Dyson Swarm Dimension Setup

**Goal**

- Create orbital debris dimension with zero-gravity sections, radiation burst mechanics, and solar flare cycles for Shattered Dyson Swarm (Tier 15 orbital debris).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/dyson_swarm.json`
- Orbital debris environment and radiation systems
- Zero-gravity and solar flare mechanics

**Acceptance**

- Orbital debris dimension with zero-gravity sections
- Radiation burst mechanics
- Solar flare cycles
- Debris-based terrain generation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create orbital debris dimension JSON
- [ ] Implement zero-gravity sections
- [ ] Add radiation burst mechanics
- [ ] Configure solar flare cycles
- [ ] Set debris-based terrain generation
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
