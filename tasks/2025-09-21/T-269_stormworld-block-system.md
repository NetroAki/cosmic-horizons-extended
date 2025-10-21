# T-269 Stormworld Block System

**Goal**

- Implement comprehensive block system for Stormworld with storm, electrical, and atmospheric materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Storm Cloud Blocks: blocks made of storm clouds
- Charge Collectors: blocks that collect electrical energy
- Lightning Rods: blocks that attract lightning
- Hydrogen Pools: pools of metallic hydrogen
- Atmospheric Gases: various gas blocks
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Storm Cloud Blocks
- [ ] Create Charge Collectors
- [ ] Add Lightning Rods
- [ ] Implement Hydrogen Pools
- [ ] Create Atmospheric Gases
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
