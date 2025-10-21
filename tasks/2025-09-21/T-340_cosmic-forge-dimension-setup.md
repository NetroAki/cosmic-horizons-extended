# T-340 The Cosmic Forge Dimension Setup

**Goal**

- Create ultimate crafting dimension with creative atmosphere and floating workshops for The Cosmic Forge (Tier 15+ Creation Mastery).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/cosmic_forge.json`
- Creative environment and crafting systems
- Floating workshop mechanics

**Acceptance**

- Ultimate crafting dimension with creative atmosphere
- Variable 0.5g to 1.5g gravity with floating workshops
- Creation storms providing random GT recipe unlocks
- Forge echoes guiding players to perfect techniques
- Crafting crystals storing GT crafting knowledge
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create Cosmic Forge dimension JSON
- [ ] Implement creative atmosphere systems
- [ ] Add floating workshop mechanics
- [ ] Configure creation storm effects
- [ ] Set forge echo guidance
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cosmic_forge_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
