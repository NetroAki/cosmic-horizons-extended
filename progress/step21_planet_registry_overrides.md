# Step 21 - Planet Registry Override Merge (2025-09-21)

## Context
- Task: T-005 Planet Registry Override Merge
- Docs referenced: CHEX_DETAILED_TASKS.md §1, tasks/2025-09-21/T-005_planet-registry-overrides.md, notes/planet_registry_overrides.md

## Actions
- Updated `PlanetOverrideMerger.merge` to union hazard sets while still allowing explicit clears via empty arrays.
- Adjusted `PlanetRegistry.applyOverrides` to respect explicit hazard overrides and fall back gracefully when no override is present.
- Added `PlanetOverrideMergerTest` covering full override, partial override, and hazard clearing scenarios.
- Installed OpenJDK 17 to satisfy Gradle toolchain requirements for running tests/formatters.
- Ran `:common:test` to execute the new unit tests and `spotlessApply` for common/forge modules.

## Results
- Hazard overrides now merge correctly without losing defaults and can explicitly clear inherited hazards.
- Override pipeline verified through unit tests ensuring suit/fuel/description/hazard behaviour.
- Formatting conforms to Spotless requirements.

## Follow-ups / Notes
- Full `./gradlew check` still fails because Loom remap encounters missing optional dependencies (HideFromJS). Capture log in chunk `0a29f3`. Future agents may need to address underlying dependency gaps if full build enforcement is required.
