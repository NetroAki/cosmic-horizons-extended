# T-274 Stormworld Environmental Hazards

**Goal**

- Implement environmental hazard systems for Stormworld with variable gravity, lightning, and pressure mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Atmospheric effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Variable gravity: Different atmospheric layers have different gravity levels
- Lightning strikes: Random lightning that can damage players
- Pressure crush: Deep atmospheric layers can crush players without protection
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement variable gravity mechanics
- [ ] Create lightning strike systems
- [ ] Add pressure crush mechanics
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
