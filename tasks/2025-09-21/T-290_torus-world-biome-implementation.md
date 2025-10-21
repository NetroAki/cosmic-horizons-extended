# T-290 Torus World Biome Implementation

**Goal**

- Implement 5 unique biomes for Torus World with toroidal, gravity, and energy environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_world_*.json`
- Biome-specific terrain generation and toroidal effects
- Gravity and energy exploration systems

**Acceptance**

- Inner Rim Forest: Forest areas on inner rim
- Outer Rim Desert: Desert areas on outer rim
- Structural Spine: Central support structure
- Radiant Fields: Energy collection areas
- Null-G Hubs: Zero-gravity areas
- Each biome has distinct visual characteristics and toroidal effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Inner Rim Forest biome
- [ ] Create Outer Rim Desert biome
- [ ] Implement Structural Spine biome
- [ ] Add Radiant Fields biome
- [ ] Create Null-G Hubs biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torus_world_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
