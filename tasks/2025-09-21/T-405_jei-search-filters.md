# T-405 JEI Search Filters

**Goal**

- Implement search filters for planet-specific content with filtering by planet, tier, and material type.

**Scope**

- `forge/src/main/java/com/netroaki/chex/integration/jei/filters/`
- JEI search filter system
- Planet-specific content filtering

**Acceptance**

- Search filters implemented
- Planet filtering working
- Tier filtering working
- Material type filtering working
- JEI integration working
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create search filter system
- [ ] Implement planet filtering
- [ ] Add tier filtering
- [ ] Add material type filtering
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_jei_search_filters.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
