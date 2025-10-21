# PowerShell script to create placeholder textures using ImageMagick
# Recolors vanilla Minecraft textures for use as placeholders in Cosmic Horizons Expanded

param(
    [string]$SourceDir = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures",
    [string]$OutputDir = "forge/src/main/resources/assets/cosmic_horizons_extended/textures",
    [switch]$Force = $false
)

Write-Host "Creating placeholder textures for Cosmic Horizons Expanded..." -ForegroundColor Green
Write-Host "Source: $SourceDir" -ForegroundColor Yellow
Write-Host "Output: $OutputDir" -ForegroundColor Yellow

# Check if ImageMagick is installed
try {
    $magickVersion = magick -version 2>$null
    if (-not $magickVersion) {
        throw "ImageMagick not found"
    }
    Write-Host "ImageMagick found: $($magickVersion[0])" -ForegroundColor Green
}
catch {
    Write-Error "ImageMagick is required but not found. Please install ImageMagick first."
    Write-Host "Download from: https://imagemagick.org/script/download.php#windows" -ForegroundColor Yellow
    exit 1
}

# Check if source directory exists
if (-not (Test-Path $SourceDir)) {
    Write-Error "Source directory not found: $SourceDir"
    Write-Host "Please ensure the Minecraft assets are available at the specified path." -ForegroundColor Yellow
    exit 1
}

# Create output directories
$directories = @(
    "$OutputDir/block",
    "$OutputDir/item",
    "$OutputDir/entity"
)

foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        Write-Host "Created directory: $dir" -ForegroundColor Cyan
    }
}

# Color mappings for different planets
$ColorMappings = @{
    "kepler"     = @{
        "hue"         = 120    # Green tint
        "saturation"  = 1.2
        "brightness"  = 1.1
        "description" = "Green-tinted (Kepler-452b flora)"
    }
    "aqua"       = @{
        "hue"         = 200    # Blue tint  
        "saturation"  = 1.3
        "brightness"  = 0.9
        "description" = "Blue-tinted (Aqua Mundus ocean)"
    }
    "inferno"    = @{
        "hue"         = 0      # Red tint
        "saturation"  = 1.4
        "brightness"  = 1.2
        "description" = "Red-tinted (Inferno Prime heat)"
    }
    "arrakis"    = @{
        "hue"         = 30     # Orange tint
        "saturation"  = 1.1
        "brightness"  = 1.3
        "description" = "Orange-tinted (Arrakis desert)"
    }
    "pandora"    = @{
        "hue"         = 280    # Purple tint
        "saturation"  = 1.2
        "brightness"  = 0.8
        "description" = "Purple-tinted (Pandora bioluminescence)"
    }
    "crystalis"  = @{
        "hue"         = 240    # Blue-white tint
        "saturation"  = 0.8
        "brightness"  = 1.4
        "description" = "Blue-white tinted (Crystalis ice)"
    }
    "stormworld" = @{
        "hue"         = 180    # Cyan tint
        "saturation"  = 1.1
        "brightness"  = 0.7
        "description" = "Cyan-tinted (Stormworld storms)"
    }
    "default"    = @{
        "hue"         = 0      # No hue change
        "saturation"  = 1.0
        "brightness"  = 1.0
        "description" = "No color change (default)"
    }
}

