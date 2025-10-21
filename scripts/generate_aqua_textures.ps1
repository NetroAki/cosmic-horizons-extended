# Generate placeholder textures for Aqua Mundus blocks
# Requires ImageMagick to be installed and in PATH

$textureDir = "common\src\main\resources\assets\chex\textures\block"

# Create textures directory if it doesn't exist
if (-not (Test-Path -Path $textureDir)) {
    New-Item -ItemType Directory -Path $textureDir -Force | Out-Null
}

# Vent Basalt - Dark gray with blue specks
magick -size 16x16 xc:#3a3a4a \
    -seed 42 -spread 2 -paint 1 -blur 0x0.5 \
    -fill "#4a5a8a" -draw "point 4,4" -draw "point 12,4" -draw "point 4,12" -draw "point 12,12" \
    "${textureDir}\vent_basalt.png"

# Manganese Nodule - Silver-gray with metallic specks
magick -size 16x16 xc:#8a8a9a \
    -seed 123 -spread 2 -paint 1 -blur 0x0.5 \
    -fill "#ccccdd" -draw "point 8,8" -fill "#777788" -draw "point 12,4" -draw "point 4,12" \
    "${textureDir}\manganese_nodule.png"

# Ice Shelf - Light blue with subtle noise
magick -size 16x16 xc:#c0e0f0 \
    -seed 456 -spread 1 -paint 1 -blur 0x0.3 \
    -fill "#d0f0ff" -draw "point 8,8" -draw "point 4,4" -draw "point 12,12" \
    "${textureDir}\ice_shelf_block.png"

# Luminous Kelp - Greenish with glow effect
magick -size 16x16 xc:#204020 \
    -seed 789 -spread 1 -paint 1 -blur 0x0.5 \
    -fill "#40c040" -draw "point 8,8" -draw "point 6,6" -draw "point 10,10" \
    "${textureDir}\luminous_kelp.png"

Write-Host "Placeholder textures generated in ${textureDir}"
