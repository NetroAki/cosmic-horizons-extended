# T-200 Mineral Config Alignment

**Goal**

- Align `chex-minerals.json5` with planet biome ore tables and create validation scripts for tag/block IDs.

**Scope**

- `common/src/main/java/com/netroaki/chex/config/MineralsConfigCore.java`
- `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5`
- Optional helper validation scripts.

**Acceptance**

- Mineral config matches design docs for all planets.
- Validation step reports missing/invalid tags.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Update config + parser
- [ ] Implement validation script/tests as needed
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_mineral_alignment.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
