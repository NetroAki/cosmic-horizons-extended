# T-220 Client Tooltip Providers

**Goal**
- Add client tooltips summarizing rocket tier, required fuel, destination hazards, suit needs.

**Scope**
- Client tooltip manager, lang entries, asset hooks.

**Acceptance**
- Tooltips appear on relevant items/GUI; localized strings added.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement tooltip providers + lang
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_client_tooltips.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
