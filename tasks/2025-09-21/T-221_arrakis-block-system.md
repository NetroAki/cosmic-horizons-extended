# T-221 Arrakis Block System

**Goal**

- Implement comprehensive block system for Arrakis with desert, underground, and storm materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Arrakite Sandstone Family: smooth, chiseled, cracked, fossil-embedded variants (golden-red sandstone)
- Arrakite Sand: custom golden sand with finer particle effect
- Dune Fossil Sandstone: rare blocks with embedded fossil textures
- Spice Ore Nodes: decorative ore veins glowing faint red (drop GT Sulfur Dust + Spice Extract)
- Arrakite Rock Salt: reddish saltstone blocks
- Arrakite Frost Stone: bluish stone with frosty cracks
- Polar Ice: translucent blocks with faint blue core glow
- Carved Arrakite Bricks: decorative brick set for strongholds
- Spice-Lantern Crystals: glowing orange crystal lamps
- Stormstone: fractured black-orange rock, constantly weathered
- Glass Flats: obsidian-like glass blocks from lightning strikes
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Arrakite Sandstone family
- [ ] Create Arrakite Sand and Dune Fossil Sandstone
- [ ] Add Spice Ore Nodes and Arrakite Rock Salt
- [ ] Implement Arrakite Frost Stone and Polar Ice
- [ ] Create Carved Arrakite Bricks and Spice-Lantern Crystals
- [ ] Add Stormstone and Glass Flats
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
