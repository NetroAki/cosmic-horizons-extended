TerraBlender Strategy for CHEX

Overview

- Cosmic Horizons (CH) remains authoritative for its own worlds. CHEX does not modify existing CH planet biomes by default.
- CHEX supplies TerraBlender regions as overlays that can be toggled in config later, enabling incremental biome additions without breaking CH defaults.

Principles

- Non-invasive: For `cosmos:`/`cosmichorizons:` dimensions, CHEX does not register surface rules or dimension jsons unless explicitly enabled.
- CHEX-owned worlds: Minimal dimension JSONs are provided only for CHEX planets that need standalone testing. Biome content is placeholder-friendly and safe to disable.
- Single source of truth: Progression and travel gating remain in CHEX (Java + json5), worldgen distribution overlays are handled by TerraBlender regions.

Implementation Plan

1. Common cores (done): travel graph, minerals parsing, themes.
2. TerraBlender regions: register CHEX regions with low priority so native CH remains primary.
3. Overlay toggle: add a config flag (future) `chex.worldgen.enableOverlays`. When false, regions register no parameter points.
4. Surface rules: keep neutral, per-biome rules live behind the overlay flag.
5. Minimal dimensions: only for CHEX checklist/test worlds; CH planets keep their default datapack-driven dimensions.

Testing

- Verify vanilla/CH worlds are unchanged with overlays disabled.
- Enable overlays and confirm CHEX features appear according to region weights.
