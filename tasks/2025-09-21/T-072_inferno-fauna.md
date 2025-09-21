# T-072 Inferno Fauna

**Goal**
- Add inferno wildlife (ash crawlers, fire wraiths, magma hoppers) with drops (cinder chitin, volcanic essence).

**Scope**
- Entity classes, loot/recipes, spawn rules.

**Acceptance**
- Entities spawn in relevant biomes; drops feed progression.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement fauna + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
