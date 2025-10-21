# T-392 Cargo Mass Validation

**Goal**

- Extend launch validation to consider cargo mass multipliers with proper weight calculations and launch restrictions.

**Scope**

- `forge/src/main/java/com/netroaki/chex/launch/validation/`
- Cargo mass validation system
- Weight calculations and restrictions

**Acceptance**

- Cargo mass validation implemented
- Cargo mass multipliers working
- Proper weight calculations
- Launch restrictions enforced
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create cargo mass validation
- [ ] Implement cargo mass multipliers
- [ ] Add weight calculations
- [ ] Enforce launch restrictions
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_cargo_mass_validation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
