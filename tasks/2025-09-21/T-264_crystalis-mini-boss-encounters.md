# T-264 Crystalis Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Crystalis with unique frozen and crystal-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Crystal Colossus (Diamond Fields): Massive golem made entirely of diamond-encrusted ice
- Frost Titan (Frosted Plains): Giant humanoid beast covered in ice plates
- Cryovore (Cryo Geysers): Gigantic insectoid with spiny frozen carapace
- Glacier Sentinel (Ice Cliffs): Towering humanoid carved from glacier ice with wings of frozen snow
- Abyssal Colossus (Pressure Depths): Gigantic eel-like monster armored with crystalline plates
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Crystal Colossus boss entity
- [ ] Create Frost Titan with ice plates
- [ ] Implement Cryovore with frozen carapace
- [ ] Add Glacier Sentinel with frozen wings
- [ ] Create Abyssal Colossus with crystalline plates
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
