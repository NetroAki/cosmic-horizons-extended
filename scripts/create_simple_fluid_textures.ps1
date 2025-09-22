# Create output directories
$texturesDir = "src/main/resources/assets/chex/textures/block/fluid"
$bucketDir = "src/main/resources/assets/chex/textures/item/fluid"
New-Item -ItemType Directory -Force -Path $texturesDir | Out-Null
New-Item -ItemType Directory -Force -Path $bucketDir | Out-Null

# Define fluids and their colors
$fluids = @{
    "kerosene" = [System.Drawing.Color]::FromArgb(139, 139, 131)  # Dark gray
    "rp1" = [System.Drawing.Color]::FromArgb(74, 74, 74)          # Very dark gray
    "lox" = [System.Drawing.Color]::FromArgb(135, 206, 235)       # Light blue
    "lh2" = [System.Drawing.Color]::FromArgb(230, 243, 255)       # Very light blue
    "dt_mix" = [System.Drawing.Color]::FromArgb(0, 255, 0)        # Bright green
    "he3_blend" = [System.Drawing.Color]::FromArgb(255, 165, 0)   # Orange
    "exotic_mix" = [System.Drawing.Color]::FromArgb(153, 50, 204) # Purple
}

# Load Windows Forms assembly
Add-Type -AssemblyName System.Drawing

foreach ($fluid in $fluids.GetEnumerator()) {
    $name = $fluid.Key
    $color = $fluid.Value
    
    Write-Host "Creating textures for $name (color: $color)..."
    
    # Create still texture (32x32)
    $bitmap = New-Object System.Drawing.Bitmap(32, 32)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $brush = New-Object System.Drawing.SolidBrush($color)
    $graphics.FillRectangle($brush, 0, 0, 32, 32)
    $bitmap.Save("$texturesDir\${name}_still.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $graphics.Dispose()
    $bitmap.Dispose()
    
    # Create flowing texture (32x32 with gradient)
    $bitmap = New-Object System.Drawing.Bitmap(32, 32)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $gradientBrush = New-Object System.Drawing.Drawing2D.LinearGradientBrush(
        [System.Drawing.Point]::Empty,
        [System.Drawing.Point]::new(32, 32),
        [System.Drawing.Color]::FromArgb(200, $color.R, $color.G, $color.B),
        [System.Drawing.Color]::FromArgb(150, $color.R, $color.G, $color.B)
    )
    $graphics.FillRectangle($gradientBrush, 0, 0, 32, 32)
    $bitmap.Save("$texturesDir\${name}_flowing.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $gradientBrush.Dispose()
    $graphics.Dispose()
    $bitmap.Dispose()
    
    # Create bucket overlay (16x16)
    $bitmap = New-Object System.Drawing.Bitmap(16, 16)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    
    # Draw a simple bucket shape
    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $path.AddArc(2, 2, 12, 8, 180, 180)
    $path.AddLine(14, 10, 12, 14)
    $path.AddLine(12, 14, 4, 14)
    $path.AddLine(2, 10, 2, 10)
    $path.CloseFigure()
    
    $brush = New-Object System.Drawing.SolidBrush($color)
    $graphics.FillPath($brush, $path)
    $bitmap.Save("$bucketDir\${name}_bucket.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $brush.Dispose()
    $path.Dispose()
    $graphics.Dispose()
    $bitmap.Dispose()
}

Write-Host "`nFluid textures have been generated in the following directories:"
Write-Host "- Still/flowing textures: $texturesDir"
Write-Host "- Bucket overlays: $bucketDir"
Write-Host "`nDone!"
