# T-276 Ringworld Megastructure Biome Implementation

**Goal**

- Implement 5 unique biomes for Ringworld Megastructure with natural, urban, and structural environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/ringworld_*.json`
- Biome-specific terrain generation and megastructure effects
- Natural and artificial environment systems

**Acceptance**

- Natural Zones: Meadow, jungle, desert areas
- Urban Zones: Maintenance tunnels, habitation hubs
- Structural Areas: Support beams and infrastructure
- Command Centers: Central control areas
- Maintenance Shafts: Underground maintenance areas
- Each biome has distinct visual characteristics and megastructure effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Natural Zones biome
- [ ] Create Urban Zones biome
- [ ] Implement Structural Areas biome
- [ ] Add Command Centers biome
- [ ] Create Maintenance Shafts biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ringworld_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
