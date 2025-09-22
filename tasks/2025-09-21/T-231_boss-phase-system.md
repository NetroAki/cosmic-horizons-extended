# T-231 Boss Phase System

**Goal**

- Implement boss phase system for all bosses with dynamic phase transitions and mechanics.

**Scope**

- `common/src/main/java/com/netroaki/chex/boss/phases/`
- Boss phase mechanics, transition logic, and phase-specific abilities.

**Acceptance**

- Boss phase system works correctly for all bosses.
- Document phase mechanics in `notes/systems/boss_phase_system.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss phase system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step231_boss_phase_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
