# T-121 Torus World Blocks & Features

**Goal**

- Implement Torus World blocks (Torium Alloy Blocks, Radiant Panels, Structural Lattice, Torium Grass, Forest Alloy Soil, Desert Alloy Sand, Reflective Stone, Spine Alloy, Rotary Plates, Pulse Nodes, Exotic Alloy Blocks, Null Nodes) and associated flora/features (Spiral Trees, Light Moss, Spine Palms, Glow Cacti, gravity vines, floating debris).

**Scope**

- Block/item registrations with metallic/animated materials and emissive textures where required.
- Configured/placed features for flora, floating debris clusters, radiation pulse nodes, and null-g flora.
- Integration with biome modifiers to place engineering set pieces per zone.

**Acceptance**

- Blocks render with correct materials/emission; features populate intended biomes (forest, desert, spine, radiant fields, null hubs).
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks/features + assets
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torusworld_features.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
