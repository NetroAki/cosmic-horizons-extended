# T-387 Suit Hazard System

**Goal**

- Implement suit hazard system (vacuum, thermal, radiation, corrosive, high pressure) with proper damage and mitigation mechanics.

**Scope**

- `forge/src/main/java/com/netroaki/chex/hazards/`
- Suit hazard system implementation
- Hazard damage and mitigation

**Acceptance**

- Suit hazard system implemented
- Vacuum, thermal, radiation, corrosive, high pressure hazards
- Proper damage and mitigation mechanics
- Integration with suit systems
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create suit hazard system
- [ ] Implement all hazard types
- [ ] Add damage and mitigation mechanics
- [ ] Integrate with suit systems
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_suit_hazard_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
