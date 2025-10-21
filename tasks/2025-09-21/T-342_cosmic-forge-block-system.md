# T-342 The Cosmic Forge Block System

**Goal**

- Implement comprehensive block system for The Cosmic Forge with creation, crafting, and mastery materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Forge Stone: Stone that enhances all crafting and creation
- Creation Crystals: Crystals that provide infinite materials
- Essence Blocks: Blocks containing pure essence of all materials
- Crafting Altars: Altars that can create anything
- Master Tools: Tools that never break and provide perfect results
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Forge Stone
- [ ] Create Creation Crystals
- [ ] Add Essence Blocks
- [ ] Implement Crafting Altars
- [ ] Create Master Tools
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
