# T-259 Crystalis Dimension Setup

**Goal**

- Create frozen giant dimension with extreme cold, constant freeze damage, and slippery surface mechanics for Crystalis (Tier 9 frozen giant).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/crystalis.json`
- Frozen environment and cold systems
- Slippery surface and atmospheric effects

**Acceptance**

- Frozen giant dimension with extreme cold
- Constant freeze damage
- Slippery surface mechanics
- Blue aurora sky atmosphere
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create frozen giant dimension JSON
- [ ] Implement extreme cold systems
- [ ] Add constant freeze damage
- [ ] Configure slippery surface mechanics
- [ ] Set blue aurora sky atmosphere
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
