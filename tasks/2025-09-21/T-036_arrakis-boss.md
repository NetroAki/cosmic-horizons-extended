# T-036 Arrakis Boss Encounter

**Goal**
- Build Sand Emperor boss fight (storm-triggered, burrow phases) and reward Sand Core unlocking T4 fuels.

**Scope**
- Boss entity, arena structure, loot tables, progression hooks.

**Acceptance**
- Boss spawns under storm conditions, multi-phase with burrow mechanics.
- Reward item integrates with progression gating.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss mechanics + arena/loot
- [ ] `./gradlew :forge:runData` if structures change
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file