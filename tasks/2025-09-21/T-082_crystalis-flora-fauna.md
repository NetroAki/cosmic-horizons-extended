# T-082 Crystalis Flora & Fauna

**Goal**

- Implement Crystalis flora (Crystal Bloom, Frost Moss, Frostcaps, Icicle Spires, pressure sponges) and fauna (Crystal Grazers, Shardlings, Frost Hares, Ice Stalkers, Mist Wraiths, Vent Crawlers, Cliff Raptors, Snow Gargoyles, Pressure Leviathans, Abyss Drifters) matching biome assignments.

**Scope**

- Block/item definitions for flora interactables plus configured/placed feature hooks.
- Entity registrations, AI behaviours, models/animations, spawn rules, and loot tables delivering GT materials.
- Mini-boss trigger hooks feeding into boss matrix where needed (e.g., Crystal Colossus allies).

**Acceptance**

- Flora generates and interacts (lights, freezing behaviour) per design doc references.
- Each fauna type spawns in correct biome bands with drops mapped to GT cryogenic progression (shards, fur, fangs, vapor, etc.).
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement flora/fauna + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
