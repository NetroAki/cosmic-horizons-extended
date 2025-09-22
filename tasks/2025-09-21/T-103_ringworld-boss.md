# T-103 Ringworld Boss Encounter

**Goal**
- Implement the Guardian Prime multi-stage boss (Defense Protocol → Siege Mode → Fail-safe Override) in the central control hub with Prime Core/Guardian’s Blade/Nano Circuit Core rewards unlocking nanomaterials/robotics.

**Scope**
- Boss entity behaviour: drone summons, rotating shields, plasma beams, platform collapse scripting.
- Central hub arena structure + destructible platform logic.
- Loot/progression ties for Prime Core, Guardian’s Blade, Nano Circuit Core.

**Acceptance**
- All three phases run with proper telegraphs and arena destruction; drone summons/hazards match design doc.
- Rewards integrate into GT progression configs; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss + arena/loot
- [ ] `./gradlew :forge:runData` if necessary
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
