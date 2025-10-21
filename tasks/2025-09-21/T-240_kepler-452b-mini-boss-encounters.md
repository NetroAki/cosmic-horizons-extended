# T-240 Kepler-452b Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Kepler-452b with unique alien and nature-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Grove Guardian (Temperate Forest): Tree-like golem made of moss and stone with glowing green eyes
- Highland Titan (Highlands): Gigantic ram golem with stone armor and glowing horns
- River Leviathan (River Valleys): Gigantic armored crocodile with jagged crystalline plates
- Thunder Herald (Meadowlands): Stag-like creature with massive lightning-infused antlers
- Stoneback Behemoth (Rocky Scrub): Massive tortoise-like beast with rocky armor
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Grove Guardian boss entity
- [ ] Create Highland Titan with stone armor
- [ ] Implement River Leviathan with crystalline plates
- [ ] Add Thunder Herald with lightning antlers
- [ ] Create Stoneback Behemoth with rocky armor
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
