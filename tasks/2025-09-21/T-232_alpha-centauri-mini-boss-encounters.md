# T-232 Alpha Centauri A Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Alpha Centauri A with unique stellar and plasma mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Helios Warden (Photosphere): ~4 blocks tall humanoid of molten stone and plasma, wields glowing spear
- Flare Serpent (Corona): gigantic plasma eel ~15 blocks long, swims in and out of streams
- Aurora Titan (Magnetosphere): colossal humanoid made of pure magnetic energy with crystalline armor fragments orbiting its body
- Osmium Colossus (Sunspot Fields): massive molten golem with dark reflective armor plates
- Array Overlord (Solar Arrays): giant drone with solar-dish head and multiple wings of broken panel shards
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Helios Warden boss entity
- [ ] Create Flare Serpent with plasma swimming
- [ ] Implement Aurora Titan with magnetic energy
- [ ] Add Osmium Colossus with molten golem mechanics
- [ ] Create Array Overlord with drone technology
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
