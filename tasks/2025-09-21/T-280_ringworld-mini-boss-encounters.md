# T-280 Ringworld Megastructure Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Ringworld Megastructure with unique mechanical and megastructure-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Aerial Behemoth (Natural Zones): Colossal sky whale corrupted with lightning veins
- Tempest Serpent (Urban Zones): Gigantic storm drake with crackling golden scales
- Storm Titan (Structural Areas): Humanoid giant armored with fulgurite plating
- Cyclone Guardian (Command Centers): Floating armored construct with glowing orb core
- Depth Leviathan (Maintenance Shafts): Titanic beast ~20 blocks long shimmering with metallic hydrogen glow
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Aerial Behemoth boss entity
- [ ] Create Tempest Serpent with golden scales
- [ ] Implement Storm Titan with fulgurite plating
- [ ] Add Cyclone Guardian with glowing orb core
- [ ] Create Depth Leviathan with metallic hydrogen glow
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
