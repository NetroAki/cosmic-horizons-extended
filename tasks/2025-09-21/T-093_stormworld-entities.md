# T-093 Stormworld Entities

**Goal**
- Implement Stormworld fauna suite: Sky Whales, Cloud Rays, Storm Drakes, Gust Wraiths, Fulminators, Spark Beetles, Eye Serpents, Calm Spirits, Hydrogen Beasts, Pressure Horrors plus mini-boss entities (Aerial Behemoth, Tempest Serpent, Storm Titan, Cyclone Guardian, Depth Leviathan).

**Scope**
- Entity classes, AI, animation controllers, loot tables, and assets for each airborne/pressure-adapted creature.
- Layer-specific spawn rules with environmental behaviours (wind push, lightning discharge, pressure resistance).
- Mini-boss ability scripts and integration with boss core drop tables.

**Acceptance**
- Entities exhibit required behaviours (flight paths, lightning discharge, pressure effects) and spawn only within their biome layers.
- Drops align with GT progression items (storm essence, charged horns, hydrogen hearts, etc.); `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
