# Create colored placeholder textures for fuel buckets
$colors = @{
    "dt_mix_bucket" = "#FFA500"  # Orange
    "he3_blend_bucket" = "#00FF00"  # Green
    "exotic_mix_bucket" = "#FF00FF"  # Magenta
}

$textureDir = "forge/src/main/resources/assets/chex/textures/item"

# Create 16x16 colored square textures
foreach ($bucket in $colors.GetEnumerator()) {
    $bitmap = New-Object System.Drawing.Bitmap(16, 16)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $color = [System.Drawing.ColorTranslator]::FromHtml($bucket.Value)
    $brush = New-Object System.Drawing.SolidBrush($color)
    $graphics.FillRectangle($brush, 0, 0, 16, 16)
    
    # Save as PNG
    $bitmap.Save("$textureDir\$($bucket.Key).png")
    
    # Clean up
    $graphics.Dispose()
    $bitmap.Dispose()
}

Write-Host "Created placeholder textures for fuel buckets in $textureDir"
