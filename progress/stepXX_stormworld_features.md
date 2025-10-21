# Stormworld Blocks & Features (T-092)

## Summary

- Added initial Stormworld block set and basic assets:
  - stormstone, stormstone_condensed, reforming_cloud, ion_spire, fulgurite_glass, charged_crystal, metallic_hydrogen.
- Implemented `ReformingCloudBlock` with simple spread/dissipation logic to emulate cloud reformation.
- Registered blocks in `common/src/main/java/com/netroaki/chex/block/ModBlocks.java` and added minimal blockstates/models.

## Next Steps (Optional)

- Add worldgen configured/placed features and biome modifiers to spawn:
  - Ion spires in storm bands.
  - Fulgurite glass veins and charged crystals in lightning fields.
  - Metallic hydrogen pools in depths.
  - Cloud fields in upper atmosphere/eye.
- Hook feature density to config for balancing.
- Add interactions (charged crystal bursts, lightning rod/collector behaviors) per design doc.
