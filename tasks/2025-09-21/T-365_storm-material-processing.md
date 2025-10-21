# T-365 Storm Material Processing Chain

**Goal**

- Implement storm material processing chain for Stormworld with GTCEu-compatible chemical processing and storm material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Storm material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Storm material processing recipes implemented
- Chemical processing chain for storm materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create storm material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add storm material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_storm_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
