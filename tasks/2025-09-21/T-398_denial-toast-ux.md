# T-398 Denial/Toast UX

**Goal**

- Implement denial/toast UX (`chex.message.launch.failed.*`, travel success) with localized text and icons for clear player feedback.

**Scope**

- `forge/src/main/java/com/netroaki/chex/client/ux/`
- Denial/toast UX system
- Localized text and icons

**Acceptance**

- Denial/toast UX implemented
- Launch failure messages working
- Travel success messages working
- Localized text and icons
- Clear player feedback
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create denial/toast UX system
- [ ] Implement launch failure messages
- [ ] Add travel success messages
- [ ] Add localized text and icons
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_denial_toast_ux.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
