# T-218 Pandora Environmental Hazards

**Goal**

- Implement environmental hazard systems for Pandora with spore, levitation, and heat-based mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Particle effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Spore blindness: Dense spore clouds can reduce visibility and cause temporary blindness
- Levitation updrafts: Strong upward air currents can carry players unexpectedly
- Heat aura: Areas near volcanic biomes cause gradual heat damage
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement spore blindness mechanics
- [ ] Create levitation updraft systems
- [ ] Add heat aura damage zones
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
