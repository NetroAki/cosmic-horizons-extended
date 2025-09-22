# Create output directories
$blockDir = "common/src/main/resources/assets/chex/textures/entity/luminfish"
$itemDir = "common/src/main/resources/assets/chex/textures/item"

New-Item -ItemType Directory -Force -Path $blockDir | Out-Null
New-Item -ItemType Directory -Force -Path $itemDir | Out-Null

# Base textures from Minecraft assets
$basePath = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/entity/fish"

# Create Luminfish texture (based on tropical fish)
$luminfishBase = "$basePath/tropical_a.png"
$outputPath = "$blockDir/luminfish.png"

# Apply blue-green tint and add glow effect
magick convert $luminfishBase `
    -modulate 100,150,100 `
    -fill "#00FFAA" -tint 30 `
    -brightness-contrast 10,10 `
    $outputPath

# Create glow layer for emissive effect
magick convert $luminfishBase `
    -alpha extract `
    -threshold 50% `
    -transparent black `
    -fill "#AAFFEE" -colorize 100 `
    -blur 0x2 `
    -alpha on `
    "$blockDir/luminfish_glow.png"

# Create spawn egg (base on tropical fish spawn egg)
$eggBase = "InventivetalentDev-minecraft-assets-af628ec/assets/minecraft/textures/item/tropical_fish_bucket.png"
$outputEgg = "$itemDir/luminfish_spawn_egg.png"

# Apply blue-green tint to the egg
magick convert $eggBase `
    -modulate 100,120,100 `
    -fill "#00AA88" -tint 40 `
    -brightness-contrast 5,10 `
    $outputEgg

Write-Host "Luminfish textures created successfully!"