# Block texture mappings (what vanilla texture to use as base)
$BlockTextureMappings = @{
    # Kepler-452b Flora
    "kepler_wood_log"        = "block/oak_log"
    "kepler_wood_leaves"     = "block/oak_leaves"
    "kepler_moss"            = "block/moss_block"
    "kepler_vines"           = "block/vine"
    "kepler_blossom"         = "block/flowering_azalea_leaves"
    
    # Aqua Mundus Ocean
    "aqua_vent_basalt"       = "block/smooth_basalt"
    "aqua_manganese_nodule"  = "block/iron_ore"
    "aqua_luminous_kelp"     = "block/kelp"
    "aqua_ice_shelf_slab"    = "block/packed_ice"
    
    # Inferno Prime
    "inferno_magma_block"    = "block/magma"
    "inferno_obsidian"       = "block/obsidian"
    "inferno_basalt"         = "block/basalt_side"
    
    # Arrakis
    "arrakis_sandstone"      = "block/sandstone"
    "arrakis_spice_ore"      = "block/gold_ore"
    
    # Pandora
    "pandora_fungal_block"   = "block/mycelium_top"
    "pandora_spore_block"    = "block/end_stone"
    
    # Crystalis
    "crystalis_ice"          = "block/ice"
    "crystalis_diamond_ore"  = "block/diamond_ore"
    
    # Stormworld
    "stormworld_storm_block" = "block/blackstone"
}

# Item texture mappings
$ItemTextureMappings = @{
    # Boss Cores
    "verdant_core"      = "item/emerald"
    "sovereign_heart"   = "item/heart_of_the_sea"
    "titan_core"        = "item/nether_star"
    "spore_tyrant_core" = "item/ender_pearl"
    "worldheart_core"   = "item/experience_bottle"
    
    # Fuel Items
    "rocket_fuel_t1"    = "item/coal"
    "rocket_fuel_t2"    = "item/charcoal"
    "rocket_fuel_t3"    = "item/blaze_powder"
    "rocket_fuel_t4"    = "item/ghast_tear"
    "rocket_fuel_t5"    = "item/nether_star"
    
    # Progression Items
    "nodule_t1"         = "item/iron_ingot"
    "nodule_t2"         = "item/gold_ingot"
    "nodule_t3"         = "item/diamond"
    "nodule_t4"         = "item/netherite_ingot"
    "nodule_t5"         = "item/nether_star"
}

# Entity texture mappings (using vanilla entities as base)
$EntityTextureMappings = @{
    # Pandora Entities
    "sporefly"           = "entity/bee/bee"
    "spore_tyrant"       = "entity/enderman/enderman"
    
    # Arrakis Entities
    "sandworm_hatchling" = "entity/silverfish"
    
    # Inferno Prime Entities
    "ash_crawler"        = "entity/spider/spider"
    "fire_wraith"        = "entity/blaze"
    "magma_hopper"       = "entity/slime/magmacube"
    "infernal_sovereign" = "entity/wither/wither"
    
    # Aqua Mundus Entities
    "ocean_sovereign"    = "entity/guardian_elder"
    
    # Stormworld Entities
    "windrider"          = "entity/phantom"
    
    # Existing Entities (if they exist)
    "glowbeast"          = "entity/bat"
    "sporeflies"         = "entity/bee/bee"
}

function Create-PlaceholderTexture {
    param(
        [string]$Name,
        [string]$BaseTexture,
        [string]$Planet,
        [string]$Type = "block"
    )
    
    $normalizedBase = $BaseTexture -replace '/', [System.IO.Path]::DirectorySeparatorChar
    $sourcePath = [System.IO.Path]::Combine($SourceDir, $normalizedBase + ".png")
    $outputPath = "$OutputDir/$Type/$Name.png"
    
    # Skip if output exists and not forcing
    if ((Test-Path $outputPath) -and -not $Force) {
        Write-Host "Skipping $Name (already exists)" -ForegroundColor Gray
        return
    }
    
    if (Test-Path $sourcePath) {
        $colors = $ColorMappings[$Planet]
        $hue = $colors.hue
        $sat = $colors.saturation
        $bright = $colors.brightness
        
        try {
            # Use ImageMagick to recolor
            $modulateArgs = "{0},{1},{2}" -f $bright, $sat, $hue
            magick "$sourcePath" -modulate $modulateArgs "$outputPath"
            Write-Host "Created: $Name ($($colors.description))" -ForegroundColor Green
        }
        catch {
            Write-Warning "Failed to create $Name : $($_.Exception.Message)"
        }
    }
    else {
        Write-Warning "Source texture not found: $sourcePath"
    }
}

