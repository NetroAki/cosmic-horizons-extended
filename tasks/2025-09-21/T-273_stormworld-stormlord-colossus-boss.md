# T-273 Stormworld Stormlord Colossus Boss

**Goal**

- Implement the main boss encounter for Stormworld with multi-phase storm and atmospheric mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Massive storm chamber arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Massive storm chamber with floating platforms
- Appearance: Colossal entity of storm and lightning
- Phase 1 - Lightning Phase: Uses electrical attacks, creates lightning storms
- Phase 2 - Storm Phase: Creates massive storms, uses wind-based attacks
- Phase 3 - Wind Phase: Controls all wind currents, uses atmospheric attacks
- Drops: Stormheart (exotic superconductors unlock), Storm Blade weapon, Atmospheric Essence (wind tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Stormlord Colossus boss entity
- [ ] Create massive storm chamber arena
- [ ] Implement Lightning Phase mechanics
- [ ] Add Storm Phase with wind attacks
- [ ] Create Wind Phase with atmospheric attacks
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_stormlord_colossus_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
