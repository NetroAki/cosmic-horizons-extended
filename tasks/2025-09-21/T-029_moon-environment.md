# T-029 Moon Environment

**Goal**
- Implement lunar environmental effects including low gravity, vacuum mechanics, and space weather.

**Scope**
- `common/src/main/java/com/netroaki/chex/environment/`
- Environmental systems for Moon-specific conditions.

**Acceptance**
- Environmental effects work correctly in Moon dimension.
- Document environment mechanics in `notes/planet_summary/moon.md`.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add environment logic + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step29_moon_environment.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
