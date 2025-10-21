# T-034 Arrakis Flora

**Goal**

- Implement Arrakis flora (spice cactus, ice reeds, desert shrubs) with growth logic and harvest drops.

**Scope**

- Plant block classes/assets, loot/recipes.

**Acceptance**

- Flora grows/harvests according to design; drops feed GT chain.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement flora blocks + behaviours
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_flora.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
