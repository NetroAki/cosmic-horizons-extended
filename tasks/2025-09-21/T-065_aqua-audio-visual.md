# T-065 Aqua Audio & Visuals

**Goal**

- Implement Aqua Mundus audio/visual polish (bioluminescent shimmer, deep ambience, volumetric fog).

**Scope**

- Sound events, particle/visual effects, shaders/config if needed.

**Acceptance**

- Effects active in Aqua biomes; performance acceptable.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add audio/visual assets + hooks
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_audio.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
