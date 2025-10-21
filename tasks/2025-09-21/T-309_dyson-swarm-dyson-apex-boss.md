# T-309 Shattered Dyson Swarm Dyson Apex Boss

**Goal**

- Implement the main boss encounter for Shattered Dyson Swarm with multi-phase energy, debris, and signal mechanics.

**Scope**

- Boss entity class with multi-phase abilities
- Central hub arena design
- Loot drops and GTCEu progression integration

**Acceptance**

- Arena: Central hub with energy systems
- Appearance: Massive entity of energy and debris
- Phase 1 - Energy Phase: Uses energy attacks, creates energy storms
- Phase 2 - Debris Phase: Uses orbital debris, creates debris storms
- Phase 3 - Signal Phase: Uses communication signals, creates signal interference
- Drops: Apex Core (stellar power systems unlock), Apex Blade weapon, Dyson Essence (stellar tech)
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Dyson Apex boss entity
- [ ] Create central hub arena
- [ ] Implement Energy Phase mechanics
- [ ] Add Debris Phase with debris storms
- [ ] Create Signal Phase with signal interference
- [ ] Design loot drops and GTCEu integration
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_dyson_apex_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
