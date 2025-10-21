# T-412 Skybox/Visual Filters

**Goal**

- Develop skyboxes/visual filters (sandstorms, aurora, void shimmer, solar glare) toggleable via config for immersive planet environments.

**Scope**

- `forge/src/main/java/com/netroaki/chex/client/visuals/`
- Skybox/visual filter system
- Planet-specific visual effects

**Acceptance**

- Skybox/visual filters implemented
- Sandstorms, aurora, void shimmer, solar glare
- Toggleable via config
- Immersive planet environments
- Visual integration working
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create skybox/visual filter system
- [ ] Implement sandstorms and aurora
- [ ] Add void shimmer and solar glare
- [ ] Make toggleable via config
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_skybox_visual_filters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
