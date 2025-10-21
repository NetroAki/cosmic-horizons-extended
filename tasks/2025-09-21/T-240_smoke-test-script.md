# T-240 Dedicated Server Smoke Test

**Goal**

- Script smoke test launching dedicated server, verifying discovery logs, registry issues, config parsing.

**Scope**

- Automation script(s), documentation.

**Acceptance**

- Script runs, reports pass/fail; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement smoke test script
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_smoke_test.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
