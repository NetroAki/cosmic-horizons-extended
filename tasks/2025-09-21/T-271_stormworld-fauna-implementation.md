# T-271 Stormworld Fauna Implementation

**Goal**

- Implement diverse fauna for Stormworld with storm, electrical, and atmospheric-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Tempest Serpents: serpentine creatures that fly through storms
- Storm Titans: massive creatures adapted to storm environments
- Aerial Behemoths: large flying creatures
- Lightning Wraiths: electrical entities
- Wind Spirits: creatures made of wind and storm
- Storm Hawks: birds that ride storm winds
- Electric Eels: creatures that generate and conduct electricity
- Wind Whales: massive creatures that float through the atmosphere
- Storm Spiders: creatures that spin webs in the wind
- Atmospheric Jellyfish: creatures that float in the upper atmosphere
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Tempest Serpents with storm flight
- [ ] Create Storm Titans
- [ ] Add Aerial Behemoths
- [ ] Implement Lightning Wraiths
- [ ] Create Wind Spirits and Storm Hawks
- [ ] Add Electric Eels and Wind Whales
- [ ] Implement Storm Spiders and Atmospheric Jellyfish
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
