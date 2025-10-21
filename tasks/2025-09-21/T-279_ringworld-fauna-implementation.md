# T-279 Ringworld Megastructure Fauna Implementation

**Goal**

- Implement diverse fauna for Ringworld Megastructure with mechanical, habitat, and maintenance-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Guardian Drones: mechanical guardians
- Habitat Fauna: creatures adapted to artificial environments
- Shadow Revenants: ghostly entities
- Maintenance Bots: mechanical maintenance creatures
- System Overlords: intelligent control entities
- Ringworld Birds: flying creatures adapted to artificial gravity
- Habitat Mammals: small creatures that live in habitation zones
- Maintenance Insects: small creatures that help with maintenance
- System Spirits: ethereal entities that represent the ringworld's consciousness
- Guardian Constructs: mechanical creatures that protect the ringworld
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Guardian Drones
- [ ] Create Habitat Fauna
- [ ] Add Shadow Revenants
- [ ] Implement Maintenance Bots
- [ ] Create System Overlords and Ringworld Birds
- [ ] Add Habitat Mammals and Maintenance Insects
- [ ] Implement System Spirits and Guardian Constructs
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
