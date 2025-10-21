# T-220 Arrakis Biome Implementation

**Goal**

- Implement 5 unique biomes for Arrakis with desert, underground, polar, and storm environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/arrakis_*.json`
- Biome-specific terrain generation and atmospheric effects
- Desert and underground exploration systems

**Acceptance**

- Great Dunes: Rolling dunes of fine Arrakite Sand (warm gold by day, deep orange at twilight)
- Spice Mines (Underground): Vast cavernous systems beneath dunes, red-brown sandstone with glowing spice lines
- Polar Ice Caps: White-blue tundra ridges with Arrakite stone buried beneath thick frost
- Sietch Strongholds: Underground networks of sandstone halls carved into cliffs
- Stormlands: Shattered canyons and rocky mesas under permanent sandstorms
- Each biome has distinct visual characteristics and environmental effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Great Dunes biome
- [ ] Create Spice Mines underground biome
- [ ] Implement Polar Ice Caps biome
- [ ] Add Sietch Strongholds biome
- [ ] Create Stormlands biome
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
