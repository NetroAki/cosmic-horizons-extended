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

# Function to create a simple colored image
function Create-ColorImage($width, $height, $r, $g, $b, $outputPath) {
    $bmp = New-Object System.Drawing.Bitmap($width, $height)
    for ($x = 0; $x -lt $width; $x++) {
        for ($y = 0; $y -lt $height; $y++) {
            $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, $r, $g, $b))
        }
    }
    $bmp.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
}

# Main script
Add-Type -AssemblyName System.Drawing

foreach ($fluid in $fluids.GetEnumerator()) {
    $name = $fluid.Key
    $r, $g, $b = $fluid.Value
    
    Write-Host "Creating textures for $name (RGB: $r,$g,$b)..."
    
    # Create still texture (32x32)
    Create-ColorImage 32 32 $r $g $b "$texturesDir\${name}_still.png"
    
    # Create flowing texture (32x32 with slight color variation)
    $bmp = New-Object System.Drawing.Bitmap(32, 32)
    for ($x = 0; $x -lt 32; $x++) {
        for ($y = 0; $y -lt 32; $y++) {
            $vary = (Get-Random -Minimum -10 -Maximum 10)
            $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, 
                [Math]::Max(0, [Math]::Min(255, $r + $vary)),
                [Math]::Max(0, [Math]::Min(255, $g + $vary)),
                [Math]::Max(0, [Math]::Min(255, $b + $vary))
            ))
        }
    }
    $bmp.Save("$texturesDir\${name}_flowing.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    
    # Create bucket overlay (16x16 with a simple shape)
    $bmp = New-Object System.Drawing.Bitmap(16, 16)
    for ($x = 0; $x -lt 16; $x++) {
        for ($y = 0; $y -lt 16; $y++) {
            if (($x -gt 3 -and $x -lt 12 -and $y -gt 3 -and $y -lt 12) -or 
                ($x -gt 1 -and $x -lt 14 -and $y -gt 11 -and $y -lt 14)) {
                $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, $r, $g, $b))
            } else {
                $bmp.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
            }
        }
    }
    $bmp.Save("$bucketDir\${name}_bucket.png", [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
}

Write-Host "`nFluid textures have been generated in the following directories:"
Write-Host "- Still/flowing textures: $texturesDir"
Write-Host "- Bucket overlays: $bucketDir"
Write-Host "`nDone!"
