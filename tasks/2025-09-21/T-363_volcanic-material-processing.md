# T-363 Volcanic Material Processing Chain

**Goal**

- Create volcanic material processing chain for Inferno Prime with GTCEu-compatible chemical processing and volcanic material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Volcanic material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Volcanic material processing recipes implemented
- Chemical processing chain for volcanic materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create volcanic material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add volcanic material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_volcanic_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
