# T-241 Manual QA Checklist

**Goal**
- Assemble manual QA checklist covering T1–T17 progression (rocket tiers, suits, fuels, planet hazards, GTCEu presence/absence).

**Scope**
- Documentation in docs/ or QA folder.

**Acceptance**
- Checklist covers all tiers/states; ready for testers; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create QA checklist doc
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_manual_qa.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
