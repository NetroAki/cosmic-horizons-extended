# Create the structure template for the Colossus Arena
$structureDir = "common/src/main/resources/data/chex/structures/verdant_colossus_arena"
$templateFile = "$structureDir/colossus_arena.nbt"

# Create directories if they don't exist
if (-not (Test-Path $structureDir)) {
    New-Item -ItemType Directory -Path $structureDir -Force | Out-Null
}

# Create a simple structure template with a boss spawn marker
# This is a placeholder - in a real scenario, you'd create this in-game using structure blocks
# and then export it using the /structure command
$templateData = @"
{
    "size": [32, 16, 32],
    "palette": [
        {
            "Name": "minecraft:stone"
        },
        {
            "Name": "minecraft:oak_log"
        },
        {
            "Name": "minecraft:oak_planks"
        },
        {
            "Name": "minecraft:structure_block",
            "Properties": {
                "mode": "data"
            },
            "nbt": {
                "metadata": "boss_spawn"
            }
        }
    ],
    "blocks": [
        {
            "pos": [0, 0, 0],
            "state": 0
        },
        {
            "pos": [15, 0, 15],
            "state": 0
        },
        {
            "pos": [15, 0, 0],
            "state": 0
        },
        {
            "pos": [0, 0, 15],
            "state": 0
        },
        {
            "pos": [8, 1, 8],
            "state": 3,
            "nbt": {
                "id": "minecraft:structure_block",
                "metadata": "boss_spawn"
            }
        }
    ]
}
"@

# Save the template
$templateData | Out-File -FilePath "$structureDir/colossus_arena.json" -Encoding utf8

Write-Host "Colossus Arena structure template created at $structureDir/colossus_arena.json"
Write-Host "Note: This is a placeholder. You should create the actual structure in-game using structure blocks and export it with /structure save chex:verdant_colossus_arena <from> <to>"
