# T-153 Forge Star Sovereign Boss Encounter

**Goal**
- Implement the Forge Star Sovereign boss (Magnetic Phase → Plasma Phase → Collapse Phase) with molten arena, magnetic disarm mechanics, and rewards (Sovereign Heart, Neutron Edge, Forge Matrix Core).

**Scope**
- Boss entity behaviour: weapon disarm, plasma beam volleys, gravitational collapse events.
- Forge platform arena structure with molten floor transitions and collapse scripting.
- Loot/progression integration linking drops to GT neutronium/exotic reactor unlocks.

**Acceptance**
- Encounter runs through all phases with magnetic/plasma/gravity mechanics functioning; rewards applied to progression configs.
- `./gradlew :forge:runData` (if structures) + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics/arena/loot
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutronforge_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
