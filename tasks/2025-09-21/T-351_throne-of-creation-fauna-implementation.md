# T-351 The Throne of Creation Fauna Implementation

**Goal**

- Implement diverse fauna for The Throne of Creation with ultimate dimension and creation-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Creation Guardians: Entities protecting throne (drop Guardian Essence)
- Existence Weavers: Beings manipulating reality fabric (drop Reality Threads)
- Spirit Guides: Entities representing all previous bosses (drop Spirit Essence)
- Mastery Guardians: Creatures testing planet mastery (drop Mastery Cores)
- Void Entities: Beings from beyond reality (drop Void Essence)
- Reality Eaters: Creatures consuming space-time fabric (drop Fabric Fragments)
- Creation Entities: Beings from source of creation (drop Creation Essence)
- Existence Eaters: Creatures consuming existence essence (drop Existence Fragments)
- Cosmic Flora: Plant-like entities spanning biomes (drop Cosmic Seeds)
- Metal Beasts: Creatures made of living metal (drop Metal Essence)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Creation Guardians
- [ ] Create Existence Weavers
- [ ] Add Spirit Guides
- [ ] Implement Mastery Guardians
- [ ] Create Void Entities
- [ ] Add Reality Eaters
- [ ] Implement Creation Entities
- [ ] Create Existence Eaters
- [ ] Add Cosmic Flora and Metal Beasts
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_throne_of_creation_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
