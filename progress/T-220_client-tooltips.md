# T-220 Client Tooltip Providers — Completed

Summary

- Implemented client tooltip handler `forge/src/main/java/com/netroaki/chex/client/TooltipHandler.java`.
  - Suits: show tier and hazard mitigation summary.
  - Fuel buckets: show usage context and baseline T3 volume from `FuelRegistry`.
  - Rocket controller: show quick summary + hint.
- Added localized strings in `forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json`.

Verification

- Tooltips are registered on client Forge bus and keys resolve from `en_us.json`.
- Acceptance satisfied: tooltips present and localized.
