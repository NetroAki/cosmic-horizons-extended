# T-212 Suit Hazard System — Completed

Summary

- Added JSON5-driven suit hazard configuration at `forge/src/main/resources/data/cosmic_horizons_extended/config/chex-suit-hazards.json5`.
- Implemented parsers/runtime holders: `SuitHazardsConfigCore` and `SuitHazardsRuntime`.
- Integrated mitigation into hazard handlers:
  - `DysonSwarmHazards` scales radiation/vacuum effects by suit tier mitigation.
  - `NeutronForgeHazards` scales magnetic(pressure), plasma(thermal), and gravity well effects by suit tier mitigation.

Verification

- Hazards attenuate or skip when mitigation is high; values configurable per planet.
- Acceptance satisfied: JSON5 config controls hazards; suits mitigate by tier.
