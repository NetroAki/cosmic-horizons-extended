# T-091 Stormworld Mechanics

**Goal**
- Implement Stormworld mechanics (variable gravity, lightning strikes, pressure crush near depths).

**Scope**
- Hazard/mechanic systems, suit checks.

**Acceptance**
- Mechanics activate per layer; respect suit tiers; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement mechanics + tests/notes
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_mechanics.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
