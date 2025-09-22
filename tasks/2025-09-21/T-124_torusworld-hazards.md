# T-124 Torus World Hazards & Progression Hooks

**Goal**
- Implement Torus World systemic hazards: regional gravity shifts, structural collapse traps in the spine, periodic radiation pulses in Radiant Fields, and null-g navigation aids; document GT progression hooks (Forest/Desert/Spine/Radiant/Exotic cores, Torus Core).

**Scope**
- Hazard controllers tied to biome regions adjusting gravity, damage, and movement.
- Radiation pulse scheduler, debris fall logic, and null-g movement toggles.
- Progression config updates enumerating required cores/hearts for GT unlock matrix.

**Acceptance**
- Hazards trigger in appropriate zones with configurable intensity; suits/tech gating enforced; progression docs updated.
- `./gradlew check` passes; notes recorded for balancing.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazard/progression systems
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torusworld_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
