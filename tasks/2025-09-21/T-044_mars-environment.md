# T-044 Mars Environment

**Goal**

- Implement Martian environmental effects including dust storms, low pressure, and red planet weather.

**Scope**

- `common/src/main/java/com/netroaki/chex/environment/`
- Environmental systems for Mars-specific conditions.

**Acceptance**

- Environmental effects work correctly in Mars dimension.
- Document environment mechanics in `notes/planet_summary/mars.md`.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add environment logic + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/step44_mars_environment.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
