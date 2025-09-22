# Kepler Tree Asset Generator
# This script creates procedural tree assets for Kepler biomes

# Configuration
$baseDir = "$PSScriptRoot/.."
$outputDir = "$baseDir/common/src/main/resources/assets/chex/models/block/kepler_trees"
$textureDir = "$baseDir/common/src/main/resources/assets/chex/textures/block/kepler"

# Create directories if they don't exist
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
New-Item -ItemType Directory -Force -Path $textureDir | Out-Null

# Tree types and their properties
$treeTypes = @(
    @{
        Name = "temperate_canopy"
        LogTexture = "oak_log"
        LeafTexture = "oak_leaves"
        Height = 10..16
        Width = 2..4
    },
    @{
        Name = "highland_pine"
        LogTexture = "spruce_log"
        LeafTexture = "spruce_leaves"
        Height = 8..14
        Width = 1..3
    },
    @{
        Name = "river_willow"
        LogTexture = "dark_oak_log"
        LeafTexture = "birch_leaves"
        Height = 6..10
        Width = 3..6
    },
    @{
        Name = "meadow_blossom"
        LogTexture = "cherry_log"
        LeafTexture = "cherry_leaves"
        Height = 4..8
        Width = 2..5
    },
    @{
        Name = "scrub_ancient"
        LogTexture = "mangrove_log"
        LeafTexture = "mangrove_leaves"
        Height = 12..20
        Width = 4..7
    }
)

# Generate tree models
foreach ($tree in $treeTypes) {
    $modelJson = @{
        parent = "block/cube_column"
        textures = @{
            end = "minecraft:block/" + $tree.LogTexture + "_top"
            side = "minecraft:block/" + $tree.LogTexture
        }
    } | ConvertTo-Json -Depth 10 -Compress
    
    $modelJson = $modelJson -replace '("\w+"):', '$1: '
    $modelJson = $modelJson -replace '("[\w_]+"):', '    $1: '
    
    $outputFile = "$outputDir/" + $tree.Name + "_log.json"
    $modelJson | Out-File -FilePath $outputFile -Encoding utf8
    
    Write-Host "Generated: $outputFile"
}

# Generate leaf block models
foreach ($tree in $treeTypes) {
    $modelJson = @{
        parent = "block/leaves"
        textures = @{
            all = "minecraft:block/" + $tree.LeafTexture
        }
    } | ConvertTo-Json -Depth 10 -Compress
    
    $modelJson = $modelJson -replace '("\w+"):', '$1: '
    $modelJson = $modelJson -replace '("[\w_]+"):', '    $1: '
    
    $outputFile = "$outputDir/" + $tree.Name + "_leaves.json"
    $modelJson | Out-File -FilePath $outputFile -Encoding utf8
    
    Write-Host "Generated: $outputFile"
}

# Generate sapling models
foreach ($tree in $treeTypes) {
    $modelJson = @{
        parent = "minecraft:block/cross"
        textures = @{
            cross = "chex:block/kepler/" + $tree.Name + "_sapling"
        }
    } | ConvertTo-Json -Depth 10 -Compress
    
    $modelJson = $modelJson -replace '("\w+"):', '$1: '
    $modelJson = $modelJson -replace '("[\w_]+"):', '    $1: '
    
    $outputFile = "$outputDir/" + $tree.Name + "_sapling.json"
    $modelJson | Out-File -FilePath $outputFile -Encoding utf8
    
    Write-Host "Generated: $outputFile"
}

Write-Host "Kepler tree asset generation complete!"
