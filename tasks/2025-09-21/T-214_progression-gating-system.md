# T-214 Progression Gating System

**Goal**

- Implement comprehensive progression gating system using boss cores and planet tiers.

**Scope**

- `common/src/main/java/com/netroaki/chex/progression/`
- Progression gating mechanics, boss core requirements, and tier unlocking system.

**Acceptance**

- Progression gating works correctly across all planets.
- Document progression mechanics in `notes/systems/progression_gating.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement progression gating system
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step214_progression_gating.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
