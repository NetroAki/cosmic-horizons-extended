# T-242 Kepler-452b Environmental Hazards

**Goal**

- Implement environmental hazard systems for Kepler-452b with seasonal, crystal, and temperature-based mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Weather effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Seasonal storms: Periodic weather changes that can affect visibility and movement
- Crystal growth: Crystals can grow and block paths unexpectedly
- Temperature variations: Different areas have different temperature requirements
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement seasonal storm mechanics
- [ ] Create crystal growth systems
- [ ] Add temperature variation hazards
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
