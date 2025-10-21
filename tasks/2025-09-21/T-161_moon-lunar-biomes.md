# T-161 Moon Lunar Biomes

**Goal**

- Add 5+ new lunar biomes (Mare Basins, Highland Craters, Polar Ice Caps, Lunar Caves, Impact Sites) to enhance Moon [EXISTING] exploration.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/moon_*.json`
- Biome-specific terrain generation and block palettes
- Lunar-specific environmental effects and lighting

**Acceptance**

- Mare Basins: Flat, dark areas with basalt-like terrain and regolith
- Highland Craters: Elevated areas with crater formations and rocky terrain
- Polar Ice Caps: Frozen regions with ice formations and cryogenic materials
- Lunar Caves: Underground tunnel systems with unique lunar geology
- Impact Sites: Crater formations with scattered debris and unique materials
- Each biome has distinct visual characteristics and resource distribution
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Mare Basins biome with basalt terrain
- [ ] Create Highland Craters with elevated rocky formations
- [ ] Implement Polar Ice Caps with cryogenic features
- [ ] Add Lunar Caves with tunnel systems
- [ ] Create Impact Sites with debris and unique materials
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_moon_lunar_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
