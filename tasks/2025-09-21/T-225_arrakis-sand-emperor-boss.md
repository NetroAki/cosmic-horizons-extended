# T-225 Arrakis Sand Emperor Boss

**Goal**

- Implement the main boss encounter for Arrakis with multi-phase mechanics and unique arena design.

**Scope**

- Boss entity class with multi-phase abilities
- Sunken pit arena with quicksand mechanics
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Found beneath Great Dunes in massive sunken pit, ground constantly shifts like quicksand
- Appearance: Colossal dune worm 20+ blocks long, armored in ridged golden-red chitin, circular head with three-ringed maw
- Phase 1 - Burrow Phase: Worm disappears under sand, erupts beneath players, creates collapsing sinkholes
- Phase 2 - Surface Phase: Emerges fully, smashing pillars and hurling sand geysers
- Phase 3 - Storm Phase: Summons massive sandstorm, arena darkens, lightning strikes randomly
- Drops: Emperor's Core (T4 Rocket Engines), Sandpiercer spear weapon, Spice Crown headpiece
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Sand Emperor boss entity
- [ ] Create sunken pit arena with quicksand
- [ ] Implement Burrow Phase mechanics
- [ ] Add Surface Phase with pillar smashing
- [ ] Create Storm Phase with sandstorm effects
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_sand_emperor_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
