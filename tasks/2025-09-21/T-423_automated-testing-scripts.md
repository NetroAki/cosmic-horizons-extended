# T-423 Automated Testing Scripts

**Goal**

- Script smoke test launching dedicated server, verify discovery logs and missing registries, test config parsing and validation, add automated planet generation tests, implement boss encounter testing.

**Scope**

- `scripts/testing/`
- Automated testing scripts
- Server testing and validation

**Acceptance**

- Smoke test script for dedicated server
- Discovery logs verification
- Missing registries detection
- Config parsing and validation tests
- Automated planet generation tests
- Boss encounter testing
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create smoke test script
- [ ] Add discovery logs verification
- [ ] Add missing registries detection
- [ ] Add config parsing tests
- [ ] Add planet generation tests
- [ ] Add boss encounter tests
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_automated_testing_scripts.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
