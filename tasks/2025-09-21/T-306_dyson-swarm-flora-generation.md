# T-306 Shattered Dyson Swarm Flora Generation

**Goal**

- Implement unique flora generation for Shattered Dyson Swarm with orbital debris, energy, and communication-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and orbital effects

**Acceptance**

- Solar Algae: microscopic life that feeds on solar energy
- Debris Vines: plants that grow on orbital debris
- Energy Flowers: flowers that bloom with energy
- Signal Moss: moss that grows on communication equipment
- Orbital Lichen: lichen that grows in zero-gravity
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Solar Algae with solar energy feeding
- [ ] Create Debris Vines with orbital debris growth
- [ ] Add Energy Flowers with energy bloom
- [ ] Implement Signal Moss with communication equipment growth
- [ ] Create Orbital Lichen with zero-gravity growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
