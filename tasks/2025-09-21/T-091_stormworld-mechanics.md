# T-091 Stormworld Mechanics

**Goal**

- Implement Stormworld systemic mechanics: layer-based gravity scaling (1.5g surface, near-zero in vortices), omnipresent lightning storms with shield checks, hunger drain acceleration, and metallic-hydrogen crushing pressure requiring advanced suits.

**Scope**

- Hazard/mechanic systems tied to player tier/suit resistances (electric, pressure, gravity).
- Weather + lightning scheduler, wind push forces, hunger drain modifiers, and depth pressure calculations.
- Telemetry/logging hooks for tuning.

**Acceptance**

- Mechanics activate per layer with configurable intensities and respect suit tiers; hunger/pressure/electric effects stack correctly.
- Logging/telemetry available for balancing; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement mechanics + tests/notes
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_stormworld_mechanics.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
