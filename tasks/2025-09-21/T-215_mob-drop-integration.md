# T-215 Mob Drop Integration

**Goal**

- Implement mob drop integration system for GregTech materials and planet-specific resources.

**Scope**

- `common/src/main/java/com/netroaki/chex/mobs/drops/`
- Mob drop mechanics, GregTech material conversion, and planet-specific loot tables.

**Acceptance**

- Mob drop integration works correctly across all planets.
- Document drop mechanics in `notes/systems/mob_drop_integration.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement mob drop integration system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step215_mob_drop_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
