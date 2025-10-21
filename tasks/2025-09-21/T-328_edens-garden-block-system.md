# T-328 Eden's Garden Block System

**Goal**

- Implement comprehensive block system for Eden's Garden with paradise, healing, and harmony materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Paradise Stone: Perfect stone that never weathers or decays
- Healing Crystals: Crystals that provide regeneration and healing
- Harmony Blocks: Blocks that enhance all nearby life and growth
- Peaceful Wood: Wood that grows infinitely and provides perfect materials
- Tranquil Soil: Soil that grows any plant instantly and perfectly
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Paradise Stone
- [ ] Create Healing Crystals
- [ ] Add Harmony Blocks
- [ ] Implement Peaceful Wood
- [ ] Create Tranquil Soil
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_edens_garden_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
