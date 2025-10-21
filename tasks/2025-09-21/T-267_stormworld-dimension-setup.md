# T-267 Stormworld Dimension Setup

**Goal**

- Create gas giant dimension with atmospheric layers, variable gravity, and lightning strike mechanics for Stormworld (Tier 10 gas giant).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/stormworld.json`
- Gas giant environment and atmospheric systems
- Variable gravity and lightning mechanics

**Acceptance**

- Gas giant dimension with atmospheric layers
- Variable gravity based on altitude
- Lightning strike mechanics
- Pressure crush mechanics near depths
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create gas giant dimension JSON
- [ ] Implement atmospheric layer systems
- [ ] Add variable gravity based on altitude
- [ ] Configure lightning strike mechanics
- [ ] Set pressure crush mechanics
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
