# T-285 Exotica Flora Generation

**Goal**

- Implement unique flora generation for Exotica with surreal, quantum, and fractal-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and surreal effects

**Acceptance**

- Chroma Grass: grass that changes colors constantly
- Resonance Crystals: crystals that respond to sound and vibration
- Fractal Trees: trees with fractal-based growth patterns
- Prism Flowers: flowers that refract light into rainbows
- Quantum Vines: plants that exist in multiple states simultaneously
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Chroma Grass with constant color changing
- [ ] Create Resonance Crystals with sound/vibration response
- [ ] Add Fractal Trees with fractal growth patterns
- [ ] Implement Prism Flowers with rainbow refraction
- [ ] Create Quantum Vines with multiple states
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
