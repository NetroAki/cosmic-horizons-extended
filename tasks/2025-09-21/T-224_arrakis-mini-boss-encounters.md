# T-224 Arrakis Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Arrakis with unique mechanics and arena designs.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Dune Wyrm Alpha (Great Dunes): ~15 blocks long worm, armored in red-gold chitin, burrows and emerges in ambush
- Spice Guardian (Spice Mines): massive insectoid with crystal-encrusted armor, emits choking spice clouds
- Cryo Leviathan (Polar Ice Caps): huge reptilian beast ~8 blocks long with ice-crystal armor plates
- Sietch Warlord (Sietch Strongholds): humanoid elite with spice-crystal armor, dual curved blades
- Storm Titan (Stormlands): colossal quadruped with rocky exoskeleton and glowing eyes
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Dune Wyrm Alpha boss entity
- [ ] Create Spice Guardian with spice cloud mechanics
- [ ] Implement Cryo Leviathan with ice attacks
- [ ] Add Sietch Warlord with dual blade combat
- [ ] Create Storm Titan with rocky exoskeleton
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
