# T-183 The Creator (Final Boss)

**Goal**

- Implement The Creator as the ultimate final boss with mechanics from all previous bosses.

**Scope**

- `common/src/main/java/com/netroaki/chex/entities/boss/`
- Boss entity, AI, and combat mechanics for The Creator.

**Acceptance**

- Boss spawns correctly and has proper AI behavior.
- Document boss mechanics in `notes/systems/final_boss_system.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics, structures, loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step183_the_creator.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
