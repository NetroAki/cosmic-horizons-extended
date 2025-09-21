# T-035 Arrakis Fauna

**Goal**
- Implement Arrakis fauna (spice gatherer NPCs, sandworm juvenile, storm hawks) with drops tied to GT chemical chain.

**Scope**
- Entity classes, AI, loot tables, spawn rules.

**Acceptance**
- Entities spawn correctly and drop intended items.
- `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement fauna + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_arrakis_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
