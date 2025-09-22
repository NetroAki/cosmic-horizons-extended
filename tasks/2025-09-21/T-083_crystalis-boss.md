# T-083 Crystalis Boss

**Goal**
- Build the multi-phase Cryo Monarch boss (Shard Storm, Frozen Domain, Glacial Collapse) with minion support and Frozen Heart/Shardblade/Monarch Core rewards for cryogenic progression.

**Scope**
- Boss entity implementation (phase controller, teleporting, ice shard barrages, arena collapse logic).
- Frozen cathedral arena structure/datapack and scripted icicle hazards.
- Loot integration: Frozen Heart tier unlock, Shardblade weapon, Monarch Core for GT recipes.

**Acceptance**
- Boss fight executes all phases with telegraphs, minion summons, terrain destruction matching design doc.
- Rewards distributed correctly and progression hooks documented/tested; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss + arena/loot
- [ ] `./gradlew :forge:runData` if needed
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
