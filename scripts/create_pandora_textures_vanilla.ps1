# Create Pandora block textures using Vanilla Minecraft assets and ImageMagick
# Usage: .\create_pandora_textures_vanilla.ps1

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

# Function to create a recolored texture
function Create-RecoloredTexture {
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

# Pandorite variants (stone-like)
Create-RecoloredTexture -outputName "pandorite_stone" -baseTexture "stone.png" -color "#8B7355" -text "Pandorite"
Create-RecoloredTexture -outputName "pandorite_cobbled" -baseTexture "cobblestone.png" -color "#8B7355" -text "Cobbled"
Create-RecoloredTexture -outputName "pandorite_bricks" -baseTexture "stone_bricks.png" -color "#8B7355" -text "Bricks"
Create-RecoloredTexture -outputName "chiseled_pandorite" -baseTexture "chiseled_stone_bricks.png" -color "#8B7355" -text "Chiseled"
Create-RecoloredTexture -outputName "pandorite_polished" -baseTexture "polished_andesite.png" -color "#A38660" -text "Polished"

# Pandorite pillar (log-like)
Create-RecoloredTexture -outputName "pandorite_pillar" -baseTexture "oak_log.png" -color "#8B7355" -text "Pillar"
Create-RecoloredTexture -outputName "pandorite_pillar_top" -baseTexture "oak_log_top.png" -color "#A38660" -text "Pillar Top"

# Spore Soil (dirt-like)
Create-RecoloredTexture -outputName "spore_soil" -baseTexture "dirt.png" -color "#5E3F3F" -text "Spore Soil"

# Biolume Moss (glowing moss)
Create-RecoloredTexture -outputName "biolume_moss" -baseTexture "moss_block.png" -color "#2A5C2A" -text "Biolume"

# Crystal Clad Pandorite (amethyst-like)
Create-RecoloredTexture -outputName "crystal_clad_pandorite" -baseTexture "amethyst_block.png" -color "#A0D8EF" -text "Crystal"

# Lumicoral (glowing coral)
Create-RecoloredTexture -outputName "lumicoral" -baseTexture "brain_coral_block.png" -color "#FF69B4" -text "Lumicoral"

# Pandoran Crystal Cluster (glowing crystal)
Create-RecoloredTexture -outputName "pandoran_crystal_cluster" -baseTexture "amethyst_cluster.png" -color "#E6E6FA" -text "Crystal"

Write-Host "All Pandora block textures have been created using Vanilla assets!"
