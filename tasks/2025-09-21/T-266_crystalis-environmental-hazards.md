# T-266 Crystalis Environmental Hazards

**Goal**

- Implement environmental hazard systems for Crystalis with cold, ice storm, and slippery surface mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Frozen effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Extreme cold: Constant freeze damage without cold protection
- Ice storms: Periodic blizzards with reduced visibility
- Slippery surfaces: Ice-covered areas cause movement difficulties
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement extreme cold mechanics
- [ ] Create ice storm systems
- [ ] Add slippery surface mechanics
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
