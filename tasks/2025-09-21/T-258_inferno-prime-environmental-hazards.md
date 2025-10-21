# T-258 Inferno Prime Environmental Hazards

**Goal**

- Implement environmental hazard systems for Inferno Prime with heat, lava, and ash-based mechanics.

**Scope**

- Environmental hazard systems and mechanics
- Volcanic effects and visual feedback
- Hazard damage and protection systems

**Acceptance**

- Heat exhaustion: Constant heat causes gradual damage without thermal protection
- Lava pools: Standing in lava causes massive damage
- Ash storms: Periodic visibility reduction and breathing difficulty
- Each hazard requires appropriate countermeasures and equipment
- Integration with suit systems and GTCEu technology
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement heat exhaustion mechanics
- [ ] Create lava pool damage systems
- [ ] Add ash storm effects
- [ ] Design hazard countermeasures and equipment
- [ ] Integrate with suit systems and GTCEu technology
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_environmental_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
