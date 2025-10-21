# T-223 Sound Events & Ambience

**Goal**

- Create sound events (launch countdown, suit alarm, planet ambience per world) and wire to ambience controllers.

**Scope**

- Sound registrations, resource packs, ambience logic.

**Acceptance**

- Sound events play in correct contexts; assets present; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add sound events + wiring
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_sound_ambience.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
