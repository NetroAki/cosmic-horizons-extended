# T-272 Stormworld Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Stormworld with unique storm and atmospheric mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Storm Titan (Upper Atmosphere): Colossal entity made of pure electrical energy
- Vortex Master (Storm Bands): Entity that controls gas vortices and wind patterns
- Pressure Colossus (Lightning Fields): Gigantic entity adapted to extreme pressure and temperature
- Reality Guardian (Eye of the Storm): Entity that guards exotic matter and reality distortions
- Cloud Sovereign (Metallic Hydrogen Depths): Entity that controls cloud formations and storm patterns
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Storm Titan boss entity
- [ ] Create Vortex Master with wind control
- [ ] Implement Pressure Colossus with pressure adaptation
- [ ] Add Reality Guardian with exotic matter
- [ ] Create Cloud Sovereign with storm control
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
