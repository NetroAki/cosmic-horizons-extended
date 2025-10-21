# T-296 Hollow World Dimension Setup

**Goal**

- Create megacavern dimension with vertical layering, void mechanics, and pressure-based systems for Hollow World (Tier 14 megacavern).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/hollow_world.json`
- Megacavern environment and void systems
- Vertical layering and pressure mechanics

**Acceptance**

- Megacavern dimension with vertical layering
- Void mechanics
- Pressure-based systems
- Cavern-based terrain generation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create megacavern dimension JSON
- [ ] Implement vertical layering systems
- [ ] Add void mechanics
- [ ] Configure pressure-based systems
- [ ] Set cavern-based terrain generation
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
