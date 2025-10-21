# T-154 Neutron Star Forge Hazards & Progression — Completed

Summary

- Implemented Neutron Forge hazard controller with magnetic storm debuffs, plasma burst burns, and gravity well pulls, scoped to Neutron biomes.
- Provides hooks for future mitigation (gear/suits) and balancing.

Files Added

- `common/src/main/java/com/netroaki/chex/world/neutronforge/NeutronForgeHazards.java`

Verification

- Hazards are gated by biome keys (`neutron_*`) and run on timed cadences.
- Behaviour aligns with acceptance: magnetic storms, plasma effects, gravity well force.
