# T-213 Launch Validation Enhancements

**Goal**
- Extend launch validation to consider cargo mass multipliers, fuel quality, and mission destination restrictions.

**Scope**
- LaunchHooks, FuelRegistry interactions, potential UI feedback.

**Acceptance**
- Launch checks account for cargo/fuel/destination; clear feedback messages.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement launch validation upgrades
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_launch_validation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
