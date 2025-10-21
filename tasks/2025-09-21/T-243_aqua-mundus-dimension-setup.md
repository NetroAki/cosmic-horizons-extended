# T-243 Aqua Mundus Dimension Setup

**Goal**

- Create water-world dimension with high-pressure zones, oxygen consumption, and thermal gradient systems for Aqua Mundus (Tier 7 ocean world).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/aqua_mundus.json`
- Water environment and pressure systems
- Oxygen consumption and thermal mechanics

**Acceptance**

- Water-world dimension with high-pressure zones
- Oxygen consumption mechanics
- Thermal gradient systems
- Variable gravity based on depth
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create water-world dimension JSON
- [ ] Implement high-pressure zone systems
- [ ] Add oxygen consumption mechanics
- [ ] Configure thermal gradient systems
- [ ] Set variable gravity based on depth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
