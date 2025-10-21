# T-228 Alpha Centauri A Biome Implementation

**Goal**

- Implement 5 unique biomes for Alpha Centauri A with stellar, plasma, and magnetic environments.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/alpha_centauri_*.json`
- Biome-specific terrain generation and stellar effects
- Plasma and magnetic field systems

**Acceptance**

- Photosphere Platforms: Floating islands of Solar Rock with glowing golden cracks
- Corona Streams: Vast rivers of glowing plasma flowing across floating platforms
- Magnetosphere Belt: Floating asteroidal rock fields surrounded by aurora-like magnetic arcs
- Sunspot Fields: Dark patches on solar surface with intense magnetic activity
- Solar Arrays: Massive solar panel installations with energy collection systems
- Each biome has distinct visual characteristics and stellar effects
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Design Photosphere Platforms biome
- [ ] Create Corona Streams with plasma rivers
- [ ] Implement Magnetosphere Belt with aurora effects
- [ ] Add Sunspot Fields with magnetic activity
- [ ] Create Solar Arrays with energy collection
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_biome_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
