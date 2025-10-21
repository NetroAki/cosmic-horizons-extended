# Script to generate luminous kelp plant models for all growth stages

$modelsDir = "common\src\main\resources\assets\chex\models\block"

# Create models directory if it doesn't exist
if (-not (Test-Path -Path $modelsDir)) {
    New-Item -ItemType Directory -Path $modelsDir -Force | Out-Null
}

# Function to create a kelp plant model file
function Create-KelpModel($age, $glowing) {
    $suffix = if ($glowing) { "_glow" } else { "" }
    $modelName = "luminous_kelp_plant${age}${suffix}.json"
    $modelPath = Join-Path $modelsDir $modelName
    
    $texture = if ($glowing) { "luminous_kelp_plant_glow" } else { "luminous_kelp_plant" }
    
    $modelContent = @"
{
    "parent": "minecraft:block/cross",
    "textures": {
        "cross": "chex:block/${texture}${age}"
    },
    "render_type": "cutout"
}
"@
    
    $modelContent | Out-File -FilePath $modelPath -Encoding utf8
    Write-Host "Created model: $modelName"
}

# Generate models for all growth stages (0-15) for both glowing and non-glowing states
for ($i = 0; $i -le 15; $i++) {
    Create-KelpModel -age $i -glowing $false
    Create-KelpModel -age $i -glowing $true
}

Write-Host "All kelp plant models have been generated."
