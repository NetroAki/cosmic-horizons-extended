# T-281 Ringworld Megastructure Guardian Prime Boss

**Goal**

- Implement the main boss encounter for Ringworld Megastructure with multi-phase mechanical and system control mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Central hub arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central hub with multiple platforms
- Appearance: Massive mechanical entity with multiple phases
- Phase 1 - Defense Phase: Activates defensive systems, creates barriers
- Phase 2 - Attack Phase: Uses offensive systems, launches attacks
- Phase 3 - System Override Phase: Takes control of ringworld systems
- Drops: Prime Core (nanomaterials/robotics unlock), Prime Blade weapon, Ringworld Essence (megastructure tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Guardian Prime boss entity
- [ ] Create central hub arena
- [ ] Implement Defense Phase mechanics
- [ ] Add Attack Phase with offensive systems
- [ ] Create System Override Phase with ringworld control
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_guardian_prime_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
