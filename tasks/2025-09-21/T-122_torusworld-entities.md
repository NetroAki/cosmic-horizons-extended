# T-122 Torus World Entities

**Goal**
- Implement Torus World entity roster: Rim Grazers, Light Owls, Sand Striders, Solar Stalkers, Maintenance Drones, Alloy Serpents, Radiant Beasts, Pulse Wraiths, Void Eels, Null Phantoms plus mini-bosses (Forest Guardian, Desert Colossus, Spine Overseer, Luminal Titan, Exotic Horror).

**Scope**
- Entity classes, AI, animation, spawn rules, loot tables delivering GT materials (alloy horns, solar scales, circuit fragments, pulse essence, null essence).
- Mini-boss ability scripting (root entangle, sandstorm, platform destabilization, radiant beams, micro singularities) and arena triggers.
- Null-G movement handling for hubs and gravity-shift behaviours in other zones.

**Acceptance**
- Entities behave per design doc (gravity-aware motion, radiation pulses, drone swarms) and spawn in correct biomes; drops feed GT exotic matter progression.
- `./gradlew check` passes; note outstanding animation/polish tasks in progress log.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_torusworld_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
