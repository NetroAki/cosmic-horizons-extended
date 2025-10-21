# Create simple colored textures for Aqua Mundus blocks

$textureDir = "common\src\main\resources\assets\chex\textures\block"

# Create textures directory if it doesn't exist
if (-not (Test-Path -Path $textureDir)) {
    New-Item -ItemType Directory -Path $textureDir -Force | Out-Null
}

# Function to create a simple colored texture
function Create-Texture($name, $color) {
    $bitmap = New-Object System.Drawing.Bitmap 16, 16
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    $brush = New-Object System.Drawing.SolidBrush $color
    $graphics.FillRectangle($brush, 0, 0, 16, 16)
    
    # Add some simple variation
    $rand = New-Object System.Random
    for ($i = 0; $i -lt 10; $i++) {
        $x = $rand.Next(0, 16)
        $y = $rand.Next(0, 16)
        $vary = $rand.Next(-20, 20)
        $r = [Math]::Min(255, [Math]::Max(0, $color.R + $vary))
        $g = [Math]::Min(255, [Math]::Max(0, $color.G + $vary))
        $b = [Math]::Min(255, [Math]::Max(0, $color.B + $vary))
        $pixelColor = [System.Drawing.Color]::FromArgb($r, $g, $b)
        $bitmap.SetPixel($x, $y, $pixelColor)
    }
    
    $bitmap.Save("${textureDir}\${name}.png")
    $bitmap.Dispose()
    $graphics.Dispose()
}

# Create textures
Add-Type -AssemblyName System.Drawing

# Vent Basalt - Dark gray with blue tint
Create-Texture "vent_basalt" ([System.Drawing.Color]::FromArgb(58, 58, 74))

# Manganese Nodule - Silver-gray
Create-Texture "manganese_nodule" ([System.Drawing.Color]::FromArgb(160, 160, 180))

# Ice Shelf - Light blue
Create-Texture "ice_shelf_block" ([System.Drawing.Color]::FromArgb(192, 224, 240))

# Luminous Kelp - Green
Create-Texture "luminous_kelp" ([System.Drawing.Color]::FromArgb(32, 128, 32))

Write-Host "Simple placeholder textures generated in ${textureDir}"
