# T-241 Kepler-452b Verdant Colossus Boss

**Goal**

- Implement the main boss encounter for Kepler-452b with multi-phase nature and crystal mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Ancient grove arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Ancient grove with massive tree structures
- Appearance: Colossal plant-based entity with root systems and crystal growths
- Phase 1 - Root Phase: Summons root tendrils from ground, creates earth barriers
- Phase 2 - Growth Phase: Grows larger, spawns plant minions, creates overgrowth
- Phase 3 - Crystal Phase: Crystals emerge from body, creates crystal barriers, energy attacks
- Drops: Verdant Core (bio-metal composites unlock), Nature's Blade weapon, Life Essence (advanced biology)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Verdant Colossus boss entity
- [ ] Create ancient grove arena
- [ ] Implement Root Phase mechanics
- [ ] Add Growth Phase with plant minions
- [ ] Create Crystal Phase with energy attacks
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_verdant_colossus_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
