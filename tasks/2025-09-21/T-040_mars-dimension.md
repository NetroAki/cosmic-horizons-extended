# T-040 Mars Dimension

**Goal**
- Build `data/.../dimension/mars.json` with red planet environment, dust storms, and Martian atmosphere.

**Scope**
- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/mars.json`
- Supporting noise settings / dimension type references if needed.

**Acceptance**
- Dimension loads in dev environment with specified sky/physics effects.
- Document configuration choices in `notes/planet_summary/mars.md`.
- `./gradlew check` passes (runData if assets generated).

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension JSON + supporting files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step40_mars_dimension.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
