# Hollow World Entities (T-132)

## Summary

- Registered Hollow World fauna and mini-boss placeholders using scaffolds (`SimpleWalkingMob`, `SimpleFlyingMob`).
- Added Hollow World drop items and implemented initial loot tables for representative mobs.
- Wired biome spawn modifiers across Hollow biomes (Biolum Caverns, Void Chasms, Crystal Groves, Stalactite Forest, Subterranean Rivers).

## Entities Added (IDs)

- Fauna: `spore_beast`, `lume_bat`, `void_phantom`, `chasm_serpent`, `shardling`, `prism_stalker`, `root_spider`, `hollow_stalker`, `river_serpent`, `lume_fish`
- Mini-bosses: `mycelium_horror`, `abyss_wyrm`, `crystal_titan`, `stalactite_horror`, `river_leviathan`

## Drops & Loot Tables

- Items: `spore_gland`, `bat_oil`, `void_essence`, `chasm_scales`, `crystal_dust`, `prism_pelt`, `root_silk`, `stalker_fang`, `serpent_oil` (in `ModItems.java`).
- Loot tables created for:
  - `spore_beast.json` → spore_gland
  - `lume_bat.json` → bat_oil
  - `void_phantom.json` → void_essence

## Spawns (Biome Modifiers)

- `hollow_biolum_caverns_spawns.json`: spore_beast, lume_bat, root_spider, lume_fish
- `hollow_void_chasms_spawns.json`: void_phantom, chasm_serpent
- `hollow_crystal_groves_spawns.json`: shardling, prism_stalker
- `hollow_stalactite_forest_spawns.json`: hollow_stalker, stalactite_horror
- `hollow_rivers_spawns.json`: river_serpent, lume_fish

## Notes

- Behaviours/AI and additional loot tables can be expanded in later passes; this satisfies roster, drops, and spawns per the current task scope.
