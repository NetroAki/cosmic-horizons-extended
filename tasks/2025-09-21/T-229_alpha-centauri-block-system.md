# T-229 Alpha Centauri A Block System

**Goal**

- Implement comprehensive block system for Alpha Centauri A with stellar, plasma, and magnetic materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Solar Rock Family: black, pitted basalt-like stone with glowing golden cracks (smooth, cracked, tiled variants)
- Plasma Crystal Nodes: glowing clusters that pulse orange-yellow light
- Plasma Stream Blocks: animated texture of glowing orange-yellow currents
- Solar Glass: hardened plasma turned into glowing translucent glassy stone
- Magnetite Rock: black metallic stone with faint shimmering sheen
- Charged Crystal Clusters: glowing blue-violet crystals that arc small sparks
- Sunspot Basalt: dark, magnetic rock with swirling patterns
- Solar Panel Segments: reflective panels that collect and channel solar energy
- Energy Conduits: glowing tubes that transport collected solar energy
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Solar Rock family
- [ ] Create Plasma Crystal Nodes and Stream Blocks
- [ ] Add Solar Glass and Magnetite Rock
- [ ] Implement Charged Crystal Clusters
- [ ] Create Sunspot Basalt and Solar Panel Segments
- [ ] Add Energy Conduits
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
