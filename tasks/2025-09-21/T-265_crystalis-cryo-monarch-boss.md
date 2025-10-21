# T-265 Crystalis Cryo Monarch Boss

**Goal**

- Implement the main boss encounter for Crystalis with multi-phase frozen and blizzard mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Massive ice chamber arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Massive ice chamber with crystal formations
- Appearance: Colossal ice entity with freeze beam attacks
- Phase 1 - Freeze Beam Phase: Uses freeze beam attacks, creates ice barriers
- Phase 2 - Minion Summon Phase: Summons ice minions, creates blizzard effects
- Phase 3 - Blizzard Phase: Creates massive blizzard, uses area freeze attacks
- Drops: Frozen Heart (cryogenics/superconductors unlock), Ice Blade weapon, Cryo Essence (cold tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Cryo Monarch boss entity
- [ ] Create massive ice chamber arena
- [ ] Implement Freeze Beam Phase mechanics
- [ ] Add Minion Summon Phase with blizzard effects
- [ ] Create Blizzard Phase with area freeze attacks
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_cryo_monarch_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
