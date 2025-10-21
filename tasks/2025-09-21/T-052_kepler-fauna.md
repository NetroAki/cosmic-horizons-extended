# T-052 Kepler Fauna

**Goal**

- Add Kepler fauna (river grazers, meadow flutterwings, scrub stalkers) with tiered organic drops for GT.

**Scope**

- Entity classes, loot tables, spawn rules, assets.

**Acceptance**

- Entities spawn in correct biomes; drops align with GT organics chain.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement entities + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_fauna.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
