# T-234 Alpha Centauri A Environmental Hazards

**Goal**

- Implement environmental hazard systems for Alpha Centauri A with radiation, solar flare, and life support mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Radiation and solar flare effects
- Life support and protection systems

**Acceptance**

- Radiation fields: Constant radiation damage without proper shielding
- Solar flares: Random intense heat and light that can overwhelm systems
- Zero oxygen: Requires life support systems to survive
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement radiation field mechanics
- [ ] Create solar flare event systems
- [ ] Add zero oxygen life support requirements
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
