# T-424 Manual Testing Checklist

**Goal**

- Assemble manual QA checklist covering T1-T17 progression, test rocket tiers, suits, and fuels for each planet, verify planet hazards and environmental effects, test GTCEu presence/absence scenarios, validate boss encounters and rewards.

**Scope**

- `docs/testing/`
- Manual testing checklist
- Comprehensive QA testing

**Acceptance**

- Manual QA checklist assembled
- T1-T17 progression testing
- Rocket tiers, suits, and fuels tested
- Planet hazards and environmental effects verified
- GTCEu presence/absence scenarios tested
- Boss encounters and rewards validated
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Assemble manual QA checklist
- [ ] Add T1-T17 progression testing
- [ ] Test rocket tiers, suits, and fuels
- [ ] Verify planet hazards and effects
- [ ] Test GTCEu scenarios
- [ ] Validate boss encounters
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_manual_testing_checklist.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
