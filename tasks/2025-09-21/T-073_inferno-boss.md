# T-073 Inferno Boss Encounter

**Goal**

- Implement Infernal Sovereign multi-phase boss (magma armor, fire rain) dropping Inferno Core for niobium/tantalum/uranium progression.

**Scope**

- Boss entity, arena, loot/progression integration.

**Acceptance**

- Fight playable with multiple phases; reward unlocks next tier.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement boss + arena/loot
- [ ] `./gradlew :forge:runData` if needed
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_inferno_boss.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
