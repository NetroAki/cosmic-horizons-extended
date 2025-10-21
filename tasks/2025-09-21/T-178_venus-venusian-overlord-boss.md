# T-178 Venus Venusian Overlord Boss

**Goal**

- Add Venusian Overlord boss with atmospheric mechanics to enhance Venus [EXISTING] with challenging endgame content.

**Scope**

- Boss entity class with atmospheric and acid-based abilities
- Boss arena and encounter mechanics
- Loot drops and progression integration

**Acceptance**

- Venusian Overlord: Massive boss entity with atmospheric and acid-based attacks
- Boss arena: Unique atmospheric environment with corrosive mechanics
- Boss abilities: Atmospheric manipulation, acid cloud attacks, pressure changes
- Loot drops: Venusian-specific materials and progression items
- Integration with GTCEu for advanced atmospheric technology unlocks
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Venusian Overlord boss entity
- [ ] Create boss arena with atmospheric mechanics
- [ ] Implement atmospheric manipulation abilities
- [ ] Add acid cloud and pressure change attacks
- [ ] Create loot drops and progression integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_venus_venusian_overlord_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
