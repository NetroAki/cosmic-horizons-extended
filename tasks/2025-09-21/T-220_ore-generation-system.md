# T-220 Ore Generation System

**Goal**

- Implement comprehensive ore generation system for all planets with tier-based distribution.

**Scope**

- `common/src/main/java/com/netroaki/chex/worldgen/ore/`
- Ore generation mechanics, distribution algorithms, and planet-specific ore placement.

**Acceptance**

- Ore generation works correctly across all planets.
- Document ore generation mechanics in `notes/systems/ore_generation.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement ore generation system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step220_ore_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
