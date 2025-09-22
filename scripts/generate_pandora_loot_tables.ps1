# Create loot tables directory
$lootTablesDir = "forge/src/main/resources/data/chex/loot_tables/blocks"
New-Item -ItemType Directory -Force -Path $lootTablesDir | Out-Null

# Define blocks and their loot table properties
$blocks = @(
    @{ Name = "pandorite_stone"; Drop = "pandorite_stone"; SilkTouch = $true; Fortune = $false },
    @{ Name = "polished_pandorite"; Drop = "polished_pandorite"; SilkTouch = $true; Fortune = $false },
    @{ Name = "pandorite_bricks"; Drop = "pandorite_bricks"; SilkTouch = $true; Fortune = $false },
    @{ Name = "chiseled_pandorite"; Drop = "chiseled_pandorite"; SilkTouch = $true; Fortune = $false },
    @{ Name = "pandorite_pillar"; Drop = "pandorite_pillar"; SilkTouch = $true; Fortune = $false },
    @{ Name = "spore_soil"; Drop = "spore_soil"; SilkTouch = $true; Fortune = $false },
    @{ Name = "biolume_moss"; Drop = "biolume_moss"; SilkTouch = $true; Fortune = $false },
    @{ Name = "crystal_clad_pandorite"; Drop = "crystal_clad_pandorite"; SilkTouch = $true; Fortune = $false },
    @{ Name = "lumicoral"; Drop = "lumicoral"; SilkTouch = $true; Fortune = $false },
    @{ Name = "pandoran_crystal_cluster"; Drop = "pandoran_crystal_cluster"; SilkTouch = $true; Fortune = $false }
)

# Generate loot tables
foreach ($block in $blocks) {
    $name = $block.Name
    $drop = $block.Drop
    $silkTouch = $block.SilkTouch
    $fortune = $block.Fortune
    
    $lootTable = @{
        type = "minecraft:block"
        pools = @(
            @{
                rolls = 1
                entries = @(
                    @{
                        type = "minecraft:item"
                        name = "chex:$drop"
                    }
                )
                conditions = @()
            }
        )
    }
    
    # Add silk touch condition if needed
    if ($silkTouch) {
        $lootTable.pools[0].entries[0].conditions = @(
            @{
                condition = "minecraft:survives_explosion"
            },
            @{
                condition = "minecraft:match_tool"
                predicate = @{
                    enchantments = @(
                        @{
                            enchantment = "minecraft:silk_touch"
                            levels = @{
                                min = 1
                            }
                        }
                    )
                }
            }
        )
    }
    
    # Add fortune condition if needed
    if ($fortune) {
        $lootTable.pools[0].bonus_rolls = 1
        $lootTable.pools[0].bonus_rolls = @{
            type = "minecraft:uniform"
            min = 0.0
            max = 1.0
        }
        $lootTable.pools[0].conditions = @(
            @{
                condition = "minecraft:table_bonus"
                enchantment = "minecraft:fortune"
                chances = @(0.33, 0.5, 0.6, 0.75, 1.0)
            }
        )
    }
    
    # Save the loot table
    $lootTablePath = "$lootTablesDir/$name.json"
    $lootTable | ConvertTo-Json -Depth 10 | Out-File -FilePath $lootTablePath -Encoding utf8
    Write-Host "Created loot table: $lootTablePath"
}

Write-Host "All Pandora block loot tables have been generated!" -ForegroundColor Green
