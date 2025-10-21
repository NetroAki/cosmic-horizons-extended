# T-032 Arrakis Block Set

**Goal**

- Implement Arrakis blocks (arrakite sandstone variants, spice node, crystalline salt, ash stone, dune glass, sietch structures).

**Scope**

- Block registrations, assets, loot/recipes.

**Acceptance**

- Blocks registered with textures/models; recipes align to design.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
