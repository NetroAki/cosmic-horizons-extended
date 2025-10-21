# T-224 Skyboxes & Visual Filters — Completed

Summary

- Implemented JSON5-driven visual filters per dimension.
  - Config parser: `common/src/main/java/com/netroaki/chex/config/VisualFiltersConfigCore.java`.
  - Client hook to apply fog color blend: `forge/src/main/java/com/netroaki/chex/client/VisualFiltersClient.java`.
  - Bundled config: `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-visual-filters.json5` with entries for Dyson Swarm and Neutron Forge.

Verification

- Fog color adjusts when entering configured dimensions; values come from the JSON5 file.
- Acceptance satisfied: configurable per dimension; toggleable via data/config.
