# T-133 Hollow Tyrant Boss Encounter

**Goal**

- Implement the Hollow Tyrant main boss (Fungal Wrath → Crystal Dominion → Void Phase) with spherical arena, inverted gravity combat, and rewards (Hollow Heart, Tyrant Fangblade, Tyrant Core).

**Scope**

- Boss entity behaviour: spore cloud summons, crystal spike growth, void platform collapse.
- Central cavern arena structure with inverted walkable surfaces and collapsible sections.
- Loot/progression integration mapping Hollow Heart + associated drops into GT void catalyst chain.

**Acceptance**

- Fight executes all phases with inverted gravity mechanics and hazard transitions; rewards delivered and progression hooks validated.
- `./gradlew :forge:runData` (if structures) + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics/arena/loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollowworld_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
