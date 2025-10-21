# T-369 Void Material Processing Chain

**Goal**

- Create void material processing chain for Hollow World with GTCEu-compatible chemical processing and void material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Void material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Void material processing recipes implemented
- Chemical processing chain for void materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create void material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add void material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_void_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
