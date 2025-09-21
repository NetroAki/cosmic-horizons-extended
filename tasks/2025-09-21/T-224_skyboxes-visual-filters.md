# T-224 Skyboxes & Visual Filters

**Goal**
- Develop skyboxes/visual filters (sandstorms, aurora, void shimmer, solar glare) toggleable via config.

**Scope**
- Rendering hooks, config entries, assets.

**Acceptance**
- Skyboxes configurable per dimension; toggles work; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement skyboxes/filters + config
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_skyboxes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
