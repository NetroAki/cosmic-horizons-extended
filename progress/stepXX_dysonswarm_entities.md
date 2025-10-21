# Shattered Dyson Swarm Entities (T-142)

## Summary

- Registered Dyson Swarm fauna and mini-boss placeholders (zero-G oriented) using existing scaffolds.
- Added Dyson drop items for photonic/radiation/circuit progression.
- Implemented loot tables for each fauna/construct and wired spawns into the five Dyson biomes.

## Entities Added (IDs)

- Fauna: `solar_moth`, `panel_crawler`, `node_wraith`, `repair_drone`, `scaffold_serpent`, `orb_drone`, `radiation_wraith`, `shadow_hound`, `signal_phantom`, `relay_construct`
- Mini-bosses: `solar_warden`, `node_horror`, `scaffold_titan`, `radiant_abomination`, `signal_overlord`

## Drops & Loot Tables

- Items: `photon_scales`, `panel_shards`, `node_essence`, `circuit_parts`, `scaffold_scales`, `drone_alloy`, `rad_essence`, `hound_fangs`, `signal_dust`, `relay_shards`.
- Loot tables: one per entity under `data/cosmic_horizons_extended/loot_tables/entities/`.

## Spawns (Biome Modifiers)

- `dyson_panel_fields_spawns.json`: solar_moth, panel_crawler
- `dyson_broken_node_clusters_spawns.json`: node_wraith, repair_drone
- `dyson_scaffold_rings_spawns.json`: scaffold_serpent, orb_drone
- `dyson_shadow_wedges_spawns.json`: radiation_wraith, shadow_hound
- `dyson_relay_lattices_spawns.json`: signal_phantom, relay_construct

## Notes

- AI/behaviour for zero-G can be enhanced later; current scaffolding satisfies spawn/drop integration.
