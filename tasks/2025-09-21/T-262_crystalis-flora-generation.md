# T-262 Crystalis Flora Generation

**Goal**

- Implement unique flora generation for Crystalis with crystal, ice, and cold-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and crystal effects

**Acceptance**

- Crystal Spires: stationary crystal formations that grow naturally
- Frost Bloom Shrubs: plant life adapted to extreme cold
- Ice Vines: plants that grow along ice formations
- Crystal Flowers: delicate flowers made of ice crystals
- Frost Moss: moss that grows in extreme cold conditions
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Crystal Spires with natural growth
- [ ] Create Frost Bloom Shrubs
- [ ] Add Ice Vines with ice growth
- [ ] Implement Crystal Flowers
- [ ] Create Frost Moss
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
