# T-283 Exotica Biome Implementation

**Goal**

- Implement 5 unique biomes for Exotica with surreal, quantum, and fractal environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/exotica_*.json`
- Biome-specific terrain generation and surreal effects
- Quantum and fractal exploration systems

**Acceptance**

- Chroma Steppes: Areas with constantly changing colors
- Resonant Dunes: Sand dunes that respond to sound
- Quantum Glades: Areas with quantum mechanics
- Fractal Forest: Forest with fractal-based growth patterns
- Prism Canyons: Canyons with prismatic light effects
- Each biome has distinct visual characteristics and surreal effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Chroma Steppes biome
- [ ] Create Resonant Dunes biome
- [ ] Implement Quantum Glades biome
- [ ] Add Fractal Forest biome
- [ ] Create Prism Canyons biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
