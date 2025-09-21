# T-100 Ringworld Generator & Wrap Hooks

**Goal**
- Implement strip-based chunk generator for ringworld with wrap hooks and natural/urban zone layout.

**Scope**
- Generator classes, wrap hooks, biome mapping.

**Acceptance**
- Ringworld terrain generates with band limits; wrap logic works.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement generator + hooks
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_generator.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
