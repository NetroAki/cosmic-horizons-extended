# T-202 Mineral Config Reload Command

**Goal**
- Implement runtime reload command for mineral distributions without requiring server restart.

**Scope**
- Command additions, config loader adjustments, feedback messages.

**Acceptance**
- `/chex minerals reload` (or similar) reloads configs, reports success/errors.
- Thread-safe, no crashes; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement command + reload wiring
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mineral_reload.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
