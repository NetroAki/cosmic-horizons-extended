# T-253 Inferno Prime Block System

**Goal**

- Implement comprehensive block system for Inferno Prime with volcanic, lava, and ash materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Magma Geysers: blocks that erupt with lava
- Basalt Pillars: tall columns of volcanic rock
- Obsidian Flora: plant life adapted to volcanic glass
- Ash Dunes: blocks made of volcanic ash
- Molten Rock: various types of heated stone
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Magma Geysers
- [ ] Create Basalt Pillars
- [ ] Add Obsidian Flora
- [ ] Implement Ash Dunes
- [ ] Create Molten Rock variants
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
