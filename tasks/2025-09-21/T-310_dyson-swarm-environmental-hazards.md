# T-310 Shattered Dyson Swarm Environmental Hazards

**Goal**

- Implement environmental hazard systems for Shattered Dyson Swarm with zero-gravity, radiation, and solar flare mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Orbital effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Zero-gravity sections: Some areas have no gravity, requiring special movement
- Radiation bursts: Periodic radiation that can damage players without protection
- Solar flare cycles: Intense solar energy that can overwhelm systems
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement zero-gravity section mechanics
- [ ] Create radiation burst systems
- [ ] Add solar flare cycle effects
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
