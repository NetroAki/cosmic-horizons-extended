# Script to generate luminous kelp plant textures for all growth stages

Add-Type -AssemblyName System.Drawing

$textureDir = "common\src\main\resources\assets\chex\textures\block"

# Create textures directory if it doesn't exist
if (-not (Test-Path -Path $textureDir)) {
    New-Item -ItemType Directory -Path $textureDir -Force | Out-Null
}

# Function to create a kelp plant texture
function Create-KelpTexture($age, $glowing) {
    $suffix = if ($glowing) { "_glow" } else { "" }
    $textureName = "luminous_kelp_plant${age}${suffix}.png"
    $texturePath = Join-Path $textureDir $textureName
    
    # Base color - green for normal, brighter green for glowing
    $baseColor = if ($glowing) { [System.Drawing.Color]::FromArgb(64, 255, 64) } 
                 else { [System.Drawing.Color]::FromArgb(32, 128, 32) }
    
    # Calculate height based on age (0-15)
    $height = [Math]::Max(1, [Math]::Floor(16 * ($age + 1) / 16))
    
    # Create bitmap
    $bitmap = New-Object System.Drawing.Bitmap 16, 16
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Make background transparent
    $graphics.Clear([System.Drawing.Color]::Transparent)
    
    # Draw kelp stem
    $stemWidth = 6
    $stemX = (16 - $stemWidth) / 2
    $stemBrush = New-Object System.Drawing.SolidBrush $baseColor
    $graphics.FillRectangle($stemBrush, $stemX, 16 - $height, $stemWidth, $height)
    
    # Add some variation to make it look more natural
    $rand = New-Object System.Random
    for ($i = 0; $i -lt 5; $i++) {
        $x = $rand.Next($stemX, $stemX + $stemWidth)
        $y = $rand.Next(16 - $height, 16)
        $vary = $rand.Next(-20, 20)
        $r = [Math]::Min(255, [Math]::Max(0, $baseColor.R + $vary))
        $g = [Math]::Min(255, [Math]::Max(0, $baseColor.G + $vary))
        $b = [Math]::Min(255, [Math]::Max(0, $baseColor.B + $vary))
        $pixelColor = [System.Drawing.Color]::FromArgb($r, $g, $b)
        $bitmap.SetPixel($x, $y, $pixelColor)
    }
    
    # Add some glow effect for the glowing variant
    if ($glowing) {
        $glowColor = [System.Drawing.Color]::FromArgb(64, 255, 64, 128)
        $glowBrush = New-Object System.Drawing.SolidBrush $glowColor
        $graphics.FillRectangle($glowBrush, $stemX - 1, 16 - $height - 1, $stemWidth + 2, $height + 2)
    }
    
    # Save the texture
    $bitmap.Save($texturePath)
    $bitmap.Dispose()
    $graphics.Dispose()
    
    Write-Host "Created texture: $textureName"
}

# Generate textures for all growth stages (0-15) for both glowing and non-glowing states
for ($i = 0; $i -le 15; $i++) {
    Create-KelpTexture -age $i -glowing $false
    Create-KelpTexture -age $i -glowing $true
}

Write-Host "All kelp plant textures have been generated."
