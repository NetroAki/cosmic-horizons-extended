# Task 07: Create Placeholder Textures

**Priority**: MEDIUM  
**Estimated Time**: 45 minutes  
**Files Affected**: All blocks and items without textures

---

## Problem

The mod currently has no textures, models, or animations. All blocks and items need placeholder textures created using ImageMagick and the provided Minecraft assets.

## Objective

Create placeholder textures for all blocks and items in the mod by recoloring existing Minecraft assets using ImageMagick.

## Assets Available

**Source**: `InventivetalentDev-minecraft-assets-af628ec/assets/`

- Contains all vanilla Minecraft textures
- Includes blocks, items, entities, and GUI elements
- Can be used as base for recoloring

## Blocks and Items Needing Textures

### Kepler-452b Flora Blocks

- `kepler_wood_log` - Recolor oak log
- `kepler_wood_leaves` - Recolor oak leaves
- `kepler_moss` - Recolor moss carpet
- `kepler_vines` - Recolor vines
- `kepler_blossom` - Recolor flowering azalea leaves

### Aqua Mundus Ocean Blocks

- `aqua_vent_basalt` - Recolor basalt
- `aqua_manganese_nodule` - Recolor iron ore
- `aqua_luminous_kelp` - Recolor kelp
- `aqua_ice_shelf_slab` - Recolor packed ice

### Existing Mod Blocks (from previous tasks)

- All planet-specific ores
- All building blocks
- All terrain blocks
- All decorative blocks

### Items

- All boss cores
- All fuel items
- All progression items
- All tools/armor

## Solution Steps

### 1. Inventory All Missing Textures

```bash
# Find all block/item registrations
grep -r "BLOCKS.register\|ITEMS.register" forge/src/main/java/ | grep -v "//"
grep -r "BLOCKS.register\|ITEMS.register" common/src/main/java/ | grep -v "//"
```

### 2. Create Texture Directory Structure

```bash
mkdir -p forge/src/main/resources/assets/cosmic_horizons_extended/textures/block
mkdir -p forge/src/main/resources/assets/cosmic_horizons_extended/textures/item
mkdir -p forge/src/main/resources/assets/cosmic_horizons_extended/textures/entity
```

### 3. ImageMagick Recoloring Script

Create `scripts/create_placeholder_textures.ps1`:

```powershell
# PowerShell script to create placeholder textures
param(
    [string]$SourceDir = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures",
    [string]$OutputDir = "forge/src/main/resources/assets/cosmic_horizons_extended/textures"
)

# Color mappings for different planets
$ColorMappings = @{
    "kepler" = @{
        "hue" = 120    # Green tint
        "saturation" = 1.2
        "brightness" = 1.1
    }
    "aqua" = @{
        "hue" = 200    # Blue tint
        "saturation" = 1.3
        "brightness" = 0.9
    }
    "inferno" = @{
        "hue" = 0      # Red tint
        "saturation" = 1.4
        "brightness" = 1.2
    }
    "arrakis" = @{
        "hue" = 30     # Orange tint
        "saturation" = 1.1
        "brightness" = 1.3
    }
    "pandora" = @{
        "hue" = 280    # Purple tint
        "saturation" = 1.2
        "brightness" = 0.8
    }
}

# Texture mappings (what vanilla texture to use as base)
$TextureMappings = @{
    "kepler_wood_log" = "block/oak_log"
    "kepler_wood_leaves" = "block/oak_leaves"
    "kepler_moss" = "block/moss_carpet"
    "kepler_vines" = "block/vine"
    "kepler_blossom" = "block/flowering_azalea_leaves"
    "aqua_vent_basalt" = "block/basalt"
    "aqua_manganese_nodule" = "block/iron_ore"
    "aqua_luminous_kelp" = "block/kelp"
    "aqua_ice_shelf_slab" = "block/packed_ice"
}

function Create-PlaceholderTexture {
    param(
        [string]$BlockName,
        [string]$BaseTexture,
        [string]$Planet
    )

    $sourcePath = "$SourceDir/$BaseTexture.png"
    $outputPath = "$OutputDir/block/$BlockName.png"

    if (Test-Path $sourcePath) {
        $colors = $ColorMappings[$Planet]
        $hue = $colors.hue
        $sat = $colors.saturation
        $bright = $colors.brightness

        # Use ImageMagick to recolor
        magick "$sourcePath" -modulate $bright,$sat,$hue "$outputPath"
        Write-Host "Created: $outputPath"
    } else {
        Write-Warning "Source texture not found: $sourcePath"
    }
}

# Create all placeholder textures
foreach ($mapping in $TextureMappings.GetEnumerator()) {
    $blockName = $mapping.Key
    $baseTexture = $mapping.Value

    # Determine planet from block name
    $planet = if ($blockName -like "kepler_*") { "kepler" }
              elseif ($blockName -like "aqua_*") { "aqua" }
              elseif ($blockName -like "inferno_*") { "inferno" }
              elseif ($blockName -like "arrakis_*") { "arrakis" }
              elseif ($blockName -like "pandora_*") { "pandora" }
              else { "default" }

    Create-PlaceholderTexture -BlockName $blockName -BaseTexture $baseTexture -Planet $planet
}
```

