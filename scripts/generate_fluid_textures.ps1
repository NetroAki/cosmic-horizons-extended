# Create output directories
$texturesDir = "src/main/resources/assets/chex/textures/block/fluid"
$bucketDir = "src/main/resources/assets/chex/textures/item/fluid"
New-Item -ItemType Directory -Force -Path $texturesDir | Out-Null
New-Item -ItemType Directory -Force -Path $bucketDir | Out-Null

# Define fluids and their colors
$fluids = @(
    @{Name="kerosene"; Color="#8B8B83"},  # Dark gray
    @{Name="rp1"; Color="#4A4A4A"},       # Very dark gray (almost black)
    @{Name="lox"; Color="#87CEEB"},       # Light blue
    @{Name="lh2"; Color="#E6F3FF"},       # Very light blue
    @{Name="dt_mix"; Color="#00FF00"},    # Bright green
    @{Name="he3_blend"; Color="#FFA500"}, # Orange
    @{Name="exotic_mix"; Color="#9932CC"} # Purple
)

# Create a noise texture for the fluid effect
$noiseFile = "$env:TEMP\fluid_noise.png"
magick -size 64x64 xc:gray -colorspace gray -blur 0x0.5 -normalize -evaluate sin 3.14 -auto-level $noiseFile

foreach ($fluid in $fluids) {
    $name = $fluid.Name
    $color = $fluid.Color
    
    Write-Host "Creating textures for $name (color: $color)..."
    
    # Create still texture
    magick -size 32x32 xc:none \
        -fill $color -tile $noiseFile -compose Overlay -composite \
        -blur 0x0.5 \
        -alpha on -channel A -evaluate multiply 0.8 +channel \
        "$texturesDir\${name}_still.png"
    
    # Create flowing texture (slightly distorted)
    magick -size 64x32 xc:none \
        -fill $color -tile $noiseFile -compose Overlay -composite \
        -blur 0x0.5 -motion-blur 0x8+45 \
        -alpha on -channel A -evaluate multiply 0.7 +channel \
        -resize 32x32 \
        "$texturesDir\${name}_flowing.png"
    
    # Create bucket overlay (simplified version)
    magick -size 16x16 xc:none \
        -fill $color -draw 'rectangle 4,4 12,14' \
        -blur 0x0.5 \
        -alpha on -channel A -evaluate multiply 0.8 +channel \
        "$bucketDir\${name}_bucket.png"
}

Write-Host "`nFluid textures have been generated in the following directories:"
Write-Host "- Still/flowing textures: $texturesDir"
Write-Host "- Bucket overlays: $bucketDir`n"

# Clean up temporary files
if (Test-Path $noiseFile) {
    Remove-Item $noiseFile
}

Write-Host "Done!"

foreach ($fluid in $fluids) {
    Write-Host "- $texturesDir/$($fluid.Name)_still.png"
    Write-Host "- $texturesDir/$($fluid.Name)_flowing.png"
}
Write-Host "`nThe suggested colors for each fluid are included in the filenames above."
