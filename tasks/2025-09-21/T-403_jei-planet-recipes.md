# T-403 JEI Planet Recipes

**Goal**

- Add recipes for all planet-specific items and blocks with proper JEI integration and recipe display.

**Scope**

- `forge/src/main/java/com/netroaki/chex/integration/jei/recipes/`
- JEI recipe integration
- Planet-specific item and block recipes

**Acceptance**

- Planet-specific recipes added to JEI
- All items and blocks have recipes
- Proper JEI integration
- Recipe display working correctly
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add planet-specific recipes to JEI
- [ ] Implement item recipes
- [ ] Add block recipes
- [ ] Ensure proper JEI integration
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_jei_planet_recipes.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
