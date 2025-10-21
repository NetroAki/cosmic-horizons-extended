# T-305 Shattered Dyson Swarm Block System

**Goal**

- Implement comprehensive block system for Shattered Dyson Swarm with orbital debris, energy, and communication materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Solar Panel Segments: debris from solar panels
- Relay Nodes: communication relay blocks
- Scaffold Girders: structural support blocks
- Shadow Plating: blocks that block light
- Energy Fragments: fragments of energy systems
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Solar Panel Segments
- [ ] Create Relay Nodes
- [ ] Add Scaffold Girders
- [ ] Implement Shadow Plating
- [ ] Create Energy Fragments
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
