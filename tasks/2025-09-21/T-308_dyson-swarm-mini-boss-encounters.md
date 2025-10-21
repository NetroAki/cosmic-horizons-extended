# T-308 Shattered Dyson Swarm Mini-Boss Encounters

**Goal**

- Implement 5 mini-boss encounters for Shattered Dyson Swarm with unique orbital debris and energy-based mechanics.

**Scope**

- Boss entity classes with unique abilities
- Boss arena generation and mechanics
- Loot drops and progression integration

**Acceptance**

- Solar Warden (Panel Fields): Colossal moth-like guardian with radiant wings
- Node Horror (Broken Node Clusters): Twisted fusion of machinery and energy
- Scaffold Titan (Scaffold Rings): Gigantic construct built from lattice segments in humanoid shape
- Radiant Abomination (Shadow Wedges): Massive beast mutated by radiation with twisted metal body
- Signal Overlord (Relay Lattices): Gigantic antenna-creature covered in pulsating nodes
- Each mini-boss has unique arena and attack patterns
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Solar Warden boss entity
- [ ] Create Node Horror with machinery fusion
- [ ] Implement Scaffold Titan with lattice construction
- [ ] Add Radiant Abomination with radiation mutation
- [ ] Create Signal Overlord with antenna features
- [ ] Design boss arenas and encounter mechanics
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_mini_boss_encounters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
