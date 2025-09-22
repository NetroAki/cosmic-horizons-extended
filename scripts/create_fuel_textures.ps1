# Create placeholder textures for fallback fuels using ImageMagick
# Usage: .\scripts\create_fuel_textures.ps1

# Check if ImageMagick is installed
if (-not (Get-Command magick -ErrorAction SilentlyContinue)) {
    Write-Error "ImageMagick is required. Please install it from https://imagemagick.org/"
    exit 1
}

# Create output directory
$outputDir = "forge/src/main/resources/assets/chex/textures/fluid"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Define fuel colors (RGB values)
$fuelColors = @{
    "kerosene" = "#8B8B83"  # Dark Gray
    "rp1" = "#8B8B00"      # Dark Yellow
    "lox" = "#87CEEB"      # Sky Blue
    "lh2" = "#E0FFFF"      # Light Cyan
    "dt_mix" = "#98FB98"   # Pale Green
    "he3_blend" = "#9370DB" # Medium Purple
    "exotic_mix" = "#FF69B4" # Hot Pink
}

# Create still and flowing textures for each fuel
foreach ($fuel in $fuelColors.GetEnumerator()) {
    $name = $fuel.Key
    $color = $fuel.Value
    
    # Create still texture (32x32)
    magick -size 32x32 xc:$color "$outputDir/${name}_still.png"
    
    # Create flowing texture (32x32 with gradient for animation)
    magick -size 32x32 gradient: -rotate 90 -fx "$color * u" "$outputDir/${name}_flowing.png"
    
    Write-Host "Created textures for $name"
}

Write-Host "All fuel textures created in $outputDir"
