# T-214 Pandora Flora Generation

**Goal**

- Implement unique flora generation for Pandora with bioluminescent and crystalline features.

**Scope**

- `forge/src/main/java/com/netroaki/chex/worldgen/features` package
- JSON definitions under `data/cosmic_horizons_extended/worldgen/configured_feature/` and `.../placed_feature/`
- Flora generation systems and particle effects

**Acceptance**

- Bioluminescent Fungal Towers: trunk uses "Mossy Pandorite Bark", wide flat caps with glowing cyan/purple mushroom blocks
- Skybark Trees: twisted trunks with cyan-cracked bark, crystalline leaf clusters (stained glass effect)
- Coral-trees: branching crystalline coral with rhythmic glow (light level 7-12 pulse)
- Magma Spires: tall black crystal columns with glowing fissures, emit ember particles
- Featherleaf Trees: thin white trunks, translucent feather-shaped leaves shimmering pink/cyan
- Cloud Flowers: spherical blooms glowing faintly in fog
- `./gradlew check` passes

**Checklist**

- [ ] `bash scripts/cloud_bootstrap.sh`
- [ ] Implement Bioluminescent Fungal Towers
- [ ] Create Skybark Trees with crystalline effects
- [ ] Add Coral-trees with rhythmic glow
- [ ] Implement Magma Spires with ember particles
- [ ] Create Featherleaf Trees with translucent leaves
- [ ] Add Cloud Flowers with fog glow
- [ ] `./gradlew :forge:runData`
- [ ] `./gradlew check`
- [ ] Log entry `progress/stepXX_pandora_flora_generation.md` + `PROGRESS_PROMPTS.md`
- [ ] Open PR referencing this task file
