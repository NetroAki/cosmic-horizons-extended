# T-160 Earth Enhancement

**Goal**

- Enhance Earth [EXISTING] with additional biomes, boss encounters, GTCEu integration, structures, and environmental hazards.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/earth_*.json`
- Boss entity classes and arena structures
- GTCEu integration for Earth-specific ores and processing chains
- Structure generation and environmental hazard systems

**Acceptance**

- 5+ new biomes added (Urban Sprawl, Industrial Zones, Nuclear Wastelands, Underground Cities, Sky Islands)
- 3 mini-bosses + 1 main boss implemented (Industrial Overlord, Nuclear Guardian, Sky Tyrant, Earth's Heart)
- GTCEu integration with advanced Earth-specific ores and processing chains
- Space launch facilities, research centers, and advanced technology hubs added
- Environmental hazards: pollution effects, radiation zones, and industrial accidents
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement additional biomes
- [ ] Create boss encounters and arenas
- [ ] Add GTCEu integration
- [ ] Implement structures
- [ ] Add environmental hazards
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_earth_enhancement.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
