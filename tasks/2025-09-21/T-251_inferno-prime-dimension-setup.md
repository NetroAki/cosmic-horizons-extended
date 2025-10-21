# T-251 Inferno Prime Dimension Setup

**Goal**

- Create volcanic world dimension with extreme heat, heat aura damage zones, and lava rain events for Inferno Prime (Tier 8 volcanic world).

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/dimension/inferno_prime.json`
- Volcanic environment and heat systems
- Lava rain and atmospheric effects

**Acceptance**

- Volcanic world dimension with extreme heat
- Heat aura damage zones
- Lava rain events
- Oppressive red sky atmosphere
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create volcanic world dimension JSON
- [ ] Implement extreme heat systems
- [ ] Add heat aura damage zones
- [ ] Configure lava rain events
- [ ] Set oppressive red sky atmosphere
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_prime_dimension_setup.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
