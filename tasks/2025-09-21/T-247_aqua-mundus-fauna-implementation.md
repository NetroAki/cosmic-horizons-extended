# T-247 Aqua Mundus Fauna Implementation

**Goal**

- Implement diverse fauna for Aqua Mundus with ocean, deep sea, and hydrothermal-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Luminfish Schools: small glowing fish that move in schools
- Hydrothermal Drones: mechanical creatures near vents
- Abyss Leviathan: massive deep-sea creature
- Tidal Jelly: large jellyfish-like creatures
- Vent Crabs: creatures adapted to hydrothermal environments
- Deep Sea Anglers: fish with bioluminescent lures
- Pressure Worms: creatures adapted to extreme pressure
- Current Riders: creatures that ride ocean currents
- Coral Guardians: creatures that protect coral formations
- Trench Crawlers: bottom-dwelling creatures
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Luminfish Schools with school behavior
- [ ] Create Hydrothermal Drones
- [ ] Add Abyss Leviathan
- [ ] Implement Tidal Jelly
- [ ] Create Vent Crabs and Deep Sea Anglers
- [ ] Add Pressure Worms and Current Riders
- [ ] Implement Coral Guardians and Trench Crawlers
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