function Create-BlockStateJson {
    param(
        [string]$BlockName
    )
    
    $blockStateDir = "forge/src/main/resources/assets/cosmic_horizons_extended/blockstates"
    if (-not (Test-Path $blockStateDir)) {
        New-Item -ItemType Directory -Path $blockStateDir -Force | Out-Null
    }
    
    $blockStatePath = "$blockStateDir/$BlockName.json"
    $blockStateContent = @{
        "variants" = @{
            "" = @{
                "model" = "cosmic_horizons_extended:block/$BlockName"
            }
        }
    } | ConvertTo-Json -Depth 3
    
    Set-Content -Path $blockStatePath -Value $blockStateContent
    Write-Host "Created block state: $BlockName.json" -ForegroundColor Cyan
}

function Create-ItemModelJson {
    param(
        [string]$ItemName
    )
    
    $itemModelDir = "forge/src/main/resources/assets/cosmic_horizons_extended/models/item"
    if (-not (Test-Path $itemModelDir)) {
        New-Item -ItemType Directory -Path $itemModelDir -Force | Out-Null
    }
    
    $itemModelPath = "$itemModelDir/$ItemName.json"
    $itemModelContent = @{
        "parent"   = "item/generated"
        "textures" = @{
            "layer0" = "cosmic_horizons_extended:item/$ItemName"
        }
    } | ConvertTo-Json -Depth 3
    
    Set-Content -Path $itemModelPath -Value $itemModelContent
    Write-Host "Created item model: $ItemName.json" -ForegroundColor Cyan
}

function Create-BlockModelJson {
    param(
        [string]$BlockName
    )
    
    $blockModelDir = "forge/src/main/resources/assets/cosmic_horizons_extended/models/block"
    if (-not (Test-Path $blockModelDir)) {
        New-Item -ItemType Directory -Path $blockModelDir -Force | Out-Null
    }
    
    $blockModelPath = "$blockModelDir/$BlockName.json"
    $blockModelContent = @{
        "parent"   = "block/cube_all"
        "textures" = @{
            "all" = "cosmic_horizons_extended:block/$BlockName"
        }
    } | ConvertTo-Json -Depth 3
    
    Set-Content -Path $blockModelPath -Value $blockModelContent
    Write-Host "Created block model: $BlockName.json" -ForegroundColor Cyan
}

function Create-EntityModelJson {
    param(
        [string]$EntityName
    )
    
    $entityModelDir = "forge/src/main/resources/assets/cosmic_horizons_extended/models/entity"
    if (-not (Test-Path $entityModelDir)) {
        New-Item -ItemType Directory -Path $entityModelDir -Force | Out-Null
    }
    
    $entityModelPath = "$entityModelDir/$EntityName.json"
    
    # Create a simple cube model for entities (placeholder)
    $entityModelContent = @{
        "parent"   = "minecraft:item/generated"
        "textures" = @{
            "layer0" = "cosmic_horizons_extended:entity/$EntityName"
        }
    } | ConvertTo-Json -Depth 3
    
    Set-Content -Path $entityModelPath -Value $entityModelContent
    Write-Host "Created entity model: $EntityName.json" -ForegroundColor Cyan
}

