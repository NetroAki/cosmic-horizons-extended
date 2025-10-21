# T-297 Hollow World Biome Implementation

**Goal**

- Implement 5 unique biomes for Hollow World with cavern, void, and crystal environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/hollow_world_*.json`
- Biome-specific terrain generation and cavern effects
- Void and crystal exploration systems

**Acceptance**

- Bioluminescent Caverns: Caverns with glowing life forms
- Void Chasms: Deep chasms with void properties
- Crystal Groves: Areas with massive crystal formations
- Stalactite Forest: Forest of hanging stalactites
- Subterranean Rivers: Underground river systems
- Each biome has distinct visual characteristics and cavern effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [x] Design Bioluminescent Caverns biome
  - Glowing mushroom forests with bioluminescent plants
  - Soft blue and green lighting
  - Unique ambient particles and sounds
- [x] Create Void Chasms biome
  - Deep chasms with void fog
  - Floating islands and bridges
  - Void crystal formations
- [x] Implement Crystal Groves biome
  - Large crystal formations in various colors
  - Reflective surfaces and light refraction
  - Crystal-based flora
- [x] Add Stalactite Forest biome
  - Massive hanging stalactites and stalagmites
  - Dense fungal growth
  - Underground water features
- [x] Create Subterranean Rivers biome
  - Glowing river systems
  - Waterfall caves
  - Unique aquatic flora and fauna
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_hollow_world_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
