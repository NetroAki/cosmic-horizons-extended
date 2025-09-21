# T-070 Inferno Prime Biomes

**Goal**
- Define biomes for Inferno Prime (Lava Seas, Basalt Flats, Obsidian Isles, Ash Wastes, Magma Caverns).

**Scope**
- Biome JSONs, climate settings, ambience.

**Acceptance**
- Biomes match design; datagen succeeds; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
