# T-372 Tiered Rocket Fuel System

**Goal**

- Implement tiered rocket fuel system (T1-T17) with progressive fuel requirements and efficiency for each planet tier.

**Scope**

- `forge/src/main/java/com/netroaki/chex/fuel/`
- Rocket fuel system implementation
- Tier-based fuel requirements

**Acceptance**

- Tiered rocket fuel system implemented (T1-T17)
- Progressive fuel requirements for each tier
- Fuel efficiency calculations
- Integration with rocket launch system
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create tiered rocket fuel system
- [ ] Implement fuel requirements for each tier
- [ ] Add fuel efficiency calculations
- [ ] Integrate with rocket launch system
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_tiered_rocket_fuel_system.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
