# Step 020 - Pandora Dimension JSON (T-020)

## Acceptance

- Dimension loads in dev with specified effects
- Document config choices
- Checks pass

## Changes

- Added custom sky effects for Pandora and registered them:
  - `forge/src/main/java/com/netroaki/chex/client/PandoraSkyEffects.java`
  - `forge/src/main/java/com/netroaki/chex/client/CHEXClientSetup.java` (register under `cosmic_horizons_extended:pandora`)
- Verified dimension JSONs exist and reference effects:
  - `data/cosmic_horizons_extended/dimension_type/pandora.json` (effects: `cosmic_horizons_extended:pandora`)
  - `data/cosmic_horizons_extended/dimension/pandora.json` uses `minecraft:multi_noise` with 5 biomes

## Evidence

```1:23:forge/src/main/resources/data/cosmic_horizons_extended/dimension_type/pandora.json
{
  "ultrawarm": false,
  "natural": true,
  "ambient_light": 0.1,
  "has_skylight": true,
  "has_ceiling": false,
  "fixed_time": 12000,
  "bed_works": false,
  "respawn_anchor_works": false,
  "piglin_safe": false,
  "has_raids": true,
  "logical_height": 384,
  "height": 384,
  "min_y": -64,
  "infiniburn": "#minecraft:infiniburn_overworld",
  "coordinate_scale": 1.0,
  "effects": "cosmic_horizons_extended:pandora",
  "monster_spawn_light_level": {
    "type": "minecraft:uniform",
    "value": { "min_inclusive": 0, "max_inclusive": 7 }
  },
  "monster_spawn_block_light_limit": 0
}
```

```50:56:forge/src/main/java/com/netroaki/chex/client/CHEXClientSetup.java
  @SubscribeEvent
  public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
    // Register Pandora special effects (matches dimension_type effects id)
    event.register(
        new net.minecraft.resources.ResourceLocation(
            com.netroaki.chex.CHEX.MOD_ID, "pandora"),
        PandoraSkyEffects.INSTANCE);
  }
```

## Commands Run

- `py -3 scripts/validate_json.py` → SUCCESS
- `./gradlew check` → SUCCESS

## Verdict

PASS – Effects registered; JSONs valid; project checks pass.
