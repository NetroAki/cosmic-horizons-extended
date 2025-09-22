# Create output directory if it doesn't exist
$outputDir = "common/src/main/resources/assets/chex/textures/block"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Base textures from Minecraft assets
$basePath = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/block"

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

# 1. Vent Basalt (based on basalt)
New-RecoloredTexture -source "basalt_side.png" -output "vent_basalt.png" -color "#1a1a2e" -brightness -10 -saturation 1.2

# 2. Polished Vent Basalt (based on polished basalt)
New-RecoloredTexture -source "polished_basalt_side.png" -output "polished_vent_basalt.png" -color "#1a1a2e" -brightness -5 -saturation 1.1

# 3. Manganese Nodule (based on nether_quartz_ore)
New-RecoloredTexture -source "nether_quartz_ore.png" -output "manganese_nodule.png" -color "#6b6b8a" -brightness 10 -saturation 0.8

# 4. Luminous Kelp (based on kelp_plant)
New-RecoloredTexture -source "kelp_plant.png" -output "luminous_kelp.png" -color "#4cff00" -brightness 30 -saturation 1.5

# 5. Ice Shelf (based on blue_ice)
New-RecoloredTexture -source "blue_ice.png" -output "ice_shelf.png" -color "#a8d8ea" -brightness 20 -saturation 0.9

# Create item textures directory
$itemOutputDir = "common/src/main/resources/assets/chex/textures/item"
New-Item -ItemType Directory -Force -Path $itemOutputDir | Out-Null

# Copy block textures to item textures
Copy-Item -Path "$outputDir/vent_basalt.png" -Destination "$itemOutputDir/vent_basalt.png" -Force
Copy-Item -Path "$outputDir/polished_vent_basalt.png" -Destination "$itemOutputDir/polished_vent_basalt.png" -Force
Copy-Item -Path "$outputDir/manganese_nodule.png" -Destination "$itemOutputDir/manganese_nodule.png" -Force
Copy-Item -Path "$outputDir/ice_shelf.png" -Destination "$itemOutputDir/ice_shelf.png" -Force

# Special case for luminous kelp item
magick convert "$outputDir/luminous_kelp.png" -resize 16x16 "$itemOutputDir/luminous_kelp.png"

Write-Host "All textures have been created in $outputDir"
