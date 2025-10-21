# Shattered Dyson Swarm Hazards & Progression (T-144)

## Summary

- Implemented hazard/ambience controller for Dyson Swarm biomes: timed radiation bursts, debris collision nudges, vacuum exposure pulses, and zero-G navigation assist.
- Provides foundation for mitigation hooks (suits/shields/shelters) and future balancing.

## Files Added

- `common/src/main/java/com/netroaki/chex/world/dyson/DysonSwarmHazards.java`

## Behaviour

- Radiation (every 6s): brief poison stand-in + glow particles + tone cue.
- Debris (every 8s): light damage + push + impact SFX/particles.
- Vacuum (every 10s): hunger + slow as exposure cue; skipped for creative/spectator.
- Zero-G assist (every 5s): short Slow Falling to aid traversal.

## Notes

- Tuning constants can be adjusted after playtesting; suit module checks can be integrated when available.
