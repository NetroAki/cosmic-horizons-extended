# T-356 Fallback Ore Processing

**Goal**

- Add processing recipes for fallback ore blocks (smelting, maceration, chemical chains) mirroring GTCEu for seamless integration.

**Scope**

- `forge/src/main/resources/data/cosmic_horizons_extended/recipes/`
- Fallback ore processing recipe system
- GTCEu-compatible processing chains

**Acceptance**

- All fallback ores have smelting recipes
- Maceration recipes for ore processing
- Chemical processing chains for advanced materials
- GTCEu compatibility maintained
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Create smelting recipes for fallback ores
- [ ] Add maceration recipes
- [ ] Implement chemical processing chains
- [ ] Ensure GTCEu compatibility
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_fallback_ore_processing.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
