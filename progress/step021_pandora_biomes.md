# Step 021 - Pandora Biomes (T-021)

## Acceptance

- Five biome JSONs exist (Bioluminescent Forest, Floating Mountains, Ocean Depths, Volcanic Wasteland, Sky Islands)
- JSONs compile; project checks pass

## Evidence

```1:17:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_bioluminescent_forest.json
{
  "temperature": 0.95,
  "downfall": 0.9,
  "has_precipitation": true,
  "effects": {
    "sky_color": 5116053,
    "fog_color": 1776419,
    "water_color": 3839730,
    "water_fog_color": 526111,
    "grass_color": 6005541,
    "foliage_color": 4780096
  },
  "spawners": {},
  "spawn_costs": {},
  "carvers": {},
  "features": []
}
```

```1:16:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_floating_mountains.json
{
  "temperature": 0.7,
  "downfall": 0.4,
  "has_precipitation": true,
  "effects": {
    "sky_color": 11452159,
    "fog_color": 9037697,
    "water_color": 4159204,
    "water_fog_color": 329011,
    "grass_color": 8319426
  },
  "spawners": {},
  "spawn_costs": {},
  "carvers": {},
  "features": []
}
```

```1:16:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_ocean_depths.json
{
  "temperature": 0.3,
  "downfall": 1.0,
  "has_precipitation": true,
  "effects": {
    "sky_color": 2240120,
    "fog_color": 861248,
    "water_color": 2264847,
    "water_fog_color": 401708
  },
  "spawners": {},
  "spawn_costs": {},
  "carvers": {},
  "features": []
}
```

```1:15:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_volcanic_wasteland.json
{
  "temperature": 1.3,
  "downfall": 0.1,
  "has_precipitation": false,
  "effects": {
    "sky_color": 8485149,
    "fog_color": 2889483,
    "water_color": 4159204,
    "water_fog_color": 329011
  },
  "spawners": {},
  "spawn_costs": {},
  "carvers": {},
  "features": []
}
```

```1:16:forge/src/main/resources/data/cosmic_horizons_extended/worldgen/biome/pandora_sky_islands.json
{
  "temperature": 0.7,
  "downfall": 0.2,
  "has_precipitation": false,
  "effects": {
    "sky_color": 13631743,
    "fog_color": 15132390,
    "water_color": 4159204,
    "water_fog_color": 329011,
    "grass_color": 11196942
  },
  "spawners": {},
  "spawn_costs": {},
  "carvers": {},
  "features": []
}
```

## Commands Run

- `py -3 scripts/validate_json.py` → SUCCESS
- `./gradlew check` → SUCCESS
- `./gradlew forge:runServer` → Attempt failed due to Windows world lock (another process); no biome JSON errors observed before lock.

## Verdict

PASS – Biome JSONs present and valid; checks pass; server previously loaded dimension JSONs without issues. World lock is environmental and unrelated to biomes.
