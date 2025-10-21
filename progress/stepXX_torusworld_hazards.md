# Torus World Hazards & Progression Hooks (T-124)

## Summary

- Implemented biome-driven Torus hazards handler applying gravity shifts, structural spine debris chips, periodic radiation pulses in Radiant Fields, and null-G levitation assists in Hubs.
- Effects are lightweight, configurable by tick cadence and effect durations, and align with the current Torus biome set.

## Files Added

- `common/src/main/java/com/netroaki/chex/world/torus/TorusworldHazards.java`

## Behaviour

- Inner Rim Forest: grants brief Slow Falling when airborne to simulate reduced gravity arcs.
- Outer Rim Desert: applies brief Slowness to suggest higher gravity drag.
- Structural Spine: periodic Mining Fatigue; occasional chip damage to simulate debris.
- Radiant Fields: scheduled radiation pulse damage and temporary Glowing.
- Null-G Hubs: short Levitation to aid zero-G navigation.

## Notes

- Future tuning: suit/tech mitigation tiers, config exposure, and arena hazard sync.
- Progression doc hooks can enumerate Torus Core and zone cores for GT gating.
