# Create placeholder textures for fuel buckets
$textureDir = "forge/src/main/resources/assets/chex/textures/item"

# Create placeholder text files as textures
@("dt_mix_bucket", "he3_blend_bucket", "exotic_mix_bucket") | ForEach-Object {
    $textureFile = "$textureDir\$_.png"
    "Placeholder texture for $_" | Out-File -FilePath $textureFile -NoNewline
    Write-Host "Created placeholder texture: $textureFile"
}

Write-Host "Created all placeholder textures in $textureDir"
