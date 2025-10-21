# T-201 Fallback Ore Processing Recipes

**Goal**

- Add smelting/maceration/chemical recipes for fallback ore blocks mirroring GTCEu chains.

**Scope**

- Data pack recipes under `forge/src/main/resources/data/`
- Any recipe serializer updates.

**Acceptance**

- Recipes exist for each fallback ore block; progression balanced per design.
- `./gradlew :forge:runData` (if used) + `./gradlew check` pass.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Author recipes + serializers if needed
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_fallback_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
