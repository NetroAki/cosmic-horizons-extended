# T-343 The Cosmic Forge Flora Generation

**Goal**

- Implement unique flora generation for The Cosmic Forge with creation, crafting, and mastery-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and creation effects

**Acceptance**

- Forge Vines: Vines that grow crafting materials
- Essence Flowers: Flowers that bloom with material essence
- Creation Trees: Trees that grow perfect crafting materials
- Crafting Moss: Moss that grows on crafting stations
- Master Algae: Algae that grows in essence wells
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Forge Vines with crafting material growth
- [ ] Create Essence Flowers with material essence blooming
- [ ] Add Creation Trees with perfect material growth
- [ ] Implement Crafting Moss with station growth
- [ ] Create Master Algae with essence well growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
