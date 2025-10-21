# T-231 Alpha Centauri A Fauna Implementation

**Goal**

- Implement diverse fauna for Alpha Centauri A with stellar, plasma, and magnetic-adapted creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- Solar Sprites: small glowing flame-like beings that hover, drop Solar Essences
- Plasma Rays: manta-shaped plasma creatures gliding across the void
- Corona Eels: plasma-serpents swimming in streams, made of segmented glowing coils
- Flarelings: small hostile flame-elementals that dart out of streams
- Magnet Wraiths: humanoid silhouettes of crackling electricity, pull players with magnetic tethers
- Ore Drones: small floating mineral constructs that mine asteroids, attack intruders
- Sunspot Crawlers: creatures adapted to the intense magnetic fields of sunspots
- Solar Engineers: intelligent beings that maintain solar arrays, drop advanced technology
- Flare Sprites: small, fast-moving energy beings that explode on contact
- Plasma Wraiths: larger, more dangerous versions of plasma creatures
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Solar Sprites with hovering behavior
- [ ] Create Plasma Rays with gliding mechanics
- [ ] Add Corona Eels with stream swimming
- [ ] Implement Flarelings with darting attacks
- [ ] Create Magnet Wraiths with magnetic tethers
- [ ] Add Ore Drones with mining behavior
- [ ] Implement Sunspot Crawlers and Solar Engineers
- [ ] Create Flare Sprites and Plasma Wraiths
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_alpha_centauri_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
