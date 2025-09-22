# T-230 Boss Arena Generation

**Goal**

- Implement boss arena generation system for all planets with unique arena designs.

**Scope**

- `common/src/main/java/com/netroaki/chex/worldgen/arena/`
- Arena generation mechanics, structure placement, and boss-specific arena designs.

**Acceptance**

- Boss arenas generate correctly for all planets.
- Document arena generation mechanics in `notes/systems/boss_arena_generation.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss arena generation system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step230_boss_arena_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
