# T-344 The Cosmic Forge Fauna Implementation

**Goal**

- Implement diverse fauna for The Cosmic Forge with creation, crafting, and mastery-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Forge Masters: Entities that master all crafting (drop Master's Blessing)
- Creation Spirits: Spirits that provide infinite materials (drop Spirit Essence)
- Essence Guardians: Guardians protecting material essence (drop Guardian Cores)
- Crafting Dragons: Dragons that hoard crafting knowledge (drop Dragon Scales)
- Master Craftsmen: Craftsmen that create perfect items (drop Craftsman's Tools)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Forge Masters
- [ ] Create Creation Spirits
- [ ] Add Essence Guardians
- [ ] Implement Crafting Dragons
- [ ] Create Master Craftsmen
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
