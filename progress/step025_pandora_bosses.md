# T-025: Pandora Boss Encounters

- Task: Add boss arenas, spawns, loot, advancements; keep build green
- Status: In progress

## Edits

- Added structure JSON and structure set:
  - `data/cosmic_horizons_extended/worldgen/structure/spore_tyrant_arena.json`
  - `data/cosmic_horizons_extended/worldgen/structure_set/spore_tyrant_arena.json`
- Added anchor placed feature + biome modifier to surface_structures step:
  - `data/cosmic_horizons_extended/worldgen/placed_feature/spore_tyrant_arena_anchor.json`
  - `data/cosmic_horizons_extended/worldgen/biome_modifier/spore_tyrant_arena_addition.json`
- Loot tables:
  - `data/cosmic_horizons_extended/loot_tables/entities/worldheart_avatar.json` (fixed)
  - `data/cosmic_horizons_extended/loot_tables/entities/spore_tyrant.json` (consolidated)
- Advancement:
  - `data/cosmic_horizons_extended/advancements/kill_spore_tyrant.json`

## Checks

- JSON validation: PASS
- `./gradlew check`: PASS
- Server datapack load: PENDING (to be verified in next step)

## Evidence

- Selected JSON refs:

```1:20:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/structure/spore_tyrant_arena.json
{
  "type": "minecraft:jigsaw",
  "biomes": [
    "cosmic_horizons_extended:pandora_bioluminescent_forest",
    "cosmic_horizons_extended:pandora_floating_mountains",
    "cosmic_horizons_extended:pandora_volcanic_wasteland",
    "cosmic_horizons_extended:pandora_sky_islands"
  ],
  "spawn_overrides": {},
  "step": "surface_structures",
  "start_pool": "minecraft:empty",
  "size": 1,
  "start_height": { "type": "minecraft:constant", "value": 64 },
  "project_start_to_heightmap": "WORLD_SURFACE_WG",
  "use_expansion_hack": false
}
```

```1:14:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/structure_set/spore_tyrant_arena.json
{
  "structures": [
    { "structure": "cosmic_horizons_extended:spore_tyrant_arena", "weight": 1 }
  ],
  "placement": {
    "type": "minecraft:random_spread",
    "spacing": 60,
    "separation": 30,
    "salt": 144665123
  }
}
```

```1:13:forge/src/main/resources/data/cosmic_horizons_extended/loot_tables/entities/spore_tyrant.json
{
  "type": "minecraft:entity",
  "pools": [
    {
      "rolls": 1,
      "entries": [
        { "type": "minecraft:item", "name": "minecraft:nether_star" }
      ],
      "conditions": [{ "condition": "minecraft:killed_by_player" }]
    }
  ]
}
```

## Verdict

- Arena JSONs wired; build green. Server verification pending.
