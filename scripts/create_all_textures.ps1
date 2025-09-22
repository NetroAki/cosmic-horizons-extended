# Create placeholder textures for all blocks in the mod
# Uses ImageMagick to recolor vanilla Minecraft textures

# Check if ImageMagick is installed
if (-not (Get-Command magick -ErrorAction SilentlyContinue)) {
    Write-Error "ImageMagick is required. Please install it from https://imagemagick.org/"
    exit 1
}

# Paths
$vanillaAssets = "C:\Users\zacpa\intellij\Cosmic_Horizons_Expanded\InventivetalentDev-minecraft-assets-af628ec\assets\minecraft\textures\block"
$outputDir = "C:\Users\zacpa\intellij\Cosmic_Horizons_Expanded\forge\src\main\resources\assets\chex\textures\block"

# Create output directory if it doesn't exist
if (-not (Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
}

# Function to create a recolored texture with label
function Create-TexturedBlock {
    param (
        [string]$outputName,
        [string]$baseTexture,
        [string]$color,
        [string]$text = ""
    )
    
    $inputPath = Join-Path $vanillaAssets $baseTexture
    $outputPath = Join-Path $outputDir "${outputName}.png"
    
    if (-not (Test-Path $inputPath)) {
        Write-Warning "Base texture not found: $baseTexture"
        return
    }
    
    if ([string]::IsNullOrEmpty($text)) {
        # Simple recolor
        magick $inputPath -fill $color -tint 100 $outputPath
    }
    else {
        # Recolor and add text
        magick $inputPath -fill $color -tint 100 -gravity center -pointsize 20 -fill white -annotate +0+0 $text $outputPath
    }
    
    Write-Host "Created texture: $outputPath"
}

# Function to create a simple colored block
function Create-ColoredBlock {
    param (
        [string]$outputName,
        [string]$color,
        [string]$text = ""
    )
    
    $outputPath = Join-Path $outputDir "${outputName}.png"
    
    if ([string]::IsNullOrEmpty($text)) {
        magick -size 16x16 xc:$color $outputPath
    }
    else {
        magick -size 16x16 xc:$color -gravity center -pointsize 10 -fill white -annotate +0+0 $text $outputPath
    }
    
    Write-Host "Created colored block: $outputPath"
}

# ===== VANILLA-BASED TEXTURES =====

# Pandora Blocks
Create-TexturedBlock "pandorite_stone" "stone.png" "#8B7355" "Pandorite"
Create-TexturedBlock "pandorite_cobbled" "cobblestone.png" "#8B7355" "Cobbled"
Create-TexturedBlock "pandorite_bricks" "stone_bricks.png" "#8B7355" "Bricks"
Create-TexturedBlock "pandorite_mossy" "mossy_stone_bricks.png" "#8B7355" "Mossy"
Create-TexturedBlock "pandorite_polished" "polished_andesite.png" "#A38660" "Polished"
Create-TexturedBlock "pandorite_pillar" "oak_log.png" "#8B7355" "Pillar"
Create-TexturedBlock "pandorite_pillar_top" "oak_log_top.png" "#A38660" "Pillar Top"
Create-TexturedBlock "spore_soil" "dirt.png" "#5E3F3F" "Spore Soil"
Create-TexturedBlock "biolume_moss" "moss_block.png" "#2A5C2A" "Biolume"
Create-TexturedBlock "crystal_clad_pandorite" "amethyst_block.png" "#A0D8EF" "Crystal"
Create-TexturedBlock "lumicoral" "brain_coral_block.png" "#FF69B4" "Lumicoral"
Create-TexturedBlock "pandoran_crystal_cluster" "amethyst_cluster.png" "#E6E6FA" "Crystal"

# Aqua Mundus Blocks
Create-TexturedBlock "vent_basalt" "basalt_side.png" "#2D2D2D" "Vent Basalt"
Create-TexturedBlock "polished_vent_basalt" "polished_basalt_side.png" "#3D3D3D" "Polished"
Create-TexturedBlock "manganese_nodule" "nether_wart_block.png" "#8B8B8B" "Manganese"
Create-TexturedBlock "luminous_kelp" "kelp_plant.png" "#00FF7F" "Luminous"
Create-TexturedBlock "ice_shelf" "blue_ice.png" "#B5E8F7" "Ice Shelf"
Create-TexturedBlock "coral_formation" "brain_coral_block.png" "#FF6B6B" "Coral"

# Arrakis Blocks
Create-TexturedBlock "arrakite_sandstone" "sandstone.png" "#D2B48C" "Arrakite"
Create-TexturedBlock "arrakite_sandstone_cut" "cut_sandstone.png" "#D2B48C" "Cut"
Create-TexturedBlock "arrakite_sandstone_chiseled" "chiseled_sandstone.png" "#D2B48C" "Chiseled"
Create-TexturedBlock "arrakite_sandstone_smooth" "smooth_sandstone.png" "#D2B48C" "Smooth"
Create-TexturedBlock "arrakis_salt" "calcite.png" "#F5F5F5" "Salt"
Create-TexturedBlock "spice_node" "nether_wart_block.png" "#8B4513" "Spice Node"

# Other Dimension Blocks
Create-TexturedBlock "inferno_stone" "netherrack.png" "#8B0000" "Inferno"
Create-TexturedBlock "inferno_ash" "soul_soil.png" "#4A4A4A" "Ash"
Create-TexturedBlock "crystalis_crystal" "amethyst_block.png" "#E6E6FA" "Crystal"
Create-TexturedBlock "crystalis_clear" "glass.png" "#F0F8FF" "Clear"
Create-TexturedBlock "aqua_mundus_stone" "prismarine.png" "#5E9B8B" "Aqua"
Create-TexturedBlock "aqua_dark_prism" "dark_prismarine.png" "#2D5B53" "Dark"
Create-TexturedBlock "neutron_star_basalt" "blackstone.png" "#1A1A1A" "Neutron"
Create-TexturedBlock "neutron_star_plate" "polished_blackstone.png" "#262626" "Plate"

# ===== SIMPLE COLORED BLOCKS =====

# Rocket Assembly Blocks
Create-ColoredBlock "rocket_assembly_table" "#8B4513" "Rocket Table"
Create-ColoredBlock "fuel_refinery" "#A9A9A9" "Refinery"

# Special Blocks
Create-ColoredBlock "arc_scenery" "#1E90FF" "Arc"
Create-ColoredBlock "pandora_grass" "#7CFC00" "Grass"
Create-ColoredBlock "pandora_bloom" "#FF69B4" "Bloom"

Write-Host "All block textures have been created!"
