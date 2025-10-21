# T-064 Aqua Boss Encounter

**Goal**

- Build Ocean Sovereign boss fight (multi-head eel with sonic/whirlpool attacks) and Ocean Core reward.

**Scope**

- Boss entity, arena, loot progression.

**Acceptance**

- Boss fight playable with stated mechanics; reward unlocks platinum/iridium/palladium chain.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss + loot
- [ ] `./gradlew :forge:runData` if necessary
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
