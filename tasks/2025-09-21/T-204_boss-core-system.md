# T-204 Boss Core System

**Goal**

- Implement boss core system for progression gating and boss mechanics across all planets.

**Scope**

- `common/src/main/java/com/netroaki/chex/boss/`
- Boss core items, mechanics, and progression system.

**Acceptance**

- Boss core system works correctly for all planets.
- Document boss core mechanics in `notes/systems/boss_core.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss core system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step204_boss_core.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
