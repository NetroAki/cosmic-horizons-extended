# T-260 Crystalis Biome Implementation

**Goal**

- Implement 5 unique biomes for Crystalis with frozen, crystal, and pressure environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/crystalis_*.json`
- Biome-specific terrain generation and frozen effects
- Crystal and pressure exploration systems

**Acceptance**

- Diamond Fields: Areas with massive diamond formations
- Frosted Plains: Flat areas covered in ice crystals
- Cryo Geysers: Geysers that erupt with supercooled liquid
- Ice Cliffs: Massive ice formations
- Pressure Depths: Deep areas with extreme pressure
- Each biome has distinct visual characteristics and frozen effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Diamond Fields biome
- [ ] Create Frosted Plains biome
- [ ] Implement Cryo Geysers biome
- [ ] Add Ice Cliffs biome
- [ ] Create Pressure Depths biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
