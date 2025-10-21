# T-027 Pandora GTCEu Integration

**Goal**

- Map Pandora ores to GTCEu (bismuthinite, phosphorite, etc.) and update `chex-minerals.json5` plus fallback data.

**Scope**

- `common/src/main/java/com/netroaki/chex/config/MineralsConfigCore.java`
- `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5`
- Fallback ores integration.

**Acceptance**

- Mineral config enumerates Pandora ore distributions by biome.
- Fallback ores kick in when GTCEu absent.
- `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Update configs + any registry hooks
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_gtceu.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
