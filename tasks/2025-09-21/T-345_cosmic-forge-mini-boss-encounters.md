# T-345 The Cosmic Forge Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for The Cosmic Forge with unique creation and crafting mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Forge Master (Forge Chambers): Ultimate craftsman controlling all creation
- Material Guardian (Material Vaults): Guardian of infinite materials
- Creation Keeper (Creation Altars): Keeper of creation altars
- Essence Master (Essence Wells): Master of material essence
- Workshop Sage (Master Workshops): Sage of perfect crafting
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Forge Master boss entity
- [ ] Create Material Guardian with material protection
- [ ] Implement Creation Keeper with altar mastery
- [ ] Add Essence Master with essence control
- [ ] Create Workshop Sage with perfect crafting mastery
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
