# T-221 Launch Messaging UX

**Goal**

- Implement denial/toast UX (`chex.message.launch.failed.*`, travel success) with localized text/icons.

**Scope**

- Client messaging, localization, assets.

**Acceptance**

- Launch feedback displays toasts/messages with reasons; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement UX + localization/assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_launch_messaging.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
