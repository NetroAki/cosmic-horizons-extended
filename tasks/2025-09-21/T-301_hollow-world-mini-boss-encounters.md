# T-301 Hollow World Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Hollow World with unique cavern and void-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Mycelium Horror (Bioluminescent Caverns): Colossal fungal mass with dozens of glowing tendrils
- Abyss Wyrm (Void Chasms): Serpent ~20 blocks long emerging from void fog
- Crystal Titan (Crystal Groves): Giant crystalline humanoid ~12 blocks tall
- Stalactite Horror (Stalactite Forest): Gigantic spider-beast fused with stone
- River Leviathan (Subterranean Rivers): Colossal eel with armored scales
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Mycelium Horror boss entity
- [ ] Create Abyss Wyrm with void fog emergence
- [ ] Implement Crystal Titan with crystalline form
- [ ] Add Stalactite Horror with stone fusion
- [ ] Create River Leviathan with armored scales
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
