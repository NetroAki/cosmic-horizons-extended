# Configuration
$modId = "chex"
$fluids = @(
    "kerosene",
    "rp1",
    "lox",
    "lh2",
    "dt_mix",
    "he3_blend",
    "exotic_mix"
)

# Base paths
$assetsDir = "src/main/resources/assets"
$blockstatesDir = "$assetsDir/$modId/blockstates"
$modelsBlockDir = "$assetsDir/$modId/models/block"
$modelsItemDir = "$assetsDir/$modId/models/item"

# Create directories if they don't exist
New-Item -ItemType Directory -Force -Path $blockstatesDir | Out-Null
New-Item -ItemType Directory -Force -Path $modelsBlockDir | Out-Null
New-Item -ItemType Directory -Force -Path $modelsItemDir | Out-Null

# Generate blockstate JSON
$blockstateTemplate = @'
{
  "variants": {
    "": {
      "model": "{0}:block/{1}_fluid_block"
    }
  }
}
'@

# Generate fluid model JSON
$fluidModelTemplate = @'
{
  "parent": "minecraft:block/water",
  "textures": {
    "particle": "{0}:block/fluid/{1}_still"
  }
}
'@

# Generate bucket model JSON
$bucketModelTemplate = @'
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/bucket"
  },
  "display": {
    "gui": {
      "rotation": [30, 225, 0],
      "translation": [0, 0, 0],
      "scale": [0.6, 0.6, 0.6]
    }
  }
}
'@

# Generate all JSON files
foreach ($fluid in $fluids) {
    # Blockstate
    $blockstateContent = $blockstateTemplate -f $modId, $fluid
    $blockstatePath = "$blockstatesDir/$($fluid)_fluid_block.json"
    $blockstateContent | Out-File -FilePath $blockstatePath -Encoding utf8
    
    # Fluid model
    $fluidModelContent = $fluidModelTemplate -f $modId, $fluid
    $fluidModelPath = "$modelsBlockDir/$($fluid)_fluid_block.json"
    $fluidModelContent | Out-File -FilePath $fluidModelPath -Encoding utf8
    
    # Bucket model
    $bucketModelContent = $bucketModelTemplate
    $bucketModelPath = "$modelsItemDir/$($fluid)_bucket.json"
    $bucketModelContent | Out-File -FilePath $bucketModelPath -Encoding utf8
}

Write-Host "Generated fluid assets for $($fluids.Count) fluids."
