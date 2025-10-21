# T-250 Aqua Mundus Environmental Hazards

**Goal**

- Implement environmental hazard systems for Aqua Mundus with pressure, oxygen, and current-based mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Underwater effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- High pressure zones: Deep ocean areas require pressure suits to avoid damage
- Oxygen consumption: Underwater breathing mechanics with limited air supply
- Strong currents: Water currents can carry players unexpectedly
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement high pressure zone mechanics
- [ ] Create oxygen consumption systems
- [ ] Add strong current mechanics
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_mundus_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
