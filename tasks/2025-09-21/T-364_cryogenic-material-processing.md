# T-364 Cryogenic Material Processing Chain

**Goal**

- Add cryogenic material processing chain for Crystalis with GTCEu-compatible chemical processing and cryogenic material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Cryogenic material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Cryogenic material processing recipes implemented
- Chemical processing chain for cryogenic materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create cryogenic material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add cryogenic material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cryogenic_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
