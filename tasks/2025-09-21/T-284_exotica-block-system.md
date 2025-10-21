# T-284 Exotica Block System

**Goal**

- Implement comprehensive block system for Exotica with surreal, quantum, and fractal materials.

**Scope**

- Block registrations in `forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java`
- Assets under `forge/src/main/resources/assets/cosmic_horizons_extended/`
- Block models, textures, and properties

**Acceptance**

- Chroma Grass: grass that changes colors
- Resonance Crystals: crystals that respond to sound
- Fractal Trees: trees with fractal growth patterns
- Prism Stone: stone with prismatic properties
- Quantum Flora: plant life with quantum properties
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Chroma Grass with color changing
- [ ] Create Resonance Crystals with sound response
- [ ] Add Fractal Trees with fractal patterns
- [ ] Implement Prism Stone with prismatic properties
- [ ] Create Quantum Flora with quantum properties
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_block_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
