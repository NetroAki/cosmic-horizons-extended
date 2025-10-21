# T-368 Gravity Material Processing Chain

**Goal**

- Implement gravity material processing chain for Torus World with GTCEu-compatible chemical processing and gravity material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Gravity material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Gravity material processing recipes implemented
- Chemical processing chain for gravity materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create gravity material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add gravity material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_gravity_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
