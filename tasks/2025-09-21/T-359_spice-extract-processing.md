# T-359 Spice Extract Processing Chain

**Goal**

- Implement spice extract processing chain for Arrakis with GTCEu-compatible chemical processing and material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Spice extract processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Spice extract processing recipes implemented
- Chemical processing chain for spice refinement
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create spice extract processing recipes
- [ ] Implement chemical processing chain
- [ ] Add material refinement steps
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_spice_extract_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
