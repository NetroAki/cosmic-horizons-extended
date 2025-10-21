# T-361 Stellar Material Processing Chain

**Goal**

- Add stellar material processing chain for Alpha Centauri A with GTCEu-compatible chemical processing and stellar material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Stellar material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Stellar material processing recipes implemented
- Chemical processing chain for stellar materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create stellar material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add stellar material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stellar_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
