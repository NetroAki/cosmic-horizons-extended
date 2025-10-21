# T-318 Neutron Star Forge Environmental Hazards

**Goal**

- Implement environmental hazard systems for Neutron Star Forge with crushing gravity, magnetic storms, and radiation mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Neutron star effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Crushing gravity pulses: Extreme gravity that can crush players without protection
- Magnetic storms: Intense magnetic fields that can disrupt equipment
- Radiation ticks: Constant radiation damage requiring advanced shielding
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement crushing gravity pulse mechanics
- [ ] Create magnetic storm systems
- [ ] Add radiation tick damage
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_neutron_star_forge_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
