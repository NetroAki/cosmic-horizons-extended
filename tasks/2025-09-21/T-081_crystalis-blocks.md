# T-081 Crystalis Blocks

**Goal**

- Implement the full Crystalis block set: cryostone variants (base/cracked/mossy/permafrost), glacial glass, geyser stone + frozen vent ice, pressure crystals, prism ice spires, and crystal lattice decorative blocks with shimmering visuals.

**Scope**

- Block registrations, blockstate/models/loot/recipes across `common` + `forge` resources.
- Optional shader hooks or render layers for translucent/animated materials (glacial glass, pressure crystals).

**Acceptance**

- All listed block families available in-game with crafting/loot parity; biome palettes reference them.
- Render layers validated for transparency/shine; `./gradlew check` passes.

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement blocks + assets
- [ ] `./gradlew :common:spotlessApply :forge:spotlessApply`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_crystalis_blocks.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
