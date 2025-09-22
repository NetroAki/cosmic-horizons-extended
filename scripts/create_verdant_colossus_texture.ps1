# Create a 128x128 placeholder texture for the Verdant Colossus
Add-Type -AssemblyName System.Drawing

$width = 128
$height = 128
$bitmap = New-Object System.Drawing.Bitmap($width, $height)

# Create a graphics object for drawing
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)

# Fill with a transparent background
$graphics.Clear([System.Drawing.Color]::Transparent)

# Define colors
$barkDark = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(85, 60, 40))  # Dark brown
$barkLight = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(120, 85, 55))  # Light brown
$leafDark = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(30, 100, 30))  # Dark green
$leafLight = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(60, 150, 60))  # Light green
$glow = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(100, 255, 100, 100))  # Glowing green

# Draw the main body (tree trunk like)
$trunkPoints = @(
    [System.Drawing.Point]::new(64, 20),
    [System.Drawing.Point]::new(84, 60),
    [System.Drawing.Point]::new(84, 120),
    [System.Drawing.Point]::new(64, 128),
    [System.Drawing.Point]::new(44, 120),
    [System.Drawing.Point]::new(44, 60)
)
$graphics.FillPolygon($barkDark, $trunkPoints)

# Add bark details
for ($i = 0; $i -lt 5; $i++) {
    $x = 44 + (Get-Random -Minimum 0 -Maximum 40)
    $y = 40 + (Get-Random -Minimum 0 -Maximum 90)
    $width = 5 + (Get-Random -Minimum 0 -Maximum 10)
    $height = 10 + (Get-Random -Minimum 0 -Maximum 20)
    $graphics.FillRectangle($barkLight, $x, $y, $width, $height)
}

# Add leaf clusters
$leafClusters = @(
    @{X=30; Y=30; Size=40; Color=$leafDark},
    @{X=70; Y=25; Size=35; Color=$leafLight},
    @{X=20; Y=60; Size=30; Color=$leafDark},
    @{X=80; Y=65; Size=25; Color=$leafLight}
)

foreach ($cluster in $leafClusters) {
    $graphics.FillEllipse($cluster.Color, $cluster.X, $cluster.Y, $cluster.Size, $cluster.Size)
}

# Add glowing runes (magical effects)
$glowPoints = @(
    @{X=60; Y=40; Size=8},
    @{X=70; Y=90; Size=6},
    @{X=40; Y=80; Size=7},
    @{X=80; Y=110; Size=5}
)

foreach ($point in $glowPoints) {
    $graphics.FillEllipse($glow, $point.X, $point.Y, $point.Size, $point.Size)
}

# Create the output directory if it doesn't exist
$outputDir = "common/src/main/resources/assets/chex/textures/entity"
if (-not (Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir -Force | Out-Null
}

# Save the normal texture
$outputPath = Join-Path $outputDir "verdant_colossus.png"
$bitmap.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

# Create and save the enraged version (brighter and more intense)
$graphics.FillRectangle([System.Drawing.Brushes]::Transparent, 0, 0, $width, $height)
$graphics.FillPolygon($barkDark, $trunkPoints)

# More intense colors for enraged
$enragedLeafDark = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(20, 180, 20))
$enragedLeafLight = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(100, 255, 100))
$enragedGlow = [System.Drawing.Brush]::new([System.Drawing.Color]::FromArgb(150, 255, 150, 150))

foreach ($cluster in $leafClusters) {
    $color = if ($cluster.Color -eq $leafDark) { $enragedLeafDark } else { $enragedLeafLight }
    $graphics.FillEllipse($color, $cluster.X, $cluster.Y, $cluster.Size, $cluster.Size)
}

foreach ($point in $glowPoints) {
    $graphics.FillEllipse($enragedGlow, $point.X, $point.Y, $point.Size + 2, $point.Size + 2)
}

$enragedPath = Join-Path $outputDir "verdant_colossus_enraged.png"
$bitmap.Save($enragedPath, [System.Drawing.Imaging.ImageFormat]::Png)

# Clean up
$graphics.Dispose()
$bitmap.Dispose()

Write-Host "Verdant Colossus textures created at:"
Write-Host "- Normal: $outputPath"
Write-Host "- Enraged: $enragedPath"
