# T-167 Mars Martian Boss Encounters

**Goal**

- Add 3 mini-bosses + 1 main boss (Dust Wraith, Ancient Guardian, Polar Beast, Mars Overlord) to enhance Mars [EXISTING] with challenging encounters.

**Scope**

- Boss entity classes with Martian-specific abilities
- Boss arenas and encounter mechanics
- Loot drops and progression integration

**Acceptance**

- Dust Wraith: Sand-based boss with dust storm abilities
- Ancient Guardian: Pre-terraforming entity with ancient technology
- Polar Beast: Ice-based boss with cryogenic attacks
- Mars Overlord: Main boss with terraforming and dust-based mechanics
- Each boss has unique arena and attack patterns
- Loot drops integrate with GTCEu progression
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Dust Wraith boss with sand abilities
- [ ] Create Ancient Guardian with ancient technology
- [ ] Implement Polar Beast with cryogenic attacks
- [ ] Design Mars Overlord main boss
- [ ] Create boss arenas and encounter mechanics
- [ ] Add loot drops and progression integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mars_martian_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
