# T-130 Hollow World Dimension & Biomes

**Goal**

- Define Hollow World inverted dimension with biomes (Bioluminescent Caverns, Void Chasms, Crystal Groves, Stalactite Forest, Subterranean Rivers) and inverted gravity visuals.

**Scope**

- Dimension JSON/noise for inner-shell terrain, void chasm carving, glowing sky/ceiling.
- Biome JSONs capturing lighting (biolume patches, darkness pockets), hazard flags, and ambience per biome.
- Biome tags for ore layers and hazard routing.

**Acceptance**

- Dimension loads with inverted gravity cues and biome placement matching design doc; debug worldgen shows each biome.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension/biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollowworld_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
