# T-307 Shattered Dyson Swarm Fauna Implementation

**Goal**

- Implement diverse fauna for Shattered Dyson Swarm with orbital debris, energy, and communication-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Solar Warden: guardian of solar panel areas
- Node Horror: creature of broken energy nodes
- Scaffold Titan: massive creature of scaffolding
- Radiant Abomination: creature of energy systems
- Signal Overlord: creature that controls communication
- Orbital Birds: flying creatures adapted to zero-gravity
- Debris Crawlers: creatures that live in orbital debris
- Energy Sprites: small creatures that feed on energy
- Signal Bats: flying creatures that use communication signals
- Orbital Fish: aquatic creatures in zero-gravity water systems
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Solar Warden
- [ ] Create Node Horror
- [ ] Add Scaffold Titan
- [ ] Implement Radiant Abomination
- [ ] Create Signal Overlord
- [ ] Add Orbital Birds and Debris Crawlers
- [ ] Implement Energy Sprites and Signal Bats
- [ ] Create Orbital Fish
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
