# T-110 Exotica Dimension & Biomes

**Goal**

- Define the Exotica dimension (chromatic haze, variable gravity) and biome set (Chroma Steppes, Resonant Dunes, Quantum Glades, Fractal Forest, Prism Canyons) per the design document.

**Scope**

- Dimension JSON/noise settings capturing chromatic skybox, spatial distortion visuals, gravity toggles.
- Biome JSONs with color-cycling fog, ambience, weather, and surface builders for each listed biome.
- Biome tag curation for ore injection and feature routing.

**Acceptance**

- Datapack loads without errors; each biome presents intended colors/fog and is reachable via debug worldgen.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension/biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotica_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
