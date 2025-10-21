# T-315 Neutron Star Forge Fauna Implementation

**Goal**

- Implement diverse fauna for Neutron Star Forge with neutron star, magnetic, and forge-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Accretion Leviathan: massive creature of accretion rim
- Magnetar Colossus: creature of magnetic fields
- Forge Overseer: creature that oversees forging
- Graviton Horror: creature of gravity wells
- Shelter Sentinel: guardian of radiation shelters
- Neutron Birds: flying creatures adapted to extreme gravity
- Magnetic Worms: creatures that live in magnetic fields
- Forge Spiders: creatures that live in forge environments
- Graviton Fish: aquatic creatures in gravity wells
- Radiation Bats: flying creatures adapted to radiation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Accretion Leviathan
- [ ] Create Magnetar Colossus
- [ ] Add Forge Overseer
- [ ] Implement Graviton Horror
- [ ] Create Shelter Sentinel
- [ ] Add Neutron Birds and Magnetic Worms
- [ ] Implement Forge Spiders and Graviton Fish
- [ ] Create Radiation Bats
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
