# T-140 Shattered Dyson Swarm Dimension & Biomes

**Goal**

- Define the Shattered Dyson Swarm zero-G dimension with biome set (Panel Fields, Broken Node Clusters, Scaffold Rings, Shadow Wedges, Relay Lattices) and vacuum conditions.

**Scope**

- Dimension JSON/noise for orbital debris fields, sun-facing lighting, zero-G configuration.
- Biome JSONs with vacuum atmosphere flags, radiation lighting, particle FX, and structure placements per design doc.
- Biome tags for ore distribution and hazard routing.

**Acceptance**

- Datapack loads with zero-G/vacuum behaviour exposed to mechanics; debug worldgen shows each biome variant with correct ambience.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author dimension/biome files
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_dysonswarm_biomes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
