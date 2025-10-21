# T-212 Suit Hazard System

**Goal**

- Implement configurable suit hazard system (vacuum, thermal, radiation, corrosive, high pressure) with per-planet damage/mitigation.

**Scope**

- Hazard manager, config files, suit capability integration.

**Acceptance**

- Hazards configurable via JSON5; suits mitigate according to tier.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard system + configs
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_suit_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
