# T-313 Neutron Star Forge Block System

**Goal**

- Implement comprehensive block system for Neutron Star Forge with neutron star, magnetic, and forge materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Neutronium Crust: blocks made of neutron star material
- Magnetar Coils: blocks that generate magnetic fields
- Forge Reactor Blocks: blocks that forge matter
- Graviton Pillars: blocks that control gravity
- Radiation Shields: blocks that block radiation
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Neutronium Crust
- [ ] Create Magnetar Coils
- [ ] Add Forge Reactor Blocks
- [ ] Implement Graviton Pillars
- [ ] Create Radiation Shields
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
