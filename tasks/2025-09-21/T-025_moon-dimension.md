# T-025 Moon Dimension

**Goal**
- Build `data/.../dimension/moon.json` with lunar environment, low gravity, and space-based atmosphere for Moon exploration.

**Scope**
- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/moon.json`
- Supporting noise settings / dimension type references if needed.

**Acceptance**
- Dimension loads in dev environment with specified sky/physics effects.
- Document configuration choices in `notes/planet_summary/moon.md`.
- `./gradlew check` passes (runData if assets generated).

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension JSON + supporting files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step25_moon_dimension.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
