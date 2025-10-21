# T-316 Neutron Star Forge Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Neutron Star Forge with unique neutron star and extreme physics-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Accretion Leviathan (Accretion Rim): Gigantic beast formed from molten alloy and plasma
- Magnetar Colossus (Magnetar Belts): Giant armored golem magnetized, attracting weapons from player hands
- Forge Overseer (Forge Platforms): Massive quadrupedal machine controlling forge platforms
- Graviton Horror (Gravity Wells): Colossal beast shifting between forms, bending light around itself
- Shelter Sentinel (Radiation Shelters): Massive armored construct with shield domes
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Accretion Leviathan boss entity
- [ ] Create Magnetar Colossus with weapon attraction
- [ ] Implement Forge Overseer with platform control
- [ ] Add Graviton Horror with light bending
- [ ] Create Shelter Sentinel with shield domes
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
