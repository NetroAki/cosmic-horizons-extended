# T-373 Suit Power Systems

**Goal**

- Create suit power systems for each planet tier with power consumption, generation, and management for space exploration.

**Scope**

- `forge/src/main/java/com/netroaki/chex/suit/`
- Suit power system implementation
- Planet tier power requirements

**Acceptance**

- Suit power systems for each planet tier
- Power consumption calculations
- Power generation and management
- Integration with suit hazard system
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create suit power systems
- [ ] Implement power consumption calculations
- [ ] Add power generation and management
- [ ] Integrate with suit hazard system
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_suit_power_systems.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
