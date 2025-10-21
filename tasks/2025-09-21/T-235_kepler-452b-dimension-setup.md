# T-235 Kepler-452b Dimension Setup

**Goal**

- Create temperate exoworld dimension with Earth-like atmosphere and mild environmental hazards for Kepler-452b (Tier 6 temperate exoworld).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/kepler_452b.json`
- Atmospheric and environmental systems
- Gravity and hazard configuration

**Acceptance**

- Temperate exoworld dimension with Earth-like atmosphere
- Breathable air with slightly different composition
- Gravity set to 1.0 (Earth-like)
- Mild environmental hazards (seasonal storms, temperature variations)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create temperate exoworld dimension JSON
- [ ] Implement Earth-like atmosphere system
- [ ] Add breathable air with different composition
- [ ] Set gravity to 1.0
- [ ] Configure mild environmental hazards
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
