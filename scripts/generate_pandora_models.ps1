# Create necessary directories
$baseDir = "forge/src/main/resources/assets/chex"
$blockStatesDir = "$baseDir/blockstates"
$blockModelsDir = "$baseDir/models/block"
$itemModelsDir = "$baseDir/models/item"

# Ensure directories exist
@($blockStatesDir, $blockModelsDir, $itemModelsDir) | ForEach-Object {
    New-Item -ItemType Directory -Force -Path $_ | Out-Null
}

# Define block properties
$blocks = @(
    @{ Name = "pandorite_bricks"; Type = "simple" },
    @{ Name = "chiseled_pandorite"; Type = "simple" },
    @{ Name = "spore_soil"; Type = "simple" },
    @{ Name = "biolume_moss"; Type = "simple" },
    @{ Name = "crystal_clad_pandorite"; Type = "simple" },
    @{ Name = "lumicoral"; Type = "simple" },
    @{ Name = "pandoran_crystal_cluster"; Type = "cross" }
)

# Generate block models, blockstates, and item models
foreach ($block in $blocks) {
    $name = $block.Name
    $type = $block.Type
    
    # Block model
    $modelContent = if ($type -eq "cross") {
        @"
{
  "parent": "minecraft:block/cross",
  "textures": {
    "cross": "chex:block/$name"
  },
  "render_type": "cutout"
}
"@
    } else {
        @"
{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "chex:block/$name"
  }
}
"@
    }
    
    $modelPath = "$blockModelsDir/$name.json"
    $modelContent | Out-File -FilePath $modelPath -Encoding utf8
    Write-Host "Created block model: $modelPath"
    
    # Blockstate
    $stateContent = @"
{
  "variants": {
    "": {
      "model": "chex:block/$name"
    }
  }
}
"@
    $statePath = "$blockStatesDir/$name.json"
    $stateContent | Out-File -FilePath $statePath -Encoding utf8
    Write-Host "Created blockstate: $statePath"
    
    # Item model
    $itemContent = @"
{
  "parent": "chex:block/$name"
}
"@
    $itemPath = "$itemModelsDir/$name.json"
    $itemContent | Out-File -FilePath $itemPath -Encoding utf8
    Write-Host "Created item model: $itemPath"
}

Write-Host "All Pandora block models, blockstates, and item models have been generated!" -ForegroundColor Green
