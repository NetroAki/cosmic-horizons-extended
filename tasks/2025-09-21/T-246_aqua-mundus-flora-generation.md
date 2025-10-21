# T-246 Aqua Mundus Flora Generation

**Goal**

- Implement unique flora generation for Aqua Mundus with underwater, hydrothermal, and bioluminescent plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and underwater effects

**Acceptance**

- Luminous Kelp: tall underwater plants with bioluminescent properties
- Hydrothermal Vents: unique plant life adapted to extreme heat and pressure
- Coral Formations: massive coral structures with unique properties
- Sea Grass: underwater grass that sways with currents
- Bioluminescent Algae: microscopic organisms that create glowing effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Luminous Kelp with bioluminescence
- [ ] Create Hydrothermal Vent plant life
- [ ] Add Coral Formations
- [ ] Implement Sea Grass with current animation
- [ ] Create Bioluminescent Algae
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
