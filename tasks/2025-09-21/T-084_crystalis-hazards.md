# T-084 Crystalis Hazards

**Goal**

- Implement cryogenic hazards (ambient frostbite debuff, cryo geyser eruptions that encase players, low-traction ice, snow blindness) plus aurora skybox + blizzard FX.

**Scope**

- Hazard systems tied to suit tier checks (frostbite tick, pressure depth damage over time).
- Particle/visual effects for auroras, blizzards, geyser eruptions, frozen overlays.

**Acceptance**

- Hazards activate in appropriate biomes/layers with tunable configs and respect suit mitigation.
- Visual/ambient effects (auroras, blizzards, geyser steam) align with design doc references; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement hazards + visuals
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_hazards.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
