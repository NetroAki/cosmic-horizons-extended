# T-350 The Throne of Creation Flora Generation

**Goal**

- Implement unique flora generation for The Throne of Creation with ultimate dimension and creation-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and creation effects

**Acceptance**

- Creation Vines: Vines that grow in creation rifts
- Existence Flowers: Flowers that bloom with existence essence
- Throne Moss: Moss that grows on throne stone
- Void Algae: Algae that grows in void platforms
- Cosmic Seeds: Seeds that grow into universe trees
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Creation Vines with rift growth
- [ ] Create Existence Flowers with essence blooming
- [ ] Add Throne Moss with throne stone growth
- [ ] Implement Void Algae with platform growth
- [ ] Create Cosmic Seeds with universe tree growth
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_throne_of_creation_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
