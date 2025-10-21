# T-278 Ringworld Megastructure Flora Generation

**Goal**

- Implement unique flora generation for Ringworld Megastructure with artificial, maintenance, and habitat-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and megastructure effects

**Acceptance**

- Artificial Gardens: carefully maintained plant life in habitation zones
- Maintenance Vines: plants that grow along maintenance structures
- Habitat Trees: trees adapted to artificial environments
- System Algae: microscopic life that helps maintain air quality
- Garden Flowers: decorative plants that enhance living spaces
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Artificial Gardens with maintenance
- [ ] Create Maintenance Vines with structural growth
- [ ] Add Habitat Trees with artificial adaptation
- [ ] Implement System Algae with air quality maintenance
- [ ] Create Garden Flowers with decorative enhancement
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
