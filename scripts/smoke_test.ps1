# CHEX Smoke Test Script (non-destructive)
# Usage: run in a dev instance with a test world loaded where applicable

Write-Host "=== CHEX Smoke Test ==="

# 1) Check Gradle build (no tests expected here)
Write-Host "[1/6] Gradle build check"
./gradlew -q :common:classes :forge:classes | Out-Host

# 2) Validate that key resource files exist
Write-Host "[2/6] Resource presence checks"
$paths = @(
  "forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json",
  "forge/src/main/resources/assets/cosmic_horizons_extended/sounds.json",
  "forge/src/main/java/com/netroaki/chex/integration/jei/CHEXJeiPlugin.java",
  "common/src/main/java/com/netroaki/chex/config/SuitHazardsConfigCore.java",
  "common/src/main/java/com/netroaki/chex/config/MineralsRuntime.java",
  "forge/src/main/java/com/netroaki/chex/hooks/LaunchHooks.java"
)
foreach ($p in $paths) {
  if (Test-Path $p) { Write-Host "OK  $p" } else { Write-Warning "MISSING  $p" }
}

# 3) Print recent configuration statuses (placeholder)
Write-Host "[3/6] Print config hints"
Write-Host "Suit hazards config: data/cosmic_horizons_extended/config/chex-suit-hazards.json5"
Write-Host "Visual filters config: data/cosmic_horizons_extended/config/chex-visual-filters.json5"

# 4) Static grep for localized keys used by toasts/tooltips
Write-Host "[4/6] Localized key grep"
Select-String -Path "forge/src/main/resources/assets/cosmic_horizons_extended/lang/en_us.json" -Pattern "chex.toast.launch_denied|chex.tooltip|jei.cosmic_horizons_extended" | Out-Host

# 5) Summarize JEI categories registration (static scan)
Write-Host "[5/6] JEI plugin scan"
Select-String -Path "forge/src/main/java/com/netroaki/chex/integration/jei/CHEXJeiPlugin.java" -Pattern "addRecipeCategories|addRecipes" | Out-Host

# 6) Completion message
Write-Host "[6/6] Smoke test complete (static checks). For in-game checks:"
Write-Host " - Use /chex minerals reload and verify success message"
Write-Host " - Attempt launch to gated destination to see localized deny toasts"
Write-Host " - Inspect JEI categories for CHEX entries"
