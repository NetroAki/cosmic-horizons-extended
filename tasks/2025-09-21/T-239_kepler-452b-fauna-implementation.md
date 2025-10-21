# T-239 Kepler-452b Fauna Implementation

**Goal**

- Implement diverse fauna for Kepler-452b with alien and luminescent creatures.

**Scope**

- Entity classes under `forge/src/main/java/com/netroaki/chex/entities/`
- Registrations, spawn rules, models/textures
- Loot tables and drop integration

**Acceptance**

- River Grazers: large herbivores adapted to river valleys
- Meadow Flutterwings: small flying creatures with luminescent wings
- Scrub Stalkers: predators adapted to rocky terrain
- Crystal Beetles: small creatures that feed on crystal formations
- Valley Runners: fast-moving creatures adapted to open spaces
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement River Grazers
- [ ] Create Meadow Flutterwings with luminescent wings
- [ ] Add Scrub Stalkers
- [ ] Implement Crystal Beetles
- [ ] Create Valley Runners
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_fauna_implementation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
