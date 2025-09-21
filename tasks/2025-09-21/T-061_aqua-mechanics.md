# T-061 Aqua Fluid Mechanics

**Goal**
- Implement Aqua Mundus high-pressure zones, thermal gradients, oxygen consumption integration with suit systems.

**Scope**
- Hazard/oxygen systems, config toggles, suit interactions.

**Acceptance**
- Mechanics active in underwater biomes; suit oxygen integration works.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement mechanics + tests/notes
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mechanics.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
