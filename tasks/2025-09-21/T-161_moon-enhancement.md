# T-161 Moon Enhancement

**Goal**

- Enhance Moon [EXISTING] with lunar biomes, space stations, advanced mining, boss encounters, and GTCEu integration.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/moon_*.json`
- Space station structures and lunar base generation
- Zero-gravity mining operations and resource extraction
- Boss entity classes and lunar-specific mechanics

**Acceptance**

- 5+ new lunar biomes added (Mare Basins, Highland Craters, Polar Ice Caps, Lunar Caves, Impact Sites)
- Orbital space stations and lunar bases implemented
- Zero-gravity mining operations and resource extraction systems
- Lunar Overlord boss with moon-based mechanics
- GTCEu integration with lunar-specific materials and processing chains
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement lunar biomes
- [ ] Create space stations and lunar bases
- [ ] Add zero-gravity mining operations
- [ ] Implement Lunar Overlord boss
- [ ] Add GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_moon_enhancement.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
