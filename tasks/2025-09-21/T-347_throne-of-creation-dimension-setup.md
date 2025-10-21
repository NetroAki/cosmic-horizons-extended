# T-347 The Throne of Creation Dimension Setup

**Goal**

- Create ultimate dimension where universe creation occurs with variable gravity and creation rifts for The Throne of Creation (Tier 15+ Final Boss).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/throne_of_creation.json`
- Ultimate dimension environment and creation systems
- Variable gravity and creation rift mechanics

**Acceptance**

- Ultimate dimension where universe creation occurs
- Variable 0.0g to 5.0g gravity with shifting gravity wells
- Creation rifts that teleport players randomly or cause instant death
- Existence storms causing equipment aging/reversal
- Cosmic radiation damage even with max shielding
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create Throne of Creation dimension JSON
- [ ] Implement ultimate dimension environment
- [ ] Add variable gravity mechanics
- [ ] Configure creation rift effects
- [ ] Set existence storm mechanics
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_throne_of_creation_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
