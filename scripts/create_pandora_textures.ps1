# Create Pandora block textures using ImageMagick
# Usage: .\create_pandora_textures.ps1

# Check if ImageMagick is installed
if (-not (Get-Command magick -ErrorAction SilentlyContinue)) {
    Write-Error "ImageMagick is required. Please install it from https://imagemagick.org/"
    exit 1
}

# Create textures directory if it doesn't exist
$textureDir = "C:\Users\zacpa\intellij\Cosmic_Horizons_Expanded\forge\src\main\resources\assets\chex\textures\block"
if (-not (Test-Path $textureDir)) {
    New-Item -ItemType Directory -Path $textureDir -Force | Out-Null
}

# Function to create a simple texture with text
function Create-Texture {
    param (
        [string]$name,
        [string]$color,
        [string]$text,
        [string]$textColor = "white"
    )
    
    $path = Join-Path $textureDir "${name}.png"
    magick -size 64x64 -background $color -fill $textColor -gravity center -pointsize 20 label:$text $path
    Write-Host "Created texture: $path"
}

# Pandorite variants
Create-Texture -name "pandorite_stone" -color "#8B7355" -text "Pandorite"
Create-Texture -name "polished_pandorite" -color "#A38660" -text "Polished"
Create-Texture -name "pandorite_bricks" -color "#8B7355" -text "Bricks" -textColor "#000000"
Create-Texture -name "chiseled_pandorite" -color "#8B7355" -text "Chiseled"
Create-Texture -name "pandorite_pillar_top" -color "#A38660" -text "Pillar Top"
Create-Texture -name "pandorite_pillar" -color "#8B7355" -text "Pillar"

# Spore Soil
Create-Texture -name "spore_soil" -color "#5E3F3F" -text "Spore Soil"

# Biolume Moss
Create-Texture -name "biolume_moss" -color "#2A5C2A" -text "Biolume"

# Crystal Clad Pandorite
Create-Texture -name "crystal_clad_pandorite" -color "#A0D8EF" -text "Crystal"

# Lumicoral
Create-Texture -name "lumicoral" -color "#FF69B4" -text "Lumicoral"

# Pandoran Crystal Cluster
Create-Texture -name "pandoran_crystal_cluster" -color "#E6E6FA" -text "Crystal"

Write-Host "All Pandora block textures have been created!"
