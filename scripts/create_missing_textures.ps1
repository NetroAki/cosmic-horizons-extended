# Create output directory if it doesn't exist
$outputDir = "common/src/main/resources/assets/chex/textures/item"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Base textures from Minecraft assets
$basePath = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/item"

# Function to create a recolored texture
function New-RecoloredTexture {
    param (
        [string]$source,
        [string]$output,
        [string]$color,
        [int]$brightness = 0,
        [float]$saturation = 1.0
    )
    
    $sourcePath = Join-Path $basePath $source
    $outputPath = Join-Path $outputDir $output
    
    # Apply color tint and adjustments
    magick convert $sourcePath -modulate 100,$(100*$saturation),100 -fill $color -tint 50 -brightness-contrast $brightness $outputPath
    
    Write-Host "Created texture: $output"
}

# 1. Luminous Kelp Bucket (based on water bucket)
New-RecoloredTexture -source "water_bucket.png" -output "luminous_kelp_bucket.png" -color "#4cff00" -brightness 20 -saturation 1.5

# 2. Diving Helmet (based on turtle helmet)
New-RecoloredTexture -source "turtle_helmet.png" -output "diving_helmet.png" -color "#3a7ca5" -brightness 10 -saturation 1.2

# 3. Diving Chestplate (based as chainmail chestplate)
New-RecoloredTexture -source "chainmail_chestplate.png" -output "diving_chestplate.png" -color "#3a7ca5" -brightness 10 -saturation 1.2

# 4. Diving Leggings (based on chainmail leggings)
New-RecoloredTexture -source "chainmail_leggings.png" -output "diving_leggings.png" -color "#3a7ca5" -brightness 10 -saturation 1.2

# 5. Diving Boots (based on chainmail boots)
New-RecoloredTexture -source "chainmail_boots.png" -output "diving_boots.png" -color "#3a7ca5" -brightness 10 -saturation 1.2

# 6. Oxygen Tank (based on water bucket with different colors)
New-RecoloredTexture -source "water_bucket.png" -output "oxygen_tank.png" -color "#a3c1da" -brightness 20 -saturation 0.8

Write-Host "All missing item textures have been created in $outputDir"
