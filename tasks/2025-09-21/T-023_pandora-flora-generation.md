# T-023 Pandora Flora Generation

**Goal**
- Implement configured/placed features for Pandora flora (fungal towers, skybark trees, kelp forests, magma spires, cloudstone islands).

**Scope**
- `forge/src/main/java/com/netroaki/chex/worldgen/features` package (new classes as needed)
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Hooks in `CHEXWorldGenProvider` / registries.

**Acceptance**
- Features generate in correct biomes with reasonable density.
- No missing registry warnings on world load.
- `./gradlew :forge:runData` + `./gradlew check` pass.

**Checklist**
- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Add feature builders + JSON definitions
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_flora.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
