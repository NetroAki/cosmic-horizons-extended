# Create bucket content textures for fallback fuels using ImageMagick
# Usage: .\scripts\create_bucket_textures.ps1

# Check if ImageMagick is installed
if (-not (Get-Command magick -ErrorAction SilentlyContinue)) {
    Write-Error "ImageMagick is required. Please install it from https://imagemagick.org/"
    exit 1
}

# Create output directory
$outputDir = "forge/src/main/resources/assets/chex/textures/item"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Define fuel colors (RGB values) - same as in create_fuel_textures.ps1
$fuelColors = @{
    "kerosene" = "#8B8B83"  # Dark Gray
    "rp1" = "#8B8B00"      # Dark Yellow
    "lox" = "#87CEEB"      # Sky Blue
    "lh2" = "#E0FFFF"      # Light Cyan
    "dt_mix" = "#98FB98"   # Pale Green
    "he3_blend" = "#9370DB" # Medium Purple
    "exotic_mix" = "#FF69B4" # Hot Pink
}

# Create bucket content textures for each fuel
foreach ($fuel in $fuelColors.GetEnumerator()) {
    $name = $fuel.Key
    $color = $fuel.Value
    
    # Create a 16x16 texture with the fuel color and a slight gradient
    magick -size 16x16 gradient: -rotate 90 -fx "$color * (0.7 + 0.6 * u)" "$outputDir/${name}_bucket_content.png"
    
    Write-Host "Created bucket texture for $name"
}

Write-Host "All bucket textures created in $outputDir"
