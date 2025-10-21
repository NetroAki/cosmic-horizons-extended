# T-252 Inferno Prime Biome Implementation

**Goal**

- Implement 5 unique biomes for Inferno Prime with volcanic, lava, and ash environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/inferno_prime_*.json`
- Biome-specific terrain generation and volcanic effects
- Lava and ash exploration systems

**Acceptance**

- Lava Seas: Massive lakes of molten rock
- Basalt Flats: Flat areas of cooled lava
- Obsidian Isles: Islands of volcanic glass
- Ash Wastes: Areas covered in volcanic ash
- Magma Caverns: Underground lava systems
- Each biome has distinct visual characteristics and volcanic effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Lava Seas biome
- [ ] Create Basalt Flats biome
- [ ] Implement Obsidian Isles biome
- [ ] Add Ash Wastes biome
- [ ] Create Magma Caverns biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
