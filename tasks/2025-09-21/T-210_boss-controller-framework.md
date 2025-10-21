# T-210 Boss Controller Framework

**Goal**

- Build centralized boss controller managing spawn triggers, multi-phase state, and loot distribution.

**Scope**

- New controller class, integration with existing boss entities, config hooks.

**Acceptance**

- Controller orchestrates boss fights, exposes API for planet bosses.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement controller + integrate with existing/planned bosses
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_boss_controller.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
