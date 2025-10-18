# Placeholder Texture Creation Script

## Overview

This script creates placeholder textures for all blocks and items in Cosmic Horizons Expanded by recoloring vanilla Minecraft assets using ImageMagick.

## Prerequisites

### 1. Install ImageMagick

Download and install ImageMagick from: https://imagemagick.org/script/download.php#windows

Or use Chocolatey:

```powershell
choco install imagemagick
```

### 2. Verify Installation

```powershell
magick -version
```

### 3. Ensure Assets Available

The script expects Minecraft assets at: `InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures`

## Usage

### Basic Usage

```powershell
.\scripts\create_placeholder_textures.ps1
```

### With Custom Paths

```powershell
.\scripts\create_placeholder_textures.ps1 -SourceDir "path/to/minecraft/assets" -OutputDir "path/to/output"
```

### Force Recreation

```powershell
.\scripts\create_placeholder_textures.ps1 -Force
```

## What It Creates

### Textures

- **Block textures**: Planet-themed recolors of vanilla blocks
- **Item textures**: Recolored vanilla items for progression items
- **Entity textures**: Recolored vanilla entities for all mod entities

### JSON Files

- **Block states**: Simple cube models for all blocks
- **Block models**: Cube models with proper texture references
- **Item models**: Generated item models with texture references
- **Entity models**: Basic item-style models for entities
- **Entity GeoJSON**: Minecraft Bedrock-style geometry for entities

## Color Themes

| Planet        | Color      | Description          |
| ------------- | ---------- | -------------------- |
| Kepler-452b   | Green      | Flora and vegetation |
| Aqua Mundus   | Blue       | Ocean and water      |
| Inferno Prime | Red        | Heat and lava        |
| Arrakis       | Orange     | Desert and sand      |
| Pandora       | Purple     | Bioluminescence      |
| Crystalis     | Blue-white | Ice and crystals     |
| Stormworld    | Cyan       | Storms and wind      |

## Output Structure

```
forge/src/main/resources/assets/cosmic_horizons_extended/
├── textures/
│   ├── block/
│   │   ├── kepler_wood_log.png
│   │   ├── aqua_vent_basalt.png
│   │   └── ...
│   ├── item/
│   │   ├── verdant_core.png
│   │   ├── sovereign_heart.png
│   │   └── ...
│   └── entity/
│       ├── sporefly.png
│       ├── ash_crawler.png
│       └── ...
├── blockstates/
│   ├── kepler_wood_log.json
│   └── ...
├── models/
│   ├── block/
│   │   ├── kepler_wood_log.json
│   │   └── ...
│   ├── item/
│   │   ├── verdant_core.json
│   │   └── ...
│   └── entity/
│       ├── sporefly.json
│       ├── sporefly.geo.json
│       └── ...
```

## Troubleshooting

### ImageMagick Not Found

- Ensure ImageMagick is installed and in PATH
- Try running `magick -version` in PowerShell
- Restart PowerShell after installation

### Source Assets Not Found

- Verify the Minecraft assets are in the correct location
- Check the path in the script parameters
- Ensure the assets folder contains the expected structure

### Permission Errors

- Run PowerShell as Administrator
- Check file permissions in the output directory
- Ensure the mod project is not locked by an IDE

### Texture Quality Issues

- All textures are 16x16 pixels (Minecraft standard)
- Colors are applied using ImageMagick's modulate function
- For better quality, replace with custom art later

## Customization

### Adding New Blocks/Items

Edit the script to add new entries to:

- `$BlockTextureMappings` for blocks
- `$ItemTextureMappings` for items

### Changing Color Themes

Modify the `$ColorMappings` hashtable to adjust:

- Hue (0-360 degrees)
- Saturation (0.0-2.0)
- Brightness (0.0-2.0)

### Different Base Textures

Change the base texture references to use different vanilla blocks/items as starting points.

## Next Steps

After running the script:

1. Test the mod to ensure all textures load
2. Replace placeholder textures with custom art
3. Create custom models for animated blocks
4. Add normal maps and emissive textures for advanced effects

## Notes

- These are placeholder textures for development
- Replace with custom art before release
- All textures follow Minecraft's 16x16 pixel standard
- Colors are planet-themed for easy identification
- JSON files use simple cube models for compatibility
