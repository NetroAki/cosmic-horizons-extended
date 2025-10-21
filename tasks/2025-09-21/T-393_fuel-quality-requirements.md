# T-393 Fuel Quality Requirements

**Goal**

- Add fuel quality requirements for different destinations with tier-based fuel requirements and quality validation.

**Scope**

- `forge/src/main/java/com/netroaki/chex/launch/fuel/`
- Fuel quality requirements system
- Tier-based fuel validation

**Acceptance**

- Fuel quality requirements implemented
- Different destinations have different requirements
- Tier-based fuel validation working
- Quality validation system
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create fuel quality requirements
- [ ] Add destination-specific requirements
- [ ] Implement tier-based validation
- [ ] Add quality validation system
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_fuel_quality_requirements.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
