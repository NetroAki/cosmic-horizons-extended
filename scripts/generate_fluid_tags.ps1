# Create the fluids tag directory if it doesn't exist
$tagsDir = "src/main/resources/data/chex/tags/fluids"
New-Item -ItemType Directory -Force -Path $tagsDir | Out-Null

# Define all our fuel fluids
$fluids = @(
    "kerosene",
    "rp1",
    "lox",
    "lh2",
    "dt_mix",
    "he3_blend",
    "exotic_mix"
)

# Create a tag file for each fluid
foreach ($fluid in $fluids) {
    $tagPath = "$tagsDir/$fluid.json"
    if (-not (Test-Path $tagPath)) {
        $tagContent = @"
{
  "replace": false,
  "values": [
    "chex:$fluid",
    "chex:flowing_$fluid"
  ]
}
"@
        Set-Content -Path $tagPath -Value $tagContent -NoNewline
        Write-Host "Created fluid tag for $fluid"
    }
}

# Create a common tag for all fuel fluids
$commonTagPath = "$tagsDir/fuels.json"
$commonTagContent = @"
{
  "replace": false,
  "values": [
    "chex:kerosene",
    "chex:flowing_kerosene",
    "chex:rp1",
    "chex:flowing_rp1",
    "chex:lox",
    "chex:flowing_lox",
    "chex:lh2",
    "chex:flowing_lh2",
    "chex:dt_mix",
    "chex:flowing_dt_mix",
    "chex:he3_blend",
    "chex:flowing_he3_blend",
    "chex:exotic_mix",
    "chex:flowing_exotic_mix"
  ]
}
"@
Set-Content -Path $commonTagPath -Value $commonTagContent -NoNewline
Write-Host "Created common fuel fluids tag"
