# Create output directories
$texturesDir = "src/main/resources/assets/chex/textures/block/fluid"
$bucketDir = "src/main/resources/assets/chex/textures/item/fluid"
New-Item -ItemType Directory -Force -Path $texturesDir | Out-Null
New-Item -ItemType Directory -Force -Path $bucketDir | Out-Null

# Define fluids and their colors (RGB format)
$fluids = @{
    "kerosene" = @(139, 139, 131)  # Dark gray
    "rp1" = @(74, 74, 74)          # Very dark gray
    "lox" = @(135, 206, 235)       # Light blue
    "lh2" = @(230, 243, 255)       # Very light blue
    "dt_mix" = @(0, 255, 0)        # Bright green
    "he3_blend" = @(255, 165, 0)   # Orange
    "exotic_mix" = @(153, 50, 204) # Purple
}

# Create noise texture for effects
$noiseFile = "$env:TEMP\fluid_noise.png"
& magick -size 64x64 xc:gray -colorspace gray -blur 0x0.5 -normalize -evaluate sin 3.14 -auto-level $noiseFile

foreach ($fluid in $fluids.GetEnumerator()) {
    $name = $fluid.Key
    $r, $g, $b = $fluid.Value
    $color = "rgb($r,$g,$b)"
    
    Write-Host "Creating textures for $name (color: $color)..."
    
    # Create still texture (32x32)
    & magick -size 32x32 xc:none `
        -fill $color `
        -draw "rectangle 0,0 31,31" `
        -blur 0x0.5 `
        -alpha on `
        -channel A -evaluate multiply 0.8 +channel `
        "$texturesDir\${name}_still.png"
    
    # Create flowing texture (32x32 with noise)
    & magick -size 32x32 xc:none `
        -fill $color `
        -draw "rectangle 0,0 31,31" `
        -blur 0x0.5 `
        -motion-blur 0x8+45 `
        -alpha on `
        -channel A -evaluate multiply 0.7 +channel `
        "$texturesDir\${name}_flowing.png"
    
    # Create bucket overlay (16x16)
    & magick -size 16x16 xc:none `
        -fill $color `
        -draw "rectangle 4,4 12,14" `
        -blur 0x0.5 `
        -alpha on `
        -channel A -evaluate multiply 0.8 +channel `
        "$bucketDir\${name}_bucket.png"
}

# Clean up
if (Test-Path $noiseFile) {
    Remove-Item $noiseFile
}

Write-Host "`nFluid textures have been generated in:"
Write-Host "- Still/flowing textures: $texturesDir"
Write-Host "- Bucket overlays: $bucketDir"
Write-Host "`nDone!"
