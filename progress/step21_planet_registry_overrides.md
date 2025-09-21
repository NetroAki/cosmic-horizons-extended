# Step 21 - Planet Registry Overrides (2025-09-21)

## Objectives
- Close CHEX_DETAILED_TASKS.md §1 task for merging Cosmic Horizons discovery data with config overrides.
- Provide regression coverage validating suit/fuel/description/hazard override behaviour.

## Actions
- Extended `PlanetOverrideMerger.merge` to sanitize override fields, ignore blanks, and union hazard sets with discovered data while preserving lowercase normalization.
- Added `common/src/test/java/com/netroaki/chex/config/PlanetOverrideMergerTest.java` covering override priority, suit-tier fallback, and hazard merging semantics.
- Ran Spotless formatters for common + forge modules.

## Testing & Validation
- `./gradlew --console=plain :common:test`
  - Confirms new unit tests pass (common module).
- `./gradlew --console=plain :common:spotlessApply :forge:spotlessApply`
  - Formatting applied successfully (loom remap spam still appears but task exits cleanly).
- `./gradlew --console=plain check`
  - Fails due to pre-existing forge compilation errors (missing GTCEu/JEI sources) and spotlessMarkdown on legacy progress docs; override work validated separately via module tests.

## Follow-ups
- Resolve Forge compilation failures (missing method references, outdated TerraBlender constants) in a dedicated task.
- Decide how to handle Spotless markdown failures on inherited docs.
