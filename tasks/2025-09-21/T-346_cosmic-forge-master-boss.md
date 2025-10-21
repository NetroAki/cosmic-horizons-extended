# T-346 The Cosmic Forge The Forge Master Boss

**Goal**

- Implement the main boss encounter for The Cosmic Forge with multi-phase creation, mastery, and cosmic mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Center of Cosmic Forge arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Center of Cosmic Forge with massive anvil and floating tools
- Appearance: Ultimate craftsman surrounded by floating tools and embers
- Phase 1 - Creation Phase: Provides infinite materials and perfect crafting
- Phase 2 - Mastery Phase: Creates perfect items and enhances all crafting
- Phase 3 - Cosmic Phase: Becomes one with creation itself
- Drops: Forge Master's Blessing (Tier 15+ Creation Mastery), Creation Blade weapon, Master's Crown
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design The Forge Master boss entity
- [ ] Create center of Cosmic Forge arena
- [ ] Implement Creation Phase mechanics
- [ ] Add Mastery Phase with perfect crafting
- [ ] Create Cosmic Phase with creation mastery
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_master_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
