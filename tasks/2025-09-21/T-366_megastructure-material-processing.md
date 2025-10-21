# T-366 Megastructure Material Processing Chain

**Goal**

- Create megastructure material processing chain for Ringworld with GTCEu-compatible chemical processing and megastructure material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Megastructure material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Megastructure material processing recipes implemented
- Chemical processing chain for megastructure materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create megastructure material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add megastructure material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_megastructure_material_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
