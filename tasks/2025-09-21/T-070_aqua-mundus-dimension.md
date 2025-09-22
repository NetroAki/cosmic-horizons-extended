# T-070 Aqua Mundus Dimension

**Goal**

- Build `data/.../dimension/aqua_mundus.json` with underwater environment, ocean currents, and aquatic atmosphere.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/aqua_mundus.json`
- Supporting noise settings / dimension type references if needed.

**Acceptance**

- Dimension loads in dev environment with specified underwater effects.
- Document configuration choices in `notes/planet_summary/aqua_mundus.md`.
- `./gradlew check` passes (runData if assets generated).

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension JSON + supporting files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step70_aqua_mundus_dimension.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
