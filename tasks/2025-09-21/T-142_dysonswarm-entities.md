# T-142 Shattered Dyson Swarm Entities

**Goal**
- Implement Dyson Swarm fauna/constructs: Solar Moths, Panel Crawlers, Node Wraiths, Repair Drones, Scaffold Serpents, Orb Drones, Radiation Wraiths, Shadow Hounds, Signal Phantoms, Relay Constructs plus mini-bosses (Solar Warden, Node Horror, Scaffold Titan, Radiant Abomination, Signal Overlord).

**Scope**
- Entity classes with zero-G movement/AI, loot tables (photon scales, panel shards, node essence, circuit parts, scaffold scales, drone alloys, rad essence, hound fangs, signal dust, relay shards).
- Mini-boss ability scripting (blinding flashes, EMP pulses, collapsing scaffolds, radiation bursts, HUD overload) and arena triggers.
- Vacuum survival hooks ensuring mobs operate without atmosphere.

**Acceptance**
- Entities spawn/behave per biome (zero-G drift, scaffold traversal, radiation zones) and drops feed GT photonics/radiation/quantum unlocks.
- `./gradlew check` passes; log outstanding animation/FX tasks.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dysonswarm_entities.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
