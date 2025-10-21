# T-209 Asteroid Belt Asteroid King Boss

**Goal**

- Add Asteroid King boss with space-based mechanics to enhance Asteroid Belt [EXISTING] with challenging endgame content.

**Scope**

- Boss entity class with space and asteroid abilities
- Boss arena and encounter mechanics
- Loot drops and progression integration

**Acceptance**

- Asteroid King: Massive boss entity with space and asteroid-based attacks
- Boss arena: Unique space environment with asteroid mechanics
- Boss abilities: Asteroid manipulation, space-based attacks, zero-gravity effects
- Loot drops: Space-specific materials and progression items
- Integration with GTCEu for advanced space technology unlocks
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Asteroid King boss entity
- [ ] Create boss arena with space mechanics
- [ ] Implement asteroid manipulation abilities
- [ ] Add space-based and zero-gravity attack mechanics
- [ ] Create loot drops and progression integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_asteroid_belt_asteroid_king_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
