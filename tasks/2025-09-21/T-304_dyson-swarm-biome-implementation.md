# T-304 Shattered Dyson Swarm Biome Implementation

**Goal**

- Implement 5 unique biomes for Shattered Dyson Swarm with orbital debris, energy, and communication environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/dyson_swarm_*.json`
- Biome-specific terrain generation and orbital effects
- Energy and communication exploration systems

**Acceptance**

- Panel Fields: Areas with solar panel debris
- Broken Node Clusters: Areas with broken energy nodes
- Scaffold Rings: Areas with structural scaffolding
- Shadow Wedges: Areas in shadow
- Relay Lattices: Areas with communication relays
- Each biome has distinct visual characteristics and orbital effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Panel Fields biome
- [ ] Create Broken Node Clusters biome
- [ ] Implement Scaffold Rings biome
- [ ] Add Shadow Wedges biome
- [ ] Create Relay Lattices biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dyson_swarm_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
