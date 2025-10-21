# T-293 Torus World Fauna Implementation

**Goal**

- Implement diverse fauna for Torus World with toroidal, gravity, and energy-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Forest Guardian: guardian of inner rim forest
- Desert Colossus: massive creature of outer rim desert
- Spine Overseer: creature that maintains structural spine
- Luminal Titan: creature of radiant fields
- Exotic Horror: creature of null-gravity zones
- Gravity Birds: flying creatures adapted to toroidal gravity
- Torus Mammals: creatures that live in the torus structure
- Energy Sprites: small creatures that feed on energy
- Gravity Worms: creatures that move through gravity fields
- Torus Fish: aquatic creatures in torus water systems
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Forest Guardian
- [ ] Create Desert Colossus
- [ ] Add Spine Overseer
- [ ] Implement Luminal Titan
- [ ] Create Exotic Horror
- [ ] Add Gravity Birds and Torus Mammals
- [ ] Implement Energy Sprites and Gravity Worms
- [ ] Create Torus Fish
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
