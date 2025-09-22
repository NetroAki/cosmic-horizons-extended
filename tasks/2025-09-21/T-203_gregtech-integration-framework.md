# T-203 GregTech Integration Framework

**Goal**

- Create comprehensive GregTech integration system for planet-based progression and resource management.

**Scope**

- `common/src/main/java/com/netroaki/chex/integration/gregtech/`
- Integration classes for GregTech multiblocks, recipes, and progression.

**Acceptance**

- GregTech integration works correctly with planet progression.
- Document integration mechanics in `notes/systems/gregtech_integration.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement GregTech integration framework
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step203_gregtech_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
