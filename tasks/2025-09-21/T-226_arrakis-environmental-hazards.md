# T-226 Arrakis Environmental Hazards

**Goal**

- Implement environmental hazard systems for Arrakis with heat, sandstorm, and glass-based mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Weather effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Heat exhaustion: Constant heat causes gradual dehydration and damage
- Sandstorm events: Periodic sandstorms reduce visibility and cause slowness
- Glass flats damage: Standing on glass flats without protection causes damage
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement heat exhaustion mechanics
- [ ] Create sandstorm event systems
- [ ] Add glass flats damage mechanics
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
