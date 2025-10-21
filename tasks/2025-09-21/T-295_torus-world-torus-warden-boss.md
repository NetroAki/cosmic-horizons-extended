# T-295 Torus World Torus Warden Boss

**Goal**

- Implement the main boss encounter for Torus World with multi-phase gravity and energy mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Central hub arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central hub with gravity manipulation
- Appearance: Massive entity that controls gravity
- Phase 1 - Gravity Manipulation Phase: Controls gravity fields, uses gravity-based attacks
- Phase 2 - Energy Phase: Uses energy systems, creates power-based effects
- Phase 3 - Structural Phase: Controls torus structure, uses structural attacks
- Drops: Torus Core (gravity tech unlock), Gravity Blade weapon, Torus Essence (gravity tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Torus Warden boss entity
- [ ] Create central hub arena
- [ ] Implement Gravity Manipulation Phase mechanics
- [ ] Add Energy Phase with power effects
- [ ] Create Structural Phase with structural attacks
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_torus_warden_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
