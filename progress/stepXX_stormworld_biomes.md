# Stormworld Layers & Biomes (T-090)

## Summary

- Authored 5 biomes and a dimension JSON for Stormworld layered generation:
  - `storm_upper_atmosphere`
  - `storm_storm_bands`
  - `storm_lightning_fields`
  - `storm_eye`
  - `storm_metallic_hydrogen_depths`
- Added `worldgen/dimension/stormworld.json` using a `multi_noise` biome source with distinct parameters per layer.

## Files Created

- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/storm_upper_atmosphere.json`
- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/storm_storm_bands.json`
- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/storm_lightning_fields.json`
- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/storm_eye.json`
- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/storm_metallic_hydrogen_depths.json`
- `common/src/main/resources/data/cosmic_horizons_extended/worldgen/dimension/stormworld.json`

## Notes

- Biome definitions are minimal placeholders suitable for data generation and iteration.
- Dimension `type` key references `cosmic_horizons_extended:stormworld` (to be implemented/registered separately if required by runtime).

## Next Steps (Optional)

- Add ambient particles/sounds and feature lists per biome.
- Register a proper dimension type and hooks if needed for runtime.
