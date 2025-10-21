# T-268 Stormworld Biome Implementation

**Goal**

- Implement 5 unique biomes for Stormworld with atmospheric, storm, and lightning environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/stormworld_*.json`
- Biome-specific terrain generation and atmospheric effects
- Storm and lightning exploration systems

**Acceptance**

- Upper Atmosphere: Light, floating areas
- Storm Bands: Areas with constant storms
- Lightning Fields: Areas with frequent lightning
- Eye of the Storm: Calm center of storm systems
- Metallic Hydrogen Depths: Deep areas with metallic hydrogen
- Each biome has distinct visual characteristics and atmospheric effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Upper Atmosphere biome
- [ ] Create Storm Bands biome
- [ ] Implement Lightning Fields biome
- [ ] Add Eye of the Storm biome
- [ ] Create Metallic Hydrogen Depths biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
