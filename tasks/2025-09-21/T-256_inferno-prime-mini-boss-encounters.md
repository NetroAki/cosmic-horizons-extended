# T-256 Inferno Prime Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Inferno Prime with unique volcanic and fire-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Pyrohydra (Lava Seas): Multi-headed serpent erupting from lava pools
- Basalt Colossus (Basalt Flats): Towering basalt golem with molten core visible through cracks
- Obsidian Revenant (Obsidian Isles): Tall humanoid formed from obsidian shards with purple inner light
- Ash Titan (Ash Wastes): Gigantic quadruped creature with ash armor
- Sulfur Behemoth (Magma Caverns): Massive lava-crust beast with sulfur nodules embedded in armor
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Pyrohydra boss entity
- [ ] Create Basalt Colossus with molten core
- [ ] Implement Obsidian Revenant with purple light
- [ ] Add Ash Titan with ash armor
- [ ] Create Sulfur Behemoth with sulfur nodules
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
