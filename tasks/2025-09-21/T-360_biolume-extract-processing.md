# T-360 Biolume Extract Processing Chain

**Goal**

- Create biolume extract processing chain for Pandora with GTCEu-compatible chemical processing and bioluminescent material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Biolume extract processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Biolume extract processing recipes implemented
- Chemical processing chain for bioluminescent materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create biolume extract processing recipes
- [ ] Implement chemical processing chain
- [ ] Add bioluminescent material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_biolume_extract_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
