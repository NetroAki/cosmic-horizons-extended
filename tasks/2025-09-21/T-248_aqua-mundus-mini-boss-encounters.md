# T-248 Aqua Mundus Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Aqua Mundus with unique underwater and ocean-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Reef Guardian (Shallow Seas): Gigantic armored turtle ~8 blocks long with coral-encrusted shell
- Kelp Serpent (Kelp Forests): Snake-like aquatic creature ~12 blocks long that camouflages within kelp
- Leviathan Horror (Abyssal Trenches): Colossal anglerfish monster ~15 blocks long with blood-red lure
- Vent Titan (Hydrothermal Vents): Gigantic crustacean with obsidian-black shell glowing red underneath
- Glacier Colossus (Ice Shelves): Towering ice elemental humanoid ~10 blocks tall
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Reef Guardian boss entity
- [ ] Create Kelp Serpent with camouflage mechanics
- [ ] Implement Leviathan Horror with anglerfish lure
- [ ] Add Vent Titan with hydrothermal adaptations
- [ ] Create Glacier Colossus with ice elemental mechanics
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
