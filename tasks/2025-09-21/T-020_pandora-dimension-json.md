# T-020 Pandora Dimension JSON

**Goal**
- Build `data/.../dimension/pandora.json` with twilight gradient, levitation pockets, dense atmosphere tuned for Pandora.

**Scope**
- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/pandora.json`
- Supporting noise settings / dimension type references if needed.

**Acceptance**
- Dimension loads in dev environment with specified sky/physics effects.
- Document configuration choices in `notes/planet_summary/pandora.md`.
- `./gradlew check` passes (runData if assets generated).

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension JSON + supporting files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_dimension.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
