# Hollow World Blocks & Features (T-131)

## Summary

- Registered Hollow World block set (hollowstone, biolume moss, voidstone, void crystal, crystal bark, prism stone, stalactite, riverbed stone) and flora (glowshroom, spore reeds).
- Authored assets (blockstates/models) for all new blocks and flora.
- Implemented worldgen features and biome modifiers to populate Hollow biomes:
  - Glowshroom patches in Bioluminescent Caverns
  - Void crystal clusters in Void Chasms
  - Stalactite clusters in Stalactite Forest
  - Spore reeds along Subterranean Rivers

## Files Added (highlights)

- Blocks: `common/src/main/java/com/netroaki/chex/block/ModBlocks.java`
- Assets: `common/src/main/resources/assets/cosmic_horizons_extended/blockstates|models/block/*.json`
- Configured features: `.../worldgen/configured_feature/`
- Placed features: `.../worldgen/placed_feature/`
- Biome modifiers: `.../data/cosmic_horizons_extended/forge/biome_modifier/`

## Notes

- Lighting levels on biolume and void crystals are placeholders; can be tuned during balancing.
- Additional flora/feature diversity can be layered in follow-up tasks.
