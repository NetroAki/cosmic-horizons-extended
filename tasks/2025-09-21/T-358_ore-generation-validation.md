# T-358 Ore Generation Validation

**Goal**

- Create ore generation validation system for each planet tier to ensure proper mineral distribution and GTCEu integration.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/validation/`
- Ore generation validation system
- Planet tier validation tools

**Acceptance**

- Validation system for each planet tier
- Ore generation accuracy verification
- GTCEu integration validation
- Error reporting and logging
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create ore generation validation system
- [ ] Add planet tier validation
- [ ] Implement accuracy verification
- [ ] Add error reporting
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ore_generation_validation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
