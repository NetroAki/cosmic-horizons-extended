# T-331 Eden's Garden Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Eden's Garden with unique paradise and harmony mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Garden Keeper (Eternal Gardens): Guardian of paradise that tests worthiness
- Healing Master (Crystal Springs): Master of healing and regeneration
- Harmony Guardian (Harmony Fields): Guardian of perfect harmony
- Peace Keeper (Peaceful Meadows): Keeper of peace and tranquility
- Forest Sage (Tranquil Forests): Sage of ancient wisdom and peace
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Garden Keeper boss entity
- [ ] Create Healing Master with regeneration mastery
- [ ] Implement Harmony Guardian with perfect harmony
- [ ] Add Peace Keeper with tranquility mastery
- [ ] Create Forest Sage with ancient wisdom
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_edens_garden_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
