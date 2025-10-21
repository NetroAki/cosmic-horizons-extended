# T-219 Arrakis Dimension Setup

**Goal**

- Create dimension JSON with harsh sunlight, sandstorm weather, and desert survival mechanics for Arrakis (Tier 4 desert progression planet).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/arrakis.json`
- Weather and environmental systems
- Desert survival mechanics

**Acceptance**

- Dimension JSON with harsh sunlight and sandstorm weather controller
- Reduced water availability (desert world)
- Constant hydration drain mechanic (faster hunger/thirst without water/food)
- Periodic sandstorm events (blindness + slowness, reduced ranged accuracy)
- Gravity set to 1.0 (normal)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create dimension JSON with harsh sunlight
- [ ] Implement sandstorm weather controller
- [ ] Add reduced water availability
- [ ] Configure hydration drain mechanic
- [ ] Add periodic sandstorm events
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
