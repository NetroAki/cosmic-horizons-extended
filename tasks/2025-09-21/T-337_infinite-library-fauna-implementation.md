# T-337 The Infinite Library Fauna Implementation

**Goal**

- Implement diverse fauna for The Infinite Library with knowledge, wisdom, and scholarly-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Library Spirits: Helpful entities organizing GT knowledge (drop Spirit Essence)
- Wisdom Owls: Wise creatures providing GT optimization advice (drop Owl Feathers)
- Page Butterflies: Creatures carrying GT recipes between machines (drop Page Dust)
- Rune Spiders: Entities weaving GT enhancement runes (drop Rune Silk)
- Crystal Scholars: Entities studying and optimizing GT processes (drop Scholar's Notes)
- Data Dragons: Creatures hoarding GT processing data (drop Dragon Data)
- Garden Scholars: Entities tending wisdom gardens (drop Scholar's Wisdom)
- Wisdom Bees: Creatures pollinating with knowledge (drop Wisdom Honey)
- Cosmic Scribes: Entities writing universe knowledge (drop Scribe's Ink)
- Reality Writers: Creatures writing laws of physics (drop Reality Ink)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Library Spirits
- [ ] Create Wisdom Owls
- [ ] Add Page Butterflies
- [ ] Implement Rune Spiders
- [ ] Create Crystal Scholars
- [ ] Add Data Dragons
- [ ] Implement Garden Scholars
- [ ] Create Wisdom Bees
- [ ] Add Cosmic Scribes and Reality Writers
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_infinite_library_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
