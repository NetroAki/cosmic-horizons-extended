# T-063 Aqua Fauna

**Goal**

- Implement Aqua Mundus entities (luminfish, hydrothermal drones, abyss leviathan, tidal jelly) with underwater AI.

**Scope**

- Entity classes, AI, loot tables, assets.

**Acceptance**

- Entities behave correctly underwater; drops align with progression.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_aqua_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
