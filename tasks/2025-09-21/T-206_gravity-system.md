# T-206 Gravity System

**Goal**

- Implement variable gravity system for different planets and environments.

**Scope**

- `common/src/main/java/com/netroaki/chex/gravity/`
- Gravity mechanics, physics modifications, and planet-specific gravity effects.

**Acceptance**

- Gravity system works correctly across all planets.
- Document gravity mechanics in `notes/systems/gravity_system.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement gravity system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step206_gravity_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
