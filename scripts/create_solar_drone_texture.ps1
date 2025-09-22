# Create a 64x64 placeholder texture for the Solar Engineer Drone
$width = 64
$height = 64
$bitmap = New-Object System.Drawing.Bitmap($width, $height)

# Create a graphics object for drawing
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)

# Fill with transparent background
$graphics.Clear([System.Drawing.Color]::Transparent)

# Define colors
$gold = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(255, 215, 0)) # Gold
$darkGold = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(205, 173, 0)) # Dark Gold
$yellow = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(255, 255, 0)) # Yellow
$orange = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(255, 165, 0)) # Orange
$black = [System.Drawing.Brush]::new([System.Drawing.Color]::Black)
$white = [System.Drawing.Brush]::new([System.Drawing.Color]::White)

# Draw main body (hexagonal shape)
$points = @(
    [System.Drawing.Point]::new(32, 10),
    [System.Drawing.Point]::new(54, 22),
    [System.Drawing.Point]::new(54, 42),
    [System.Drawing.Point]::new(32, 54),
    [System.Drawing.Point]::new(10, 42),
    [System.Drawing.Point]::new(10, 22)
)
$graphics.FillPolygon($gold, $points)

# Add some details
$graphics.FillEllipse($yellow, 24, 24, 16, 16) # Center core

# Add some lines for tech details
$pen = [System.Drawing.Pen]::new($black, 1)
$graphics.DrawLine($pen, 24, 16, 40, 16) # Top line
$graphics.DrawLine($pen, 16, 32, 24, 24) # Top-left line
$graphics.DrawLine($pen, 40, 24, 48, 32) # Top-right line
$graphics.DrawLine($pen, 16, 48, 24, 56) # Bottom-left line
$graphics.DrawLine($pen, 40, 56, 48, 48) # Bottom-right line

# Add some orange highlights
$graphics.FillRectangle($orange, 28, 12, 8, 4) # Top
$graphics.FillRectangle($orange, 28, 48, 8, 4) # Bottom
$graphics.FillRectangle($orange, 12, 28, 4, 8) # Left
$graphics.FillRectangle($orange, 48, 28, 4, 8) # Right

# Save the image
$outputPath = Join-Path $PSScriptRoot "..\common\src\main\resources\assets\chex\textures\entity\solar_engineer_drone.png"
$bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

# Clean up
$graphics.Dispose()
$bitmap.Dispose()

Write-Host "Placeholder texture created at: $outputPath"
