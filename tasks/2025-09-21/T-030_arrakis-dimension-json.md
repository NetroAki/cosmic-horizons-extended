# T-030 Arrakis Dimension JSON

**Goal**

- Author Arrakis dimension definition with harsh sunlight, sandstorm controller, reduced water.

**Scope**

- `data/cosmic_horizons_extended/dimension/arrakis.json`
- Supporting noise/dimension type files as needed.

**Acceptance**

- Dimension loads with sandstorm control + low water.
- Document decisions in notes/planet_summary/arrakis.md.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement dimension JSON
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_dimension.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
