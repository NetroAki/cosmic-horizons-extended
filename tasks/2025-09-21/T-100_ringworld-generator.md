# T-100 Ringworld Generator & Wrap Hooks

**Goal**
- Implement strip-based chunk generator for the Ringworld with wrap hooks and biome zoning matching design doc (Natural: Ice Fields, Dust Belts, Low-Gravity Meadows, Shadowed Rims; Urban: Habitation Plains, Arcology Districts, Sunline Boulevard, Maintenance Tunnels, Edge Trenches).

**Scope**
- Custom chunk generator/density functions for toroidal strip layout, including gravity band metadata.
- Wrap hooks ensuring seamless longitude transitions.
- Biome band mapping + data-driven zone assignments feeding natural vs urban groups and GregTech ore layers.

**Acceptance**
- Terrain produces expected alternating strips (visual + debug logging) for each listed zone with correct gravity modifiers.
- Wrap logic validated (player circumnavigation) without seams; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement generator + hooks
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_generator.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
