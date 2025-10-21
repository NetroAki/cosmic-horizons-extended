# T-245 Aqua Mundus Block System

**Goal**

- Implement comprehensive block system for Aqua Mundus with ocean, deep sea, and hydrothermal materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Vent Basalt: volcanic rock from hydrothermal vents
- Manganese Nodules: mineral-rich nodules on ocean floor
- Luminous Kelp Fronds: glowing kelp variants
- Ice Shelf Slabs: frozen water blocks with unique properties
- Pressure Stone: rock formed under extreme pressure
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Vent Basalt
- [ ] Create Manganese Nodules
- [ ] Add Luminous Kelp Fronds
- [ ] Implement Ice Shelf Slabs
- [ ] Create Pressure Stone
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
