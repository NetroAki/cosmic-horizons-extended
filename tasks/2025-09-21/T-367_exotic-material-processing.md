# T-367 Exotic Material Processing Chain

**Goal**

- Add exotic material processing chain for Exotica with GTCEu-compatible chemical processing and exotic material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Exotic material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Exotic material processing recipes implemented
- Chemical processing chain for exotic materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create exotic material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add exotic material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_exotic_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
