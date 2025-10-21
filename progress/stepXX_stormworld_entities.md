# Stormworld Entities (T-093)

## Summary

- Added Stormworld entity scaffolding with registered placeholders to enable biome-layered spawns in future iterations:
  - Sky Whale, Cloud Ray, Storm Drake, Gust Wraith, Fulminator, Spark Beetle, Eye Serpent, Calm Spirit, Hydrogen Beast, Pressure Horror.
  - Mini-boss placeholders: Aerial Behemoth, Tempest Serpent, Storm Titan, Cyclone Guardian, Depth Leviathan.
- Base class `SimpleFlyingMob` provides flying navigation, attributes, and basic passive goals for immediate testing and future specialization.

## Files Added/Updated

- common/src/main/java/com/netroaki/chex/entity/stormworld/SimpleFlyingMob.java
- common/src/main/java/com/netroaki/chex/entity/ModEntities.java (registered all Stormworld entities)

## Notes

- Entities are minimal placeholders to wire up data and future AI/abilities (wind push, lightning discharge, pressure resistance).
- Spawn rules, loot tables, models, and animations can be incrementally added per design doc.
