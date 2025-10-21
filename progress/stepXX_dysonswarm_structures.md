# Shattered Dyson Swarm Blocks & Structures (T-141)

## Summary

- Registered Dyson Swarm block set: dyson_panel, damaged_dyson_panel, node_fragment, circuit_block, scaffold_strut, rotary_node, shadow_panel, irradiated_stone, relay_node, signal_strut.
- Authored blockstate and model JSONs for all new blocks so they render in-game.
- Scaffolding for five structure families with data-driven jigsaw starts and structure sets:
  - Panel Arrays, Node Clusters, Scaffold Lattices, Shadow Wedges, Relay Towers.
- Added biome tags to allow each structure in its intended Dyson biome.

## Files Added/Updated

- Blocks: `common/src/main/java/com/netroaki/chex/block/ModBlocks.java`
- Assets: `common/src/main/resources/assets/cosmic_horizons_extended/blockstates|models/block/*.json`
- Structures: `common/src/main/resources/data/cosmic_horizons_extended/worldgen/structure/*.json`
- Template Pools: `common/src/main/resources/data/cosmic_horizons_extended/worldgen/template_pool/*/start.json`
- Structure Sets: `common/src/main/resources/data/cosmic_horizons_extended/worldgen/structure_set/*.json`
- Biome Tags: `common/src/main/resources/data/cosmic_horizons_extended/tags/worldgen/biome/has_structure/*.json`

## Notes

- Template pools currently use `minecraft:empty_pool_element` as placeholders. Real NBT templates and processors can be added later without changing the wiring.
- Spacing and salts in structure_set files are conservative and can be tuned after playtesting.
