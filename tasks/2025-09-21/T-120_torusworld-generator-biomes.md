# T-120 Torus World Generator & Biomes

**Goal**
- Implement Torus World custom generator with wrap-around toroidal layout and biome suite (Inner Rim Forest, Outer Rim Desert, Structural Spine, Radiant Fields, Null-G Hubs) matching gravity gradients.

**Scope**
- Chunk generator/density functions for toroidal geometry, including inner/outer rim gravity metadata and null-g zones.
- Dimension + biome JSONs with engineered skybox, lighting, and biome ambience per design doc.
- Biome tag setup for GT ore layers and hazard routing.

**Acceptance**
- Terrain produces continuous torus with seamless wrap; biome bands align with intended regions and gravity metadata surfaces to mechanics systems.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement generator + biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torusworld_generator.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