### 4. Create Item Textures

For items, create simple recolored versions of vanilla items:

```powershell
# Item texture mappings
$ItemMappings = @{
    "verdant_core" = "item/emerald"
    "sovereign_heart" = "item/heart_of_the_sea"
    "titan_core" = "item/nether_star"
    "spore_tyrant_core" = "item/ender_pearl"
    "worldheart_core" = "item/experience_bottle"
}

foreach ($mapping in $ItemMappings.GetEnumerator()) {
    $itemName = $mapping.Key
    $baseItem = $mapping.Value

    $sourcePath = "$SourceDir/item/$baseItem.png"
    $outputPath = "$OutputDir/item/$itemName.png"

    if (Test-Path $sourcePath) {
        magick "$sourcePath" -modulate 110,120,100 "$outputPath"
        Write-Host "Created item texture: $outputPath"
    }
}
```

### 5. Create Block State JSONs

For each block, create a simple block state JSON:

```json
{
  "variants": {
    "": {
      "model": "cosmic_horizons_extended:block/kepler_wood_log"
    }
  }
}
```

### 6. Create Item Models

For each item, create a simple item model JSON:

```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "cosmic_horizons_extended:item/verdant_core"
  }
}
```

## Execution Steps

### Step 1: Install ImageMagick

```bash
# Download and install ImageMagick for Windows
# Or use chocolatey: choco install imagemagick
```

### Step 2: Run Texture Creation Script

```bash
# Make script executable
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

# Run the script
.\scripts\create_placeholder_textures.ps1
```

### Step 3: Create Block State JSONs

```bash
# Create block state JSONs for all blocks
# Use template and replace block names
```

### Step 4: Create Item Models

```bash
# Create item model JSONs for all items
# Use template and replace item names
```

### Step 5: Verify Textures

```bash
# Check that all textures were created
ls forge/src/main/resources/assets/cosmic_horizons_extended/textures/block/
ls forge/src/main/resources/assets/cosmic_horizons_extended/textures/item/
```

## Expected Results

After completion:

- ✅ All blocks have placeholder textures
- ✅ All items have placeholder textures
- ✅ All block state JSONs created
- ✅ All item model JSONs created
- ✅ Textures are planet-themed (green for Kepler, blue for Aqua, etc.)

## Verification

Run the mod and check:

1. All blocks render with textures (not missing texture pink/black)
2. All items show in inventory with textures
3. Textures have appropriate planet-themed colors
4. No missing texture errors in logs

## Notes

- These are placeholder textures - can be replaced with custom art later
- Colors are planet-themed for easy identification
- All textures are 16x16 pixels (Minecraft standard)
- Block state JSONs use simple cube models
- Item models use generated item template

---

**Next Task**: [08_models_animations_tracking.md](08_models_animations_tracking.md)
