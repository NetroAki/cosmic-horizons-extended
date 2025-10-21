# Hollow World Hazards & Ambience (T-134)

## Summary

- Implemented biome-driven Hollow World hazard/ambience handler with darkness flickers in caverns, void pulses in chasms, and mild radiation-like effects along subterranean rivers.

## Files Added

- `common/src/main/java/com/netroaki/chex/world/hollowworld/HollowWorldHazards.java`

## Behaviour

- Bioluminescent Caverns / Crystal Groves / Stalactite Forest: short Night Vision pulses with glow particles to simulate biolum flicker.
- Void Chasms: periodic damage + downward tug, ash particles and heartbeat cue.
- Subterranean Rivers: mild slowness + confusion with bubble sounds/particles.

## Notes

- Intensity/durations can be tuned via constants; suit/tech mitigation hooks can be layered later.
