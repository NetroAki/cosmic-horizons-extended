# Exotica Hazards & Visuals (T-114)

## Summary

- Implemented biome-driven hazard/visual systems for Exotica, including spatial distortion teleports in Quantum Glades, fluctuating gravity effects in Fractal Forest, resonance debuffs in Resonant Dunes, and chromatic haze/particle ambience across Exotica biomes.

## Files Added

- `common/src/main/java/com/netroaki/chex/world/exotica/ExoticaHazards.java`

## Behaviour

- Chroma Steppes/Resonant Dunes/Quantum Glades/Fractal Forest/Prism Canyons: chromatic haze glow particles (ambient layer).
- Quantum Glades: periodic short-range displacement and brief nausea (refraction effect cue).
- Fractal Forest: gravity fluctuation cues alternating brief Slow Falling/Levitation.
- Resonant Dunes: resonance hum with Weakness debuff cadence.
- Prism Canyons: prismatic glow flashes with sparkles.

## Notes

- Future: suit/tech mitigation hooks and config exposure for intensity/durations; shader/skybox polish pass can be layered on top of this scaffold.
