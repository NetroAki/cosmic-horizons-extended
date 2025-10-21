# T-031 Arrakis Biomes

**Goal**

- Define Arrakis biome JSONs (Great Dunes, Spice Mines, Polar Ice Caps, Sietch Strongholds, Stormlands) with sandstorm ambience.

**Scope**

- `data/cosmic_horizons_extended/worldgen/biome/arrakis_*.json`
- Feature references for storm effects.

**Acceptance**

- Biomes reflect climate, sky, particles; datagen succeeds.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author biome JSONs
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
