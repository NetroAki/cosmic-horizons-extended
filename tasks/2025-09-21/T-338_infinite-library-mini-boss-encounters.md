# T-338 The Infinite Library Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for The Infinite Library with unique knowledge and wisdom mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Archivist (Ancient Archives): Ancient entity guarding universe knowledge
- Tome Keeper (Floating Tomes): Entity guarding floating tomes and knowledge
- Crystal Sage (Crystal Chambers): Entity understanding deepest GT secrets
- Garden Sage (Wisdom Gardens): Entity tending wisdom gardens
- Cosmic Scribe (Cosmic Scriptorium): Entity writing universe knowledge
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Archivist boss entity
- [ ] Create Tome Keeper with knowledge protection
- [ ] Implement Crystal Sage with GT secret mastery
- [ ] Add Garden Sage with wisdom garden tending
- [ ] Create Cosmic Scribe with universe knowledge writing
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_infinite_library_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
