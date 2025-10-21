# Stormworld Mechanics (T-091)

## Summary

- Implemented layer-based mechanics for Stormworld:
  - Gravity scaling: slowdown in storm bands; slow falling in eye/upper atmosphere (configurable).
  - Lightning storms in Lightning Fields with suit-tier mitigation.
  - Hunger drain acceleration in bands/fields.
  - Crushing pressure in metallic hydrogen depths with suit-tier requirement.

## Files Added/Updated

- common/src/main/java/com/netroaki/chex/config/StormworldMechanicsConfig.java
- common/src/main/java/com/netroaki/chex/config/ModConfig.java (registered new config)
- common/src/main/java/com/netroaki/chex/world/stormworld/StormworldMechanics.java

## Config

- cosmic_horizons_expanded-stormworld_mechanics.toml exposes toggles and tuning knobs for gravity, lightning, hunger acceleration, and pressure.

## Notes

- Mechanics are biome-driven using the new Stormworld biome IDs.
- Suit tier integration via com.netroaki.chex.api.suit.SuitTier.
- Ready for balancing after initial gameplay tests.
