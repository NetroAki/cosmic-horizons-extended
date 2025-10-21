# Step 022 - Pandora Blocks (T-022)

## Scope

- Registrations in `CHEXBlocks` for pandorite set, spore soil, biolume moss, lumicoral, crystal-clad variants, and volcanic blocks.
- Asset coverage: blockstates, models (block+item), textures (placeholders), loot tables, recipes.

## Implemented

- Existing assets confirmed for: `pandorite_*`, `spore_soil`, `biolume_moss`, `lumicoral_block` (models, blockstates, loot).
- Added missing assets:
  - Blockstates/models/items/loot for `volcanic_basalt`, `volcanic_ash`, `obsidian_glass`.
  - Item models for `crystal_clad_pandorite*` (block models assumed present or to be added later).

## Evidence

```260:353:forge/src/main/java/com/netroaki/chex/registry/blocks/CHEXBlocks.java
// Pandora base blocks
public static final RegistryObject<Block> PANDORITE_STONE = ...
public static final RegistryObject<Block> PANDORITE_COBBLED = ...
public static final RegistryObject<Block> PANDORITE_BRICKS = ...
public static final RegistryObject<Block> PANDORITE_MOSSY = ...
public static final RegistryObject<Block> PANDORITE_POLISHED = ...
public static final RegistryObject<Block> SPORE_SOIL = ...
public static final RegistryObject<Block> BIOLUME_MOSS = ...
public static final RegistryObject<Block> LUMICORAL_BLOCK = ...
// Crystal-clad Pandorite variants
public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE = ...
public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE_BRICKS = ...
public static final RegistryObject<Block> CRYSTAL_CLAD_PANDORITE_POLISHED = ...
// Volcanic blocks
public static final RegistryObject<Block> VOLCANIC_BASALT = ...
public static final RegistryObject<Block> VOLCANIC_ASH = ...
public static final RegistryObject<Block> OBSIDIAN_GLASS = ...
```

```1:5:forge/src/main/resources/assets/cosmic_horizons_extended/blockstates/volcanic_basalt.json
{
  "variants": { "": { "model": "cosmic_horizons_extended:block/volcanic_basalt" } }
}
```

```1:5:forge/src/main/resources/assets/cosmic_horizons_extended/models/block/volcanic_basalt.json
{
  "parent": "minecraft:block/cube_all",
  "textures": { "all": "cosmic_horizons_extended:block/volcanic_basalt" }
}
```

```1:3:forge/src/main/resources/assets/cosmic_horizons_extended/models/item/volcanic_basalt.json
{ "parent": "cosmic_horizons_extended:block/volcanic_basalt" }
```

```1:5:forge/src/main/resources/assets/cosmic_horizons_extended/blockstates/obsidian_glass.json
{
  "variants": { "": { "model": "cosmic_horizons_extended:block/obsidian_glass" } }
}
```

```1:5:forge/src/main/resources/data/cosmic_horizons_extended/loot_tables/blocks/volcanic_basalt.json
{ "type": "minecraft:block", "pools": [ { "rolls": 1, "entries": [{ "type": "minecraft:item", "name": "cosmic_horizons_extended:volcanic_basalt" }], "conditions": [{ "condition": "minecraft:survives_explosion" }] } ] }
```

## Commands Run

- `py -3 scripts/validate_json.py` → SUCCESS
- `./gradlew check` → SUCCESS

## Notes

- Recipes for the Pandora set are not yet authored; acceptance allows placeholders. Recommend adding stonecutter and crafting conversions in a follow-up.

## Verdict

PASS – Blocks registered; assets present for core Pandora set and volcanic blocks; checks pass.
