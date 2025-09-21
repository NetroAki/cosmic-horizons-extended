# T-021 Pandora Biome JSON Suite

**Goal**
- Create biome JSON definitions for Pandora (Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands) with climate, fog, weather, ambience.

**Scope**
- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_*.json`
- Related placed/configured feature references (stubs acceptable if follow-up tasks own generation).

**Acceptance**
- Each biome JSON compiles via datagen and matches design doc specs.
- Includes ambience colours, precipitation flags, temperature categories.
- `./gradlew :forge:runData` and `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author biome JSONs + update references
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
