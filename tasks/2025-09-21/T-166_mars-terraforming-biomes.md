# T-166 Mars Terraforming Biomes

**Goal**

- Add 5+ new terraforming biomes (Terraformed Plains, Ancient Ruins, Dust Storms, Polar Caps, Underground Cities) to enhance Mars [EXISTING] with diverse environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/mars_*.json`
- Biome-specific terrain generation and atmospheric effects
- Terraforming technology integration

**Acceptance**

- Terraformed Plains: Areas with Earth-like vegetation and atmosphere
- Ancient Ruins: Pre-terraforming Martian structures and artifacts
- Dust Storms: Dynamic weather zones with reduced visibility and hazards
- Polar Caps: Frozen regions with unique Martian ice formations
- Underground Cities: Subsurface habitation with life support systems
- Each biome reflects different stages of terraforming process
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Terraformed Plains with Earth-like features
- [ ] Create Ancient Ruins with pre-terraforming structures
- [ ] Implement Dust Storms with dynamic weather effects
- [ ] Add Polar Caps with Martian ice formations
- [ ] Create Underground Cities with life support systems
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mars_terraforming_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
