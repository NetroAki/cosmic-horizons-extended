# T-270 Stormworld Flora Generation

**Goal**

- Implement unique flora generation for Stormworld with storm, electrical, and atmospheric-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and storm effects

**Acceptance**

- Storm Clouds: living cloud formations that move and change
- Lightning Trees: crystalline formations that conduct electricity
- Wind Vines: plants that grow in high-altitude winds
- Storm Flowers: flowers that bloom during electrical storms
- Atmospheric Algae: microscopic life that floats in the atmosphere
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Storm Clouds with movement
- [ ] Create Lightning Trees with electrical conduction
- [ ] Add Wind Vines with high-altitude growth
- [ ] Implement Storm Flowers with electrical bloom
- [ ] Create Atmospheric Algae
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
