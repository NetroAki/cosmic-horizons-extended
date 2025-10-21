# T-238 Kepler-452b Flora Generation

**Goal**

- Implement unique flora generation for Kepler-452b with alien and luminescent plants.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and luminescent effects

**Acceptance**

- Towering Trees: massive trees with braided root systems
- Hanging Moss: vine-like growths with luminescent properties
- Luminescent Grasses: glowing grass variants in meadowlands
- Succulent Plants: hardy desert-like vegetation in scrub areas
- Crystal Shrubs: small crystal formations that grow naturally
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Towering Trees with braided roots
- [ ] Create Hanging Moss with luminescent properties
- [ ] Add Luminescent Grasses
- [ ] Implement Succulent Plants
- [ ] Create Crystal Shrubs
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_kepler_452b_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