function Create-EntityGeoJson {
    param(
        [string]$EntityName
    )
    
    $entityModelDir = "forge/src/main/resources/assets/cosmic_horizons_extended/models/entity"
    if (-not (Test-Path $entityModelDir)) {
        New-Item -ItemType Directory -Path $entityModelDir -Force | Out-Null
    }
    
    $entityGeoPath = "$entityModelDir/$EntityName.geo.json"
    
    # Create a minimal GeoJSON for entity (placeholder)
    $entityGeoContent = @{
        "format_version"     = "1.12.0"
        "minecraft:geometry" = @(
            @{
                "description" = @{
                    "identifier"            = "geometry.$EntityName"
                    "texture_width"         = 64
                    "texture_height"        = 64
                    "visible_bounds_width"  = 2
                    "visible_bounds_height" = 2
                    "visible_bounds_offset" = @(0, 0, 0)
                }
                "bones"       = @(
                    @{
                        "name"  = "body"
                        "pivot" = @(0, 0, 0)
                        "cubes" = @(
                            @{
                                "origin" = @(-4, 0, -4)
                                "size"   = @(8, 8, 8)
                                "uv"     = @(0, 0)
                            }
                        )
                    }
                )
            }
        )
    } | ConvertTo-Json -Depth 10
    
    Set-Content -Path $entityGeoPath -Value $entityGeoContent
    Write-Host "Created entity GeoJSON: $EntityName.geo.json" -ForegroundColor Cyan
}

# Create all block textures
Write-Host "`nCreating block textures..." -ForegroundColor Yellow
foreach ($mapping in $BlockTextureMappings.GetEnumerator()) {
    $blockName = $mapping.Key
    $baseTexture = $mapping.Value
    
    # Determine planet from block name
    $planet = if ($blockName -like "kepler_*") { "kepler" }
    elseif ($blockName -like "aqua_*") { "aqua" }
    elseif ($blockName -like "inferno_*") { "inferno" }
    elseif ($blockName -like "arrakis_*") { "arrakis" }
    elseif ($blockName -like "pandora_*") { "pandora" }
    elseif ($blockName -like "crystalis_*") { "crystalis" }
    elseif ($blockName -like "stormworld_*") { "stormworld" }
    else { "default" }
    
    Create-PlaceholderTexture -Name $blockName -BaseTexture $baseTexture -Planet $planet -Type "block"
    Create-BlockStateJson -BlockName $blockName
    Create-BlockModelJson -BlockName $blockName
}

# Create all item textures
Write-Host "`nCreating item textures..." -ForegroundColor Yellow
foreach ($mapping in $ItemTextureMappings.GetEnumerator()) {
    $itemName = $mapping.Key
    $baseItem = $mapping.Value
    
    # Determine planet from item name
    $planet = if ($itemName -like "*core*" -or $itemName -like "*heart*") { "default" }
    elseif ($itemName -like "*fuel*") { "inferno" }
    elseif ($itemName -like "*nodule*") { "default" }
    else { "default" }
    
    Create-PlaceholderTexture -Name $itemName -BaseTexture $baseItem -Planet $planet -Type "item"
    Create-ItemModelJson -ItemName $itemName
}

# Create all entity textures
Write-Host "`nCreating entity textures..." -ForegroundColor Yellow
foreach ($mapping in $EntityTextureMappings.GetEnumerator()) {
    $entityName = $mapping.Key
    $baseEntity = $mapping.Value
    
    # Determine planet from entity name
    $planet = if ($entityName -like "spore*") { "pandora" }
    elseif ($entityName -like "sandworm*") { "arrakis" }
    elseif ($entityName -like "ash*" -or $entityName -like "fire*" -or $entityName -like "magma*" -or $entityName -like "infernal*") { "inferno" }
    elseif ($entityName -like "ocean*") { "aqua" }
    elseif ($entityName -like "wind*") { "stormworld" }
    else { "default" }
    
    Create-PlaceholderTexture -Name $entityName -BaseTexture $baseEntity -Planet $planet -Type "entity"
    Create-EntityModelJson -EntityName $entityName
    Create-EntityGeoJson -EntityName $entityName
}

Write-Host "`nPlaceholder texture creation complete!" -ForegroundColor Green
Write-Host "Created textures for $($BlockTextureMappings.Count) blocks, $($ItemTextureMappings.Count) items, and $($EntityTextureMappings.Count) entities." -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Yellow
Write-Host "1. Test the mod to ensure all textures load correctly" -ForegroundColor White
Write-Host "2. Replace placeholder textures with custom art as needed" -ForegroundColor White
Write-Host "3. Create custom models for animated blocks and entities" -ForegroundColor White
