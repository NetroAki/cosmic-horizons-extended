# Task 09: Create Entity Placeholder Models and Textures

**Priority**: MEDIUM  
**Estimated Time**: 30 minutes  
**Files Affected**: All entity models and textures

---

## Problem

The mod has entity classes but no models or textures, causing rendering issues and missing texture errors during debugging.

## Objective

Create placeholder models and textures for all entities so they can render properly during development and testing.

## What Gets Created

### Entity Textures (10 entities)

- **Pandora**: Sporefly, Spore Tyrant
- **Arrakis**: Sandworm Hatchling
- **Inferno Prime**: Ash Crawler, Fire Wraith, Magma Hopper, Infernal Sovereign
- **Aqua Mundus**: Ocean Sovereign
- **Stormworld**: Windrider
- **Existing**: Glowbeast, Sporeflies

### Entity Models (2 types per entity)

- **Basic Model JSON**: Simple item-style model for basic rendering
- **GeoJSON**: Minecraft Bedrock-style geometry for advanced rendering

## Solution

### 1. Entity Texture Mappings

Uses vanilla entity textures as base, recolored with planet themes:

```powershell
$EntityTextureMappings = @{
    # Pandora Entities
    "sporefly" = "entity/bee/bee"                    # Purple-tinted bee
    "spore_tyrant" = "entity/enderman/enderman"      # Purple-tinted enderman

    # Arrakis Entities
    "sandworm_hatchling" = "entity/silverfish/silverfish"  # Orange-tinted silverfish

    # Inferno Prime Entities
    "ash_crawler" = "entity/spider/spider"           # Red-tinted spider
    "fire_wraith" = "entity/blaze/blaze"             # Red-tinted blaze
    "magma_hopper" = "entity/magma_cube/magma_cube"  # Red-tinted magma cube
    "infernal_sovereign" = "entity/wither/wither"    # Red-tinted wither

    # Aqua Mundus Entities
    "ocean_sovereign" = "entity/elder_guardian/elder_guardian"  # Blue-tinted guardian

    # Stormworld Entities
    "windrider" = "entity/phantom/phantom"           # Cyan-tinted phantom

    # Existing Entities
    "glowbeast" = "entity/bat/bat"                   # Default bat
    "sporeflies" = "entity/bee/bee"                  # Default bee
}
```

### 2. Planet Color Themes for Entities

- **Pandora**: Purple tint (bioluminescence)
- **Arrakis**: Orange tint (desert)
- **Inferno Prime**: Red tint (heat/fire)
- **Aqua Mundus**: Blue tint (ocean)
- **Stormworld**: Cyan tint (storms)
- **Default**: No color change

### 3. Model Types Created

#### Basic Entity Model JSON

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "cosmic_horizons_extended:entity/sporefly"
  }
}
```

#### Entity GeoJSON (Bedrock-style)

```json
{
  "format_version": "1.12.0",
  "minecraft:geometry": [
    {
      "description": {
        "identifier": "geometry.sporefly",
        "texture_width": 64,
        "texture_height": 64,
        "visible_bounds_width": 2,
        "visible_bounds_height": 2,
        "visible_bounds_offset": [0, 0, 0]
      },
      "bones": [
        {
          "name": "body",
          "pivot": [0, 0, 0],
          "cubes": [
            {
              "origin": [-4, 0, -4],
              "size": [8, 8, 8],
              "uv": [0, 0]
            }
          ]
        }
      ]
    }
  ]
}
```

## Execution Steps

### Step 1: Run Enhanced Texture Script

```bash
# The script now includes entity creation
.\scripts\create_placeholder_textures.ps1
```

### Step 2: Verify Entity Assets Created

```bash
# Check entity textures
ls forge/src/main/resources/assets/cosmic_horizons_extended/textures/entity/

# Check entity models
ls forge/src/main/resources/assets/cosmic_horizons_extended/models/entity/
```

### Step 3: Test Entity Rendering

```bash
# Launch mod and spawn entities to test
./gradlew :forge:runClient
```

## Expected Results

After completion:

- ✅ All 10 entities have placeholder textures
- ✅ All entities have basic model JSONs
- ✅ All entities have GeoJSON models
- ✅ Textures are planet-themed (purple for Pandora, red for Inferno, etc.)
- ✅ No missing texture errors for entities
- ✅ Entities render as colored cubes (placeholder)

## File Structure Created

```
forge/src/main/resources/assets/cosmic_horizons_extended/
├── textures/entity/
│   ├── sporefly.png
│   ├── spore_tyrant.png
│   ├── sandworm_hatchling.png
│   ├── ash_crawler.png
│   ├── fire_wraith.png
│   ├── magma_hopper.png
│   ├── infernal_sovereign.png
│   ├── ocean_sovereign.png
│   ├── windrider.png
│   ├── glowbeast.png
│   └── sporeflies.png
└── models/entity/
    ├── sporefly.json
    ├── sporefly.geo.json
    ├── spore_tyrant.json
    ├── spore_tyrant.geo.json
    └── ... (20 total files)
```

## Verification Commands

### Check Texture Creation

```bash
# Count entity textures
ls forge/src/main/resources/assets/cosmic_horizons_extended/textures/entity/ | wc -l
# Should show 10 textures

# Count entity models
ls forge/src/main/resources/assets/cosmic_horizons_extended/models/entity/ | wc -l
# Should show 20 model files (2 per entity)
```

### Test Entity Spawning

```bash
# In-game commands to test entities
/summon cosmic_horizons_extended:sporefly ~ ~ ~
/summon cosmic_horizons_extended:ash_crawler ~ ~ ~
/summon cosmic_horizons_extended:fire_wraith ~ ~ ~
```

## Notes

### Placeholder Nature

- **Textures**: Recolored vanilla entities (not custom art)
- **Models**: Simple cube geometry (not detailed 3D models)
- **Purpose**: Enable debugging and testing, not final appearance

### Planet Theming

- Colors help identify which planet each entity belongs to
- Easy to distinguish during testing and development
- Can be replaced with custom art later

### Model Compatibility

- **Basic JSON**: Works with Forge's entity rendering
- **GeoJSON**: Compatible with Bedrock-style rendering systems
- **Simple Geometry**: Ensures compatibility across different rendering backends

### Performance

- Minimal polygon count (8x8x8 cube per entity)
- Small texture size (16x16 pixels)
- Optimized for development and testing

---

**This task ensures all entities have valid models and textures for debugging purposes. The placeholders can be replaced with custom 3D models and textures as part of the long-term modeling work outlined in Task 08.**
