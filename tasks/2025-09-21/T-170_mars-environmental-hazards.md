# T-170 Mars Environmental Hazards

**Goal**

- Add dust storms, low gravity effects, and atmospheric pressure hazards to enhance Mars [EXISTING] with challenging environmental conditions.

**Scope**

- Environmental hazard systems for Mars
- Atmospheric pressure and gravity mechanics
- Dust storm weather effects and visibility

**Acceptance**

- Dust storms: Periodic weather events that reduce visibility and cause damage
- Low gravity effects: Reduced gravity mechanics affecting movement and physics
- Atmospheric pressure: Pressure-based hazards requiring specialized equipment
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement dust storm weather effects
- [ ] Create low gravity mechanics and physics
- [ ] Add atmospheric pressure hazard systems
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mars_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
