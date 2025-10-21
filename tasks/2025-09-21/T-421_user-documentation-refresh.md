# T-421 User Documentation Refresh

**Goal**

- Refresh README with installation steps and configuration overview, create progression ladder documentation (T1-T17), add fallback behavior documentation, create QA matrix for testing, add troubleshooting guide for common issues.

**Scope**

- `README.md`, `docs/user/`
- User documentation refresh
- Installation and configuration guides

**Acceptance**

- README refreshed with installation steps
- Configuration overview provided
- Progression ladder documentation (T1-T17)
- Fallback behavior documentation
- QA matrix for testing
- Troubleshooting guide for common issues
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Refresh README with installation steps
- [ ] Add configuration overview
- [ ] Create progression ladder documentation
- [ ] Add fallback behavior documentation
- [ ] Create QA matrix
- [ ] Add troubleshooting guide
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_user_documentation_refresh.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
