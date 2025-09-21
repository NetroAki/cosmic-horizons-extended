# T-050 Kepler-452b Biomes

**Goal**
- Build biome JSONs for Kepler-452b (Temperate Forest, Highlands, River Valleys, Meadowlands, Rocky Scrub).

**Scope**
- `data/cosmic_horizons_extended/worldgen/biome/kepler_*.json`

**Acceptance**
- Biomes reflect design (climate, ambience, vegetation references).
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author biome JSONs
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
