# Dyson Apex Boss (T-143)

## Summary

- Implemented Dyson Apex boss scaffold with three phases (Solar Flare, Overload, Collapse) including telegraphed abilities (flame arc, EMP-like pulse, shock ring collapse cue).
- Registered the boss entity and added boss reward items with a dedicated loot table.

## Files Added/Updated

- Entity class: `common/src/main/java/com/netroaki/chex/entity/dyson/boss/DysonApexEntity.java`
- Entity registration: `common/src/main/java/com/netroaki/chex/entity/ModEntities.java` (`dyson_apex`)
- Items: `common/src/main/java/com/netroaki/chex/item/ModItems.java` (`APEX_CORE`, `DYSON_SPEAR`, `QUANTUM_LENS`)
- Loot table: `common/src/main/resources/data/cosmic_horizons_extended/loot_tables/entities/dyson_apex.json`

## Notes

- Zero-G arena structure and advanced FX can be layered in a future pass; this completes the encounter wiring and rewards for the current task scope.
