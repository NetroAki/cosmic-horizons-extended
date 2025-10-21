# T-375 Fusion Fuel Processing Chains

**Goal**

- Implement fusion fuel processing chains with GTCEu-compatible chemical processing and fusion material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Fusion fuel processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Fusion fuel processing chains implemented
- Chemical processing for fusion materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create fusion fuel processing chains
- [ ] Implement chemical processing
- [ ] Add fusion material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_fusion_fuel_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
