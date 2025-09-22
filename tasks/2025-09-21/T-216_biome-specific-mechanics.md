# T-216 Biome-Specific Mechanics

**Goal**

- Implement biome-specific mechanics for each planet with unique environmental effects.

**Scope**

- `common/src/main/java/com/netroaki/chex/biomes/mechanics/`
- Biome-specific mechanics, environmental effects, and planet-unique systems.

**Acceptance**

- Biome-specific mechanics work correctly across all planets.
- Document biome mechanics in `notes/systems/biome_specific_mechanics.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement biome-specific mechanics system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step216_biome_specific_mechanics.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
