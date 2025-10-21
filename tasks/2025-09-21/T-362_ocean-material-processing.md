# T-362 Ocean Material Processing Chain

**Goal**

- Implement ocean material processing chain for Aqua Mundus with GTCEu-compatible chemical processing and oceanic material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Ocean material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Ocean material processing recipes implemented
- Chemical processing chain for oceanic materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create ocean material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add oceanic material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ocean_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
