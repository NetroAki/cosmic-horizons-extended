# Torus World Entities (T-122)

## Summary

- Registered Torus World fauna and mini-boss placeholders using existing scaffolds (`SimpleWalkingMob`, `SimpleFlyingMob`).
- Added Torus-specific drop items and created loot tables mapping entities to GT-aligned materials.
- Wired biome modifiers to spawn entities appropriately across Torus biomes, including Null-G hubs for gravity-themed mobs.

## Entities Added (IDs)

- Rim Grazer (`torus_rim_grazer`), Light Owl (`torus_light_owl`), Sand Strider (`torus_sand_strider`), Solar Stalker (`torus_solar_stalker`)
- Maintenance Drone (`torus_maintenance_drone`), Alloy Serpent (`torus_alloy_serpent`), Radiant Beast (`torus_radiant_beast`)
- Pulse Wraith (`torus_pulse_wraith`), Void Eel (`torus_void_eel`), Null Phantom (`torus_null_phantom`)
- Mini-boss placeholders: Forest Guardian, Desert Colossus, Spine Overseer, Luminal Titan, Exotic Horror

## Drops & Loot Tables

- Items: `alloy_horns`, `solar_scales`, `circuit_fragments`, `pulse_essence`, `null_essence` (in `ModItems.java`).
- Loot tables (under `data/cosmic_horizons_extended/loot_tables/entities/`):
  - `torus_rim_grazer.json` → alloy_horns
  - `torus_solar_stalker.json` → solar_scales
  - `torus_maintenance_drone.json` → circuit_fragments
  - `torus_pulse_wraith.json` → pulse_essence
  - `torus_null_phantom.json` → null_essence
  - `torus_alloy_serpent.json` → alloy_horns

## Spawns (Biome Modifiers)

- `torus_inner_rim_forest_spawns.json`: rim_grazer, light_owl
- `torus_outer_rim_desert_spawns.json`: sand_strider, solar_stalker
- `torus_structural_spine_spawns.json`: maintenance_drone, alloy_serpent
- `torus_radiant_fields_spawns.json`: radiant_beast, light_owl
- `torus_null_g_hubs_spawns.json`: pulse_wraith, null_phantom, void_eel

## Notes

- Behaviours are scaffolded; gravity-aware movement, pulses, and arena triggers can be added subsequently.
- This aligns with T-122 acceptance by providing roster coverage, spawns, and progression drops.
