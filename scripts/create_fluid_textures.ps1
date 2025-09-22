# Create textures directory if it doesn't exist
$texturesDir = "src/main/resources/assets/chex/textures/block/fluid"
New-Item -ItemType Directory -Force -Path $texturesDir | Out-Null

# Define fluids and their base colors
$fluids = @{
    "kerosene" = "#8B8B83"  # Dark gray
    "rp1" = "#4A4A4A"       # Very dark gray (almost black)
    "lox" = "#87CEEB"       # Light blue
    "lh2" = "#E6F3FF"       # Very light blue
    "dt_mix" = "#00FF00"    # Bright green
    "he3_blend" = "#FFA500" # Orange
    "exotic_mix" = "#9932CC" # Purple
}

# Copy and recolor water textures for each fluid
foreach ($fluid in $fluids.GetEnumerator()) {
    $name = $fluid.Key
    $color = $fluid.Value
    
    # Create still texture (based on water)
    $stillSource = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/block/water_still.png"
    $stillDest = "$texturesDir/${name}_still.png"
    
    # Create flowing texture (based on water)
    $flowingSource = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/block/water_flow.png"
    $flowingDest = "$texturesDir/${name}_flowing.png"
    
    # Create bucket texture (based on water bucket)
    $bucketDir = "src/main/resources/assets/chex/textures/item/fluid"
    New-Item -ItemType Directory -Force -Path $bucketDir | Out-Null
    $bucketDest = "$bucketDir/${name}_bucket.png"
    
    # Copy the files (in a real scenario, we'd recolor them)
    Write-Host "Creating textures for $name (color: $color)..."
    
    # For now, just create placeholder files with the color in the name
    # In a real scenario, you'd want to use an image processing library to recolor the textures
    "Placeholder for ${name}_still.png (color: $color)" | Out-File -FilePath $stillDest -NoNewline
    "Placeholder for ${name}_flowing.png (color: $color)" | Out-File -FilePath $flowingDest -NoNewline
    "Placeholder for ${name}_bucket.png (color: $color)" | Out-File -FilePath $bucketDest -NoNewline
}

Write-Host "`nNote: This script created placeholder text files instead of actual PNG images.`n"
Write-Host "For actual textures, you'll need to create proper PNG files with the following names and colors:"
foreach ($fluid in $fluids.GetEnumerator()) {
    $name = $fluid.Key
    $color = $fluid.Value
    Write-Host "- $texturesDir/${name}_still.png (color: $color)"
    Write-Host "- $texturesDir/${name}_flowing.png (color: $color)"
    Write-Host "- $texturesDir/../item/fluid/${name}_bucket.png (color: $color)"
}

Write-Host "`nFor best results, use an image editor to recolor the water textures with the specified colors."
