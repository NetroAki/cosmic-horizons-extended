# Planet Registry Overrides - Plan

## Task Reference
- CHEX_DETAILED_TASKS.md §1 (Planet registry override merge & tests)
- Task brief: tasks/2025-09-21/T-005_planet-registry-overrides.md

## Current Observations
- `PlanetRegistry.registerDiscoveredPlanets` already loads overrides and calls `applyOverrides`, but hazard lists are replaced instead of merged.
- `PlanetOverrideMerger` in common handles the merge logic and is testable from the common module.
- Need coverage that override inputs update fuel/suit/description while respecting defaults when values are missing or blank.
- Explicitly empty hazard arrays should clear hazards; populated arrays should union with base hazards to avoid losing defaults.

## Actionable Steps
1. Update `PlanetOverrideMerger.merge` so hazard overrides union with base hazards (and respect explicit clears).
2. Adjust `PlanetRegistry.applyOverrides` to honour explicit hazard overrides, including the ability to clear hazards.
3. Add JUnit 5 tests under `common/src/test/java/com/netroaki/chex/config/` validating:
   - Full override (fuel, suit tag/tier, description, hazards union).
   - Partial override with missing fields retains base values.
   - Empty hazard override clears inherited hazards.
4. Run Spotless + Gradle checks per workflow.
5. Capture milestone in `progress/` and append to `PROGRESS_PROMPTS.md`.
