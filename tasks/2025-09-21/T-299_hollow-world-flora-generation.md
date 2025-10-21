# T-299 Hollow World Flora Generation

**Goal**

- Implement unique flora generation for Hollow World with cavern, void, and crystal-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and cavern effects

**Acceptance**

- Glowstone Fungi: fungi that provide light in dark caverns
- Void Vines: plants that grow in void chasms
- Crystal Flowers: flowers made of crystal formations
- Stalactite Moss: moss that grows on stalactites
- River Algae: algae that grows in subterranean rivers
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Glowstone Fungi with light provision
- [ ] Create Void Vines with void chasm growth
- [ ] Add Crystal Flowers with crystal formation
- [ ] Implement Stalactite Moss with stalactite growth
- [ ] Create River Algae with subterranean river growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
