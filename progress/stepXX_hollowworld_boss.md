# Hollow Tyrant Boss (T-133)

## Summary

- Implemented Hollow Tyrant boss scaffold with three phases (Fungal Wrath, Crystal Dominion, Void Phase) including telegraphed abilities (spore cloud, crystal knock-up cue, inward pull).
- Registered the boss entity and added boss reward items with a dedicated loot table.

## Files Added/Updated

- Entity class: `common/src/main/java/com/netroaki/chex/entity/hollowworld/boss/HollowTyrantEntity.java`
- Entity registration: `common/src/main/java/com/netroaki/chex/entity/ModEntities.java` (`hollow_tyrant`)
- Items: `common/src/main/java/com/netroaki/chex/item/ModItems.java` (`HOLLOW_HEART`, `TYRANT_FANGBLADE`, `TYRANT_CORE`)
- Loot table: `common/src/main/resources/data/cosmic_horizons_extended/loot_tables/entities/hollow_tyrant.json`

## Notes

- Arena structures and advanced FX can be layered in a future pass; this completes encounter wiring and progression rewards for the task scope.
