# T-371 Neutron Star Material Processing Chain

**Goal**

- Implement neutron star material processing chain for Neutron Star Forge with GTCEu-compatible chemical processing and neutron star material refinement.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Neutron star material processing recipes
- GTCEu chemical processing integration

**Acceptance**

- Neutron star material processing recipes implemented
- Chemical processing chain for neutron star materials
- GTCEu integration maintained
- Proper material outputs and byproducts
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create neutron star material processing recipes
- [ ] Implement chemical processing chain
- [ ] Add neutron star material refinement
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
