# T-370 Stellar Debris Processing Chain

**Goal**

- Add stellar debris processing chain for Dyson Swarm with GTCEu-compatible chemical processing and stellar debris material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Stellar debris processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Stellar debris processing recipes implemented
- Chemical processing chain for stellar debris materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create stellar debris processing recipes
- [ ] Implement chemical processing chain
- [ ] Add stellar debris material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stellar_debris_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
