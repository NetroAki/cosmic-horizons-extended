# Stormworld Boss Encounter (T-094)

## Summary

- Implemented the Stormlord Colossus boss scaffolding with a multi-phase cadence:
  - Phase 1 (Thunder): lightning burst telegraph (sound + electric spark particles).
  - Phase 2 (Tempest): shockwave knockback.
  - Phase 3 (Cataclysm): stronger knockback + heavy cloud visual.
- Registered the boss entity and created initial loot table with key drops (Stormheart, Thundermaul, Colossus Core).

## Files Added/Updated

- common/src/main/java/com/netroaki/chex/entity/stormworld/boss/StormlordColossusEntity.java
- common/src/main/java/com/netroaki/chex/entity/ModEntities.java (registered stormlord_colossus)
- common/src/main/resources/data/cosmic_horizons_extended/loot_tables/entities/stormlord_colossus.json
- common/src/main/java/com/netroaki/chex/item/ModItems.java (registered Stormheart, Thundermaul, Colossus Core)

## Notes

- This is encounter scaffolding ready for iteration (tornado summons, chain lightning, scripted arena lightning network).
- Rewards are wired as items and loot. Recipe/progression integration can follow design doc in a later step.
