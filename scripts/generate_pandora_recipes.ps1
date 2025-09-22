# Create recipes directory
$recipesDir = "forge/src/main/resources/data/chex/recipes"
New-Item -ItemType Directory -Force -Path $recipesDir | Out-Null

# 1. Smelting recipes
$smeltingRecipes = @(
    @{ Input = "chex:pandorite_stone"; Output = "chex:polished_pandorite"; Experience = 0.1; CookingTime = 200 }
)

# 2. Stonecutting recipes
$stonecuttingRecipes = @(
    @{ Input = "chex:pandorite_stone"; Output = "chex:polished_pandorite"; Count = 1 },
    @{ Input = "chex:polished_pandorite"; Output = "chex:pandorite_bricks"; Count = 1 },
    @{ Input = "chex:polished_pandorite"; Output = "chex:chiseled_pandorite"; Count = 1 },
    @{ Input = "chex:polished_pandorite"; Output = "chex:pandorite_pillar"; Count = 1 }
)

# 3. Shaped crafting recipes
$shapedRecipes = @(
    @{
        Output = "chex:polished_pandorite";
        Pattern = @("##", "##");
        Key = @{
            "#" = @{ "item" = "chex:pandorite_stone" }
        };
        Count = 4
    },
    @{
        Output = "chex:pandorite_bricks";
        Pattern = @("##", "##");
        Key = @{
            "#" = @{ "item" = "chex:polished_pandorite" }
        };
        Count = 4
    },
    @{
        Output = "chex:pandorite_pillar";
        Pattern = @("#", "#");
        Key = @{
            "#" = @{ "item" = "chex:polished_pandorite" }
        };
        Count = 2
    },
    @{
        Output = "chex:crystal_clad_pandorite";
        Pattern = @("###", "#X#", "###");
        Key = @{
            "#" = @{ "item" = "chex:polished_pandorite" };
            "X" = @{ "item" = "chex:pandoran_crystal_cluster" }
        };
        Count = 8
    }
)

# 4. Shapeless recipes
$shapelessRecipes = @(
    @{
        Output = "chex:biolume_moss";
        Ingredients = @(
            @{ "item" = "chex:spore_soil" },
            @{ "item" = "minecraft:bone_meal" },
            @{ "item" = "minecraft:glowstone_dust" }
        );
        Count = 1
    },
    @{
        Output = "chex:lumicoral";
        Ingredients = @(
            @{ "item" = "minecraft:kelp" },
            @{ "item" = "chex:biolume_moss" },
            @{ "item" = "minecraft:glow_ink_sac" }
        );
        Count = 2
    }
)

# Generate smelting recipes
foreach ($recipe in $smeltingRecipes) {
    $recipeObj = @{
        type = "minecraft:smelting"
        ingredient = @{ item = $recipe.Input }
        result = $recipe.Output
        experience = $recipe.Experience
        cookingtime = $recipe.CookingTime
    }
    
    $outputName = ($recipe.Output -split ":")[1]
    $recipePath = "$recipesDir/${outputName}_from_smelting.json"
    $recipeObj | ConvertTo-Json -Depth 10 | Out-File -FilePath $recipePath -Encoding utf8
    Write-Host "Created smelting recipe: $recipePath"
}

# Generate stonecutting recipes
foreach ($recipe in $stonecuttingRecipes) {
    $recipeObj = @{
        type = "minecraft:stonecutting"
        ingredient = @{ item = $recipe.Input }
        result = $recipe.Output
        count = $recipe.Count
    }
    
    $inputName = ($recipe.Input -split ":")[1]
    $outputName = ($recipe.Output -split ":")[1]
    $recipePath = "$recipesDir/${outputName}_from_${inputName}_stonecutting.json"
    $recipeObj | ConvertTo-Json -Depth 10 | Out-File -FilePath $recipePath -Encoding utf8
    Write-Host "Created stonecutting recipe: $recipePath"
}

# Generate shaped recipes
foreach ($recipe in $shapedRecipes) {
    $recipeObj = @{
        type = "minecraft:crafting_shaped"
        pattern = $recipe.Pattern
        key = $recipe.Key
        result = @{
            item = $recipe.Output
            count = $recipe.Count
        }
    }
    
    $outputName = ($recipe.Output -split ":")[1]
    $recipePath = "$recipesDir/${outputName}.json"
    $recipeObj | ConvertTo-Json -Depth 10 | Out-File -FilePath $recipePath -Encoding utf8
    Write-Host "Created shaped recipe: $recipePath"
}

# Generate shapeless recipes
foreach ($recipe in $shapelessRecipes) {
    $recipeObj = @{
        type = "minecraft:crafting_shapeless"
        ingredients = $recipe.Ingredients
        result = @{
            item = $recipe.Output
            count = $recipe.Count
        }
    }
    
    $outputName = ($recipe.Output -split ":")[1]
    $recipePath = "$recipesDir/${outputName}.json"
    $recipeObj | ConvertTo-Json -Depth 10 | Out-File -FilePath $recipePath -Encoding utf8 -Force
    Write-Host "Created shapeless recipe: $recipePath"
}

Write-Host "All Pandora block recipes have been generated!" -ForegroundColor Green
