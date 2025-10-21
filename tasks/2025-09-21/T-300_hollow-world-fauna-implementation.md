# T-300 Hollow World Fauna Implementation

**Goal**

- Implement diverse fauna for Hollow World with cavern, void, and crystal-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Mycelium Horror: creature made of fungal material
- Abyss Wyrm: massive worm-like creature
- Crystal Titan: creature made of crystal
- Stalactite Horror: creature that resembles stalactites
- River Leviathan: massive creature of subterranean rivers
- Cavern Bats: flying creatures adapted to dark environments
- Void Spiders: creatures that live in void chasms
- Crystal Beetles: small creatures with crystalline armor
- Underground Fish: aquatic creatures in subterranean rivers
- Cavern Wolves: pack hunters adapted to underground life
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Mycelium Horror
- [ ] Create Abyss Wyrm
- [ ] Add Crystal Titan
- [ ] Implement Stalactite Horror
- [ ] Create River Leviathan
- [ ] Add Cavern Bats and Void Spiders
- [ ] Implement Crystal Beetles and Underground Fish
- [ ] Create Cavern Wolves with pack behavior
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
