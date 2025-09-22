# T-094 Stormworld Boss Encounter

**Goal**
- Implement the Stormlord Colossus multi-phase encounter (Thunder → Tempest → Cataclysm) with lightning/tornado mechanics and Stormheart/Thundermaul/Colossus Core rewards unlocking exotic superconductors.

**Scope**
- Boss entity logic (chain lightning, tornado summons, shockwave knockback) with telegraphs and resist checks.
- Vortex arena structure + scripted lightning strike network.
- Loot progression wiring: Stormheart tier unlock, Thundermaul weapon, Colossus Core for HV transformer recipes.

**Acceptance**
- Encounter runs through all three phases with lightning cadence matching design doc.
- Drops correctly awarded and integrated into progression configs; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss + arena/loot
- [ ] `./gradlew :forge:runData` if structures change
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
