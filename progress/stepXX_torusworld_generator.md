# Torus World Generator & Biomes (T-120)

## Summary

- Implemented a minimal toroidal world generator scaffold with CODEC parameters for major/minor radii.
- Authored five Torus World biomes and a dimension JSON that wires them via a multi-noise biome source.
- This establishes the data path for future gravity metadata and wrap logic integration.

## Files Added

- forge/src/main/java/com/netroaki/chex/world/torus/TorusWorldChunkGenerator.java
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_inner_rim_forest.json
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_outer_rim_desert.json
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_structural_spine.json
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_radiant_fields.json
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/torus_null_g_hubs.json
- common/src/main/resources/data/cosmic_horizons_extended/worldgen/dimension/torusworld.json

## Notes

- The generator currently produces flat bands with simple inner/outer rim hints; gravity bands and seamless wrap hooks can be layered in subsequent iterations.
