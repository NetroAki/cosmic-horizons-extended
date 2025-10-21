# T-090 Stormworld Layers & Biomes

**Goal**

- Implement Stormworld layered biomes (Upper Atmosphere, Storm Bands, Lightning Fields, Eye, Metallic Hydrogen Depths).

**Scope**

- Dimension/biome JSONs, noise settings.

**Acceptance**

- Layers generate with distinct parameters; `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension/biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
