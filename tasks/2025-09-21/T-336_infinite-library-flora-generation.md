# T-336 The Infinite Library Flora Generation

**Goal**

- Implement unique flora generation for The Infinite Library with knowledge, wisdom, and scholarly-adapted plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and knowledge effects

**Acceptance**

- Wisdom Trees: Trees that grow knowledge and provide GT insights
- Memory Vines: Vines that store and share GT processing knowledge
- Knowledge Flowers: Flowers that bloom with GT recipe knowledge
- Research Moss: Moss that grows on knowledge and provides insights
- Wisdom Algae: Algae that grows in knowledge pools
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Wisdom Trees with knowledge growth
- [ ] Create Memory Vines with knowledge sharing
- [ ] Add Knowledge Flowers with recipe knowledge
- [ ] Implement Research Moss with insights
- [ ] Create Wisdom Algae with knowledge pools
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_infinite_library_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
