# T-222 JEI Integration

**Goal**
- Register JEI categories (Rocket Assembly, Planet Resources) with recipes, progression hints, planet resource tables.

**Scope**
- JEI plugin classes, gui descriptions, localization.

**Acceptance**
- JEI displays custom categories with correct data.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement JEI categories + data
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_jei_integration.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
