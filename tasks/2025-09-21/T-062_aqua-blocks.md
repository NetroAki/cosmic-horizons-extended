# T-062 Aqua Block Set

**Goal**
- Add Aqua Mundus blocks (vent basalt, manganese nodules, luminous kelp fronds, ice shelf slabs).

**Scope**
- Block registrations, assets, loot/recipes.

**Acceptance**
- Blocks registered with placeholder textures if needed; `./gradlew check` passes.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
