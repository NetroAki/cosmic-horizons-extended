# T-354 Ore Distribution Alignment

**Goal**

- Align `chex-minerals.json5` with per-biome ore tables from planet designs for accurate mineral distribution across all planets.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-minerals.json5`
- Planet-specific ore distribution configurations
- Biome-specific mineral generation systems

**Acceptance**

- All planet biomes have accurate ore distribution tables
- Mineral generation matches planet design specifications
- GTCEu integration works correctly with ore distributions
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Review all planet design ore specifications
- [ ] Update chex-minerals.json5 with per-biome ore tables
- [ ] Validate ore distribution accuracy
- [ ] Test GTCEu integration
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_ore_distribution_alignment.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
