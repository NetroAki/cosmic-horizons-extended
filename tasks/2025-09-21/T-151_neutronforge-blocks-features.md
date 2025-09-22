# T-151 Neutron Star Forge Blocks & Features

**Goal**
- Implement Neutron Star Forge block families (Accretion Rock, Plasma Glass, Magnetite Alloy Blocks, Charge Crystals, Forge Alloy Blocks, Furnace Nodes, Neutron Stone, Strangelet Blocks, Radiation Alloy, Shield Panels) and related features.

**Scope**
- Block registrations with emissive/animated materials, high-resistance properties, and radiation shielding tags.
- Configured/placed features for plasma ejection vents, magnetic storms, forge platforms, gravity well effects, radiation shelter interiors.
- Loot/recipe integration for new blocks aligning with GT processing chains.

**Acceptance**
- Blocks and features appear in correct biomes with intended behaviour (plasma glow, magnetic sparks, gravity well visuals).
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/features + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutronforge_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
