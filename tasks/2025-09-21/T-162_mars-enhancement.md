# T-162 Mars Enhancement

**Goal**

- Enhance Mars [EXISTING] with terraforming biomes, boss encounters, GTCEu integration, structures, and environmental hazards.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/mars_*.json`
- Boss entity classes and Martian-specific mechanics
- GTCEu integration for Martian-specific ores and terraforming technology
- Structure generation and environmental hazard systems

**Acceptance**

- 5+ new terraforming biomes added (Terraformed Plains, Ancient Ruins, Dust Storms, Polar Caps, Underground Cities)
- 3 mini-bosses + 1 main boss implemented (Dust Wraith, Ancient Guardian, Polar Beast, Mars Overlord)
- GTCEu integration with Martian-specific ores and terraforming technology
- Terraforming facilities, ancient alien ruins, and research stations added
- Environmental hazards: dust storms, low gravity effects, and atmospheric pressure
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement terraforming biomes
- [ ] Create boss encounters and arenas
- [ ] Add GTCEu integration
- [ ] Implement structures
- [ ] Add environmental hazards
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mars_enhancement.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
