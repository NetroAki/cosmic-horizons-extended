# T-302 Hollow World Hollow Tyrant Boss

**Goal**

- Implement the main boss encounter for Hollow World with multi-phase crystal, void, and mycelium mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Central cavern arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central cavern with massive crystal formations
- Appearance: Colossal entity of crystal and void
- Phase 1 - Crystal Phase: Uses crystal attacks, creates crystal barriers
- Phase 2 - Void Phase: Uses void attacks, creates void chasms
- Phase 3 - Mycelium Phase: Uses fungal attacks, creates mycelium growth
- Drops: Hollow Heart (void catalysts/pressure reactors unlock), Void Blade weapon, Hollow Essence (void tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Hollow Tyrant boss entity
- [ ] Create central cavern arena
- [ ] Implement Crystal Phase mechanics
- [ ] Add Void Phase with void chasms
- [ ] Create Mycelium Phase with fungal growth
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_hollow_tyrant_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
