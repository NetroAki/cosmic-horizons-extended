# Create bucket textures directory if it doesn't exist
$bucketTexturesDir = "src/main/resources/assets/chex/textures/item/fluid"
New-Item -ItemType Directory -Force -Path $bucketTexturesDir | Out-Null

# Define fluids and their colors
$fluids = @(
    @{Name="kerosene"; Color="#8B8B83"},
    @{Name="rp1"; Color="#4A4A4A"},
    @{Name="lox"; Color="#87CEEB"},
    @{Name="lh2"; Color="#E6F3FF"},
    @{Name="dt_mix"; Color="#00FF00"},
    @{Name="he3_blend"; Color="#FFA500"},
    @{Name="exotic_mix"; Color="#9932CC"}
)

# Create a simple colored square image for each bucket overlay
foreach ($fluid in $fluids) {
    $name = $fluid.Name
    $color = $fluid.Color
    
    # Create bucket overlay texture
    $bucketPath = "$bucketTexturesDir/${name}_bucket.png"
    if (-not (Test-Path $bucketPath)) {
        Write-Host "Creating bucket overlay for $name..."
        # This is a placeholder - in a real scenario, you'd use an image processing library
        # or create proper textures in an image editor
        "Placeholder for ${name}_bucket.png with color $color" | Out-File -FilePath $bucketPath -NoNewline
    }
}

Write-Host "`nNote: This script created placeholder text files instead of actual PNG images.`n"
Write-Host "For actual bucket overlay textures, you'll need to create proper PNG files with the following names:"
foreach ($fluid in $fluids) {
    Write-Host "- $bucketTexturesDir/$($fluid.Name)_bucket.png"
}
Write-Host "`nThe suggested colors for each fluid are included in the filenames above."
