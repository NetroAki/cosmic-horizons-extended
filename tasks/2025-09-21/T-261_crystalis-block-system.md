# T-261 Crystalis Block System

**Goal**

- Implement comprehensive block system for Crystalis with crystal, ice, and pressure materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Crystal Lattice Blocks: blocks with crystalline structure
- Cryo Ice: supercooled ice with unique properties
- Frost Glass: translucent ice blocks
- Pressure Ice: ice formed under extreme pressure
- Diamond Crystals: massive diamond formations
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Crystal Lattice Blocks
- [ ] Create Cryo Ice
- [ ] Add Frost Glass
- [ ] Implement Pressure Ice
- [ ] Create Diamond Crystals
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
