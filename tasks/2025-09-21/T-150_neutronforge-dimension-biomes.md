# T-150 Neutron Star Forge Dimension & Biomes

**Goal**

- Define Neutron Star Forge dimension with extreme gravity/radiation and biomes (Accretion Rim, Magnetar Belts, Forge Platforms, Gravity Wells, Radiation Shelters).

**Scope**

- Dimension JSON/noise for neutron star proximity, gravity scaling, plasma lighting.
- Biome JSONs detailing hazards, fog, ambience, and block palettes per biome.
- Biome tags for ore/hazard distribution.

**Acceptance**

- Dimension loads with intended gravity/radiation metadata; each biome spawns via debug worldgen with correct ambience.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension/biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutronforge_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
