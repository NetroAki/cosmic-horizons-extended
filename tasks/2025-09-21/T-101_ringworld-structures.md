# T-101 Ringworld Structures

**Goal**
- Add Ringworld structures: natural zone set pieces (ice field glaciers, dust belt ruins, meadow float pads, shadowed fungal hives) and urban constructs (habitation arcologies, neon towers, sunline reflector strips, maintenance shafts, edge trench fortifications) supporting mini-boss arenas (Ice Warden, Dust Colossus, Meadow Leviathan, Shadow Revenant, Arcology Warlord, Neon Sentinel, Heliolith, Tunnel Overseer, Trench Horror).

**Scope**
- Structure templates/processors for each biome grouping including boss arenas and loot tables.
- Placement integration via structure sets/biome modifiers tuned per zone.
- Hookups for collapsing debris/falling hazard scripts referenced in design doc.

**Acceptance**
- Structures spawn in correct biome strips with rotation/spacing validated; boss arenas trigger encounters as expected.
- `./gradlew :forge:runData` + `./gradlew check` pass; documentation of any TODO FX.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create structures/placement
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_structures.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
