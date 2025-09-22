# Create output directory if it doesn't exist
$outputDir = "forge/src/main/resources/assets/chex/textures/block"
New-Item -ItemType Directory -Force -Path $outputDir | Out-Null

# Function to create a simple colored texture using base64-encoded 1x1 pixel
function New-ColorTexture {
    param (
        [string]$name,
        [string]$hexColor
    )
    
    $outputPath = Join-Path $outputDir "$name.png"
    
    # Base64-encoded 1x1 pixel PNG with the specified color
    $base64 = @{
        "pandorite_stone" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "polished_pandorite" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "pandorite_bricks" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "chiseled_pandorite" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "pandorite_pillar_top" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "pandorite_pillar_side" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "spore_soil" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "biolume_moss" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "crystal_clad_pandorite" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "lumicoral" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
        "pandoran_crystal_cluster" = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z/C/n4EIwKj0L58AAAAASUVORK5CYII="
    }
    
    if ($base64.ContainsKey($name)) {
        [System.IO.File]::WriteAllBytes($outputPath, [System.Convert]::FromBase64String($base64[$name]))
        Write-Host "Generated placeholder texture: $outputPath"
    } else {
        # Fallback to a transparent pixel if the name isn't in our dictionary
        [System.IO.File]::WriteAllBytes($outputPath, [System.Convert]::FromBase64String("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="))
        Write-Host "Generated fallback texture: $outputPath"
    }
}

# Generate all textures
@(
    "pandorite_stone",
    "polished_pandorite",
    "pandorite_bricks",
    "chiseled_pandorite",
    "pandorite_pillar_top",
    "pandorite_pillar_side",
    "spore_soil",
    "biolume_moss",
    "crystal_clad_pandorite",
    "lumicoral",
    "pandoran_crystal_cluster"
) | ForEach-Object {
    New-ColorTexture -name $_ -hexColor "#000000"
}

Write-Host "All placeholder textures generated successfully!" -ForegroundColor Green
Write-Host "Note: These are 1x1 pixel placeholders. Replace with proper textures before release." -ForegroundColor Yellow
